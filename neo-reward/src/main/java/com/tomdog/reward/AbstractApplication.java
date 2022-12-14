package com.tomdog.reward;

import com.google.common.collect.Lists;
import com.spring.ioc.Injector;
import com.tomdog.netty.core.WebServer;
import com.tomdog.reward.request.api.DomainResolveApi;
import com.tomdog.reward.request.api.JingDongApi;
import com.tomdog.reward.request.api.TaskApi;
import com.tomdog.reward.request.interceptor.CorsInterceptor;
import com.tomdog.reward.request.interceptor.ExceptionController;
import com.tomdog.reward.request.interceptor.RequestIntercept;
import com.tomdog.reward.service.job.JobTaskService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public abstract class AbstractApplication {

    public static void start() {

        Injector injector = Injector.getInstance();
        log.debug("inject is {}", injector);

        injector.injectAll();
        log.info("inject success");

        injector.getBean(JobTaskService.class).doJob();
        log.info("doJob invoke");

        log.info("DataSourceService init");

        WebServer.getIgnoreUrls().add("/favicon.ico");
        WebServer.setExceptionHandler(new ExceptionController());

        WebServer webServer = new WebServer(8081);
        webServer.addInterceptor(new RequestIntercept());
        webServer.addInterceptor(new CorsInterceptor());

        List<Class<?>> list = Lists.newArrayList(DomainResolveApi.class, JingDongApi.class, TaskApi.class);
        webServer.setControllerBasePackage(list);
        try {
            webServer.start();
        } catch (Exception e) {
            log.error("start failed, exception is ", e);
        }
    }

}
