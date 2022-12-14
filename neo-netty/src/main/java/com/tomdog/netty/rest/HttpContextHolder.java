package com.tomdog.netty.rest;

import io.netty.handler.codec.http.FullHttpRequest;

/**
 * Http 上下文持有者
 *
 * @author Leo
 */
public final class HttpContextHolder {

    private static final ThreadLocal<FullHttpRequest> LOCAL_REQUEST = new ThreadLocal<>();

    private static final ThreadLocal<HttpResponse> LOCAL_RESPONSE = new ThreadLocal<>();

    /**
     * 设置Http Request
     */
    public static void setRequest(FullHttpRequest request) {
        LOCAL_REQUEST.set(request);
    }

    /**
     * 得到Http Request
     */
    public static FullHttpRequest getRequest() {
        return LOCAL_REQUEST.get();
    }

    /**
     * 删除Http Request
     */
    public static void removeRequest() {
        LOCAL_REQUEST.remove();
    }

    /**
     * 设置Http Response
     */
    public static void setResponse(HttpResponse response) {
        LOCAL_RESPONSE.set(response);
    }

    /**
     * 得到Http Response
     */
    public static HttpResponse getResponse() {
        return LOCAL_RESPONSE.get();
    }

    /**
     * 删除Http Response
     */
    public static void removeResponse() {
        LOCAL_RESPONSE.remove();
    }

}
