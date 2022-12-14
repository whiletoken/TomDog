package com.tomdog.netty.rest;

import cn.hutool.core.util.StrUtil;
import com.tomdog.netty.multipart.MultipartFile;
import com.tomdog.netty.rest.interceptor.Interceptor;
import com.tomdog.netty.rest.interceptor.InterceptorRegistry;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * 请求分派器
 *
 * @author Leo
 */
public final class RequestDispatcher {

    /**
     * 执行请求分派
     */
    public void doDispatch(FullHttpRequest request, ChannelHandlerContext channelHandlerContext) throws Exception {

        HttpResponse response = new HttpResponse(channelHandlerContext);

        // 执行拦截器
        Deque<Interceptor> executedInterceptors = new ArrayDeque<>();
        List<Interceptor> allowInterceptors = new ArrayList<>();
        for (Interceptor interceptor : InterceptorRegistry.getInterceptors()) {
            if (!this.allowExecuteInterceptor(request.uri(), interceptor)) {
                continue;
            }
            allowInterceptors.add(interceptor);
            executedInterceptors.push(interceptor);
            if (interceptor.preHandle(request, response)) {
                continue;
            }
            // 调用已执行的所有拦截器的afterCompletion方法
            while (!executedInterceptors.isEmpty()) {
                executedInterceptors.pop().afterCompletion(request, response);
            }
            return;
        }

        ChannelFuture f = null;
        if (request.method().name().equalsIgnoreCase("OPTIONS")) {
            // 处理“预检”请求
            f = this.processOptionsRequest(request, response, channelHandlerContext);
        }

        if (!request.method().name().equalsIgnoreCase("OPTIONS")) {

            RequestInfo requestInfo = RequestInfo.builder().request(request).response(response).build();
            QueryStringDecoder queryStrDecoder = new QueryStringDecoder(request.uri());

            // queryStr
            Set<Entry<String, List<String>>> entrySet = queryStrDecoder.parameters().entrySet();
            entrySet.forEach(entry -> requestInfo.getParameters().put(entry.getKey(), entry.getValue().get(0)));

            Set<String> headerNames = request.headers().names();
            headerNames.forEach(headerName -> requestInfo.getHeaders().put(headerName, request.headers().get(headerName)));

            if (!request.method().name().equalsIgnoreCase("GET")) {
                String contentType = requestInfo.getHeaders().get("Content-Type");
                if (contentType != null) {
                    if (contentType.contains(";")) {
                        contentType = contentType.split(";")[0];
                    }
                    switch (contentType.toLowerCase()) {
                        case "application/json", "application/json;charset=utf-8", "text/xml" ->
                                requestInfo.setBody(request.content().toString(StandardCharsets.UTF_8));
                        case "application/x-www-form-urlencoded" -> {
                            HttpPostRequestDecoder formDecoder = new HttpPostRequestDecoder(request);
                            formDecoder.offer(request);
                            List<InterfaceHttpData> paramList = formDecoder.getBodyHttpDatas();
                            for (InterfaceHttpData param : paramList) {
                                Attribute data = (Attribute) param;
                                requestInfo.getFormData().put(data.getName(), data.getValue());
                            }
                        }
                        case "multipart/form-data" -> {
                            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(request);
                            List<InterfaceHttpData> dataList = decoder.getBodyHttpDatas();
                            for (InterfaceHttpData data : dataList) {
                                if (data.getHttpDataType() == HttpDataType.FileUpload) {
                                    FileUpload fileUpload = (FileUpload) data;
                                    if (fileUpload.isCompleted()) {
                                        MultipartFile file = MultipartFile.builder()
                                                .fileData(fileUpload.get())
                                                .fileName(fileUpload.getFilename())
                                                .fileType(fileUpload.getContentType()).build();
                                        requestInfo.getFiles().add(file);
                                    }
                                    continue;
                                }
                                if (data.getHttpDataType() == HttpDataType.Attribute) {
                                    Attribute attribute = (Attribute) data;
                                    requestInfo.getFormData().put(attribute.getName(), attribute.getValue());
                                }
                            }
                        }
                    }
                }
            }

            f = new RequestHandler().handleRequest(requestInfo);
        }

        // 执行拦截器
        for (Interceptor interceptor : allowInterceptors) {
            interceptor.postHandle(request, response);
        }

        // 如果是“预检”请求，则处理后关闭连接。
        if (request.method().name().equalsIgnoreCase("OPTIONS")) {
            if (f != null) {
                f.addListener(ChannelFutureListener.CLOSE);
            }
            return;
        }
        if (!HttpUtil.isKeepAlive(request)) {
            Objects.requireNonNull(f).addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * 判断是否执行拦截器
     */
    private boolean allowExecuteInterceptor(String url, Interceptor interceptor) {
        List<String> excludeMappings = InterceptorRegistry.getExcludeMappings(interceptor);
        return excludeMappings == null || excludeMappings.stream().noneMatch(url::startsWith);
    }

    /**
     * 处理Options请求
     */
    private ChannelFuture processOptionsRequest(FullHttpRequest request, HttpResponse response, ChannelHandlerContext channelHandlerContext) {
        List<String> requestHeaders = Pattern.compile(",").splitAsStream(request.headers().get("Access-Control-Request-Headers")).toList();
        for (String requestHeader : requestHeaders) {
            if (StrUtil.isBlank(requestHeader)) {
                continue;
            }
            if (!this.requestHeaderAllowed(requestHeader, response.getHeaders())) {
                FullHttpResponse optionsResponse = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.NOT_FOUND,
                        Unpooled.copiedBuffer("", CharsetUtil.UTF_8));
                HttpContextHolder.getResponse().getChannelHandlerContext().writeAndFlush(optionsResponse).addListener(ChannelFutureListener.CLOSE);
                return null;
            }
        }
        FullHttpResponse optionsResponse = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK,
                Unpooled.copiedBuffer("", CharsetUtil.UTF_8));
        Map<String, String> responseHeaders = response.getHeaders();
        Set<Entry<String, String>> headersEntrySet = responseHeaders.entrySet();
        headersEntrySet.forEach(entry -> optionsResponse.headers().add(entry.getKey(), entry.getValue()));
        optionsResponse.headers().setInt("Content-Length", optionsResponse.content().readableBytes());
        return channelHandlerContext.writeAndFlush(optionsResponse);
    }

    /**
     * 判断请求头是否被允许
     */
    private boolean requestHeaderAllowed(String requestHeader, Map<String, String> responseHeaders) {
        String allowedHeader = responseHeaders.get("Access-Control-Allow-Headers");
        return StrUtil.isNotBlank(allowedHeader)
                && Pattern.compile(",").splitAsStream(allowedHeader).anyMatch(requestHeader::equalsIgnoreCase);
    }

}
