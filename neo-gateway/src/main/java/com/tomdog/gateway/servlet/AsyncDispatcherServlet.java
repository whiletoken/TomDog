package com.tomdog.gateway.servlet;

import com.tomdog.gateway.service.RoutingDelegate;
import com.tomdog.gateway.util.ClockUtil;
import com.tomdog.gateway.util.thread.ThreadPriorityPoolExecutor;
import com.tomdog.gateway.util.thread.TomcatBlockingQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.AsyncContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * 网关转发的核心处理逻辑 servlet3.0对于异步处理的支持
 * DispatcherServlet.doDispatch
 * 转发请求
 * 追求性能，处理网络请求的tomcat线程设为了一个，实际规则匹配和转发交给快慢线程池处理
 * 由于是异步线程，所以拦截器和过滤器部分失效
 */
@WebServlet(urlPatterns = {"/*"}, asyncSupported = true, name = "async")
@Slf4j
public class AsyncDispatcherServlet extends DispatcherServlet {

    private ThreadPriorityPoolExecutor fastExecutor;

    private ThreadPriorityPoolExecutor slowExecutor;

    // 慢请求集合
    private final Map<String, SlowRequest> slowRequest =
            new ConcurrentHashMap<>(1024);

    private static final long TIME_OUT = 10 * 1000;

    @Autowired
    private RoutingDelegate routingDelegate;

    @Override
    public void init(ServletConfig config) throws ServletException {

        log.info("AsyncDispatcherServlet init succeed");

        super.init(config);

        int coreSize = Runtime.getRuntime().availableProcessors();

        // 快慢线程，线程隔离
        fastExecutor = new ThreadPriorityPoolExecutor(coreSize / 2, coreSize, 12,
                TimeUnit.HOURS, new TomcatBlockingQueue<>(1024));

        slowExecutor = new ThreadPriorityPoolExecutor(coreSize / 2, coreSize, 12,
                TimeUnit.HOURS, new TomcatBlockingQueue<>(1024));
    }

    /**
     * 销毁单例
     */
    @Override
    public void destroy() {
        fastExecutor.shutdownNow();
        slowExecutor.shutdownNow();
        super.destroy();
    }

    /**
     * 由于是单线程处理请求任务，所以网关本身不提供其他对外http接口
     * 如果需要网关提供http接口，请增加线程数，增加对同步请求支持
     * 本案例维持纯净网关功能，只处理验证、转发
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     */
    @Override
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) {

        String uri = request.getRequestURI();
        AsyncContext asyncContext = request.startAsync(request, response);
        asyncContext.setTimeout(TIME_OUT);

        FutureTask<Boolean> task = new FutureTask<>(() -> {

            Instant instant = Instant.now();
            try {
                log.info("Dispatching request " + request);
                ResponseEntity<String> responseEntity = routingDelegate.redirect(request, "http://localhost:8080", "");
                response.getWriter().write(Objects.requireNonNull(responseEntity.getBody()));
                log.info("doDispatch returned from processing request " + request);
                asyncContext.complete();
            } catch (Exception ex) {
                log.info("Error in async request", ex);
                return false;
            } finally {
                // 超时请求加入慢请求黑名单，超过上限3次将进入slow thread pool
                if (ClockUtil.toEpochTime(instant, TimeUnit.SECONDS) > 10) {
                    if (slowRequest.containsKey(uri)) {
                        // 超过上限100次将不再新增
                        if (slowRequest.get(uri).getSlowTimes().get() <= 100) {
                            slowRequest.get(uri).addSlowTimes();
                        }
                    } else {
                        slowRequest.put(uri, new SlowRequest());
                    }
                } else if (slowRequest.containsKey(uri)) {
                    // 连续三次通过才算正常
                    if (slowRequest.get(uri).addThroughTimes() <= 0) {
                        slowRequest.remove(uri);
                    }
                }
            }
            return true;
        });

        asyncContext.addListener(new AsyncDispatcherServletListener(task));

        // 是否慢请求判断
        SlowRequest slowRequest1 = slowRequest.get(uri);
        if (slowRequest1 == null || slowRequest1.isThrough()) {
            fastExecutor.execute(task);
        } else {
            slowExecutor.execute(task);
        }
        // todo 考虑增加黑名单，单位时间内拒绝无法响应的请求
    }

}