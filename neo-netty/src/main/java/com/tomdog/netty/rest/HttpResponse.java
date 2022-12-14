package com.tomdog.netty.rest;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Http 响应类
 *
 * @author Leo
 */
public final class HttpResponse {

    public HttpResponse(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }

    private final ChannelHandlerContext channelHandlerContext;

    private final Map<String, String> headers = new HashMap<>();

    private final Map<String, String> cookies = new HashMap<>();

    public ChannelHandlerContext getChannelHandlerContext() {
        return channelHandlerContext;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    /**
     * 输出响应
     */
    public void write(HttpStatus status, String body) {
        HttpResponseStatus responseStatus = HttpResponseStatus.parseLine(String.valueOf(status.value()));
        FullHttpResponse response;
        if (body == null || body.trim().equals("")) {
            response = new DefaultFullHttpResponse(HTTP_1_1, responseStatus);
        } else {
            response = new DefaultFullHttpResponse(HTTP_1_1, responseStatus, Unpooled.copiedBuffer(body, CharsetUtil.UTF_8));
        }

        Set<Entry<String, String>> entrySet = headers.entrySet();
        entrySet.forEach(entry -> response.headers().add(entry.getKey(), entry.getValue()));
        response.headers().setInt("Content-Length", response.content().readableBytes());
        channelHandlerContext.writeAndFlush(response);
    }

    /**
     * 关闭Channel
     */
    public void closeChannel() {
        if (channelHandlerContext != null && channelHandlerContext.channel() != null) {
            channelHandlerContext.channel().close();
        }
    }

}
