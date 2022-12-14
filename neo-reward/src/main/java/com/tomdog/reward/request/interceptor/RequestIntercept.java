package com.tomdog.reward.request.interceptor;

import com.spring.sql.HibernateUtil;
import com.tomdog.netty.rest.HttpResponse;
import com.tomdog.netty.rest.interceptor.Interceptor;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestIntercept implements Interceptor {

    @Override
    public boolean preHandle(FullHttpRequest request, HttpResponse response) {
        return true;
    }

    @Override
    public void postHandle(FullHttpRequest request, HttpResponse response) {
        log.debug("HibernateUtil closed");
        HibernateUtil.close();
    }

    @Override
    public void afterCompletion(FullHttpRequest request, HttpResponse response) {

    }
}
