package com.spring.servlet;

import com.spring.service.RoutingDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

@WebServlet(urlPatterns = {"/*"}, asyncSupported = true, name = "async")
@Component
public class AsyncDispatcherServlet extends DispatcherServlet {

    private ExecutorService executorService;
    private static final int NUM_ASYNC_TASKS = 15;
    private static final long TIME_OUT = 10 * 1000;
    private final Log log = LogFactory.getLog(AsyncDispatcherServlet.class);

    @Autowired
    private RoutingDelegate routingDelegate;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        executorService = Executors.newFixedThreadPool(NUM_ASYNC_TASKS);
    }

    @Override
    public void destroy() {
        executorService.shutdownNow();
        super.destroy();
    }

    @Override
    protected void doDispatch(final HttpServletRequest request, final HttpServletResponse response) {
        final AsyncContext ac = request.startAsync(request, response);
        ac.setTimeout(TIME_OUT);
        FutureTask task = new FutureTask(() -> {
            try {
                log.info("Dispatching request " + request);
                if (request.getRequestURI().contains("async")) {
                    response.getWriter().write(Objects.requireNonNull(
                            routingDelegate.redirect(request, "http://localhost:8081/", "/async/").getBody()));
                } else {
                    AsyncDispatcherServlet.super.doDispatch(request, response);
                }
                log.info("doDispatch returned from processing request " + request);
                ac.complete();
            } catch (Exception ex) {
                log.info("Error in async request", ex);
            }
        }, null);

        ac.addListener(new AsyncDispatcherServletListener(task));
        executorService.execute(task);
    }

    private class AsyncDispatcherServletListener implements AsyncListener {

        private FutureTask future;

        public AsyncDispatcherServletListener(FutureTask future) {
            this.future = future;
        }

        @Override
        public void onTimeout(AsyncEvent event) throws IOException {
            log.warn("Async request did not complete timeout occured");
            handleTimeoutOrError(event, "Request timed out");
        }

        @Override
        public void onComplete(AsyncEvent event) throws IOException {
            log.info("Completed async request");
        }

        @Override
        public void onError(AsyncEvent event) throws IOException {
            String error = (event.getThrowable() == null ? "UNKNOWN ERROR" : event.getThrowable().getMessage());
            log.error("Error in async request " + error);
            handleTimeoutOrError(event, "Error processing " + error);
        }

        @Override
        public void onStartAsync(AsyncEvent event) throws IOException {
            log.info("Async Event started..");
        }

        private void handleTimeoutOrError(AsyncEvent event, String message) {
            PrintWriter writer = null;
            try {
                future.cancel(true);
                HttpServletResponse response = (HttpServletResponse) event.getAsyncContext().getResponse();
                //HttpServletRequest request = (HttpServletRequest) event.getAsyncContext().getRequest();
                //request.getRequestDispatcher("/app/error.htm").forward(request, response);
                writer = response.getWriter();
                writer.print(message);
                writer.flush();
            } catch (Exception ex) {
                log.error(ex);
            } finally {
                event.getAsyncContext().complete();
                if (writer != null) {
                    writer.close();
                }
            }
        }
    }
}