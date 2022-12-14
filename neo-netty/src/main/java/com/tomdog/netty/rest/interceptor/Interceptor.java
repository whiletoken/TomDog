package com.tomdog.netty.rest.interceptor;

import com.tomdog.netty.rest.HttpResponse;

import io.netty.handler.codec.http.FullHttpRequest;

/**
 * 拦截器接口
 *
 * @author Leo
 */
public interface Interceptor {

    /**
     * 在业务处理器处理请求之前被调用
     * 如果返回false， 从当前的拦截器往回执行所有拦截器的afterCompletion()，再退出拦截器链。
     * 如果返回true，执行下一个拦截器，直到所有的拦截器都执行完毕，再执行被拦截的Controller。
     */
    boolean preHandle(FullHttpRequest request, HttpResponse response) throws Exception;

    /**
     * 业务处理器执行完成之后执行
     */
    void postHandle(FullHttpRequest request, HttpResponse response) throws Exception;

    /**
     * 完全处理完请求后被调用
     * 当有拦截器抛出异常时，会从当前拦截器往回执行所有的拦截器的afterCompletion()。
     */
    void afterCompletion(FullHttpRequest request, HttpResponse response);

}
