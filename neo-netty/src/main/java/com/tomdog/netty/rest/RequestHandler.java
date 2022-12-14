package com.tomdog.netty.rest;

import com.tomdog.netty.core.WebServer;
import com.tomdog.netty.exception.HandleRequestException;
import com.tomdog.netty.exception.ResourceNotFoundException;
import com.tomdog.netty.rest.convert.Converter;
import com.tomdog.netty.rest.convert.ConverterFactory;
import com.tomdog.netty.rest.mapping.ControllerBean;
import com.tomdog.netty.rest.mapping.ControllerMapping;
import com.tomdog.netty.rest.mapping.ControllerMappingParameter;
import com.tomdog.netty.rest.mapping.ControllerMappingRegistry;
import com.tomdog.netty.util.JacksonUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * 请求处理器类
 *
 * @author Leo
 */
@Slf4j
final class RequestHandler {

    /**
     * 处理请求
     */
    public ChannelFuture handleRequest(RequestInfo requestInfo) {
        // 查找匹配的Mapping
        ControllerMapping mapping = this.lookupMappings(requestInfo);
        if (mapping == null) {

            FullHttpRequest fullHttpRequest = requestInfo.getRequest();

            log.error("request mapping not founded,uri is {}", fullHttpRequest.uri());

            HttpContextHolder.setRequest(fullHttpRequest);
            HttpContextHolder.setResponse(requestInfo.getResponse());

            // 全局异常处理
            if (WebServer.getExceptionHandler() != null) {
                WebServer.getExceptionHandler().doHandle(new ResourceNotFoundException());
                return null;
            }
            throw new ResourceNotFoundException();
        }

        // 准备方法参数
        Object[] paramValues = new Object[mapping.getParameters().size()];
        Class<?>[] paramTypes = new Class[mapping.getParameters().size()];
        for (int i = 0; i < paramValues.length; i++) {
            ControllerMappingParameter cmp = mapping.getParameters().get(i);
            Converter<?> converter = ConverterFactory.create(cmp.getDataType());
            switch (cmp.getType()) {
                case HTTP_REQUEST -> paramValues[i] = requestInfo.getRequest();
                case HTTP_RESPONSE -> paramValues[i] = requestInfo.getResponse();
                case REQUEST_BODY -> paramValues[i] = requestInfo.getBody();
                case REQUEST_PARAM -> {
                    paramValues[i] = requestInfo.getParameters().get(cmp.getName());
                    if (converter != null) {
                        paramValues[i] = converter.convert(paramValues[i]);
                    }
                }
                case REQUEST_HEADER -> {
                    paramValues[i] = requestInfo.getParameters().get(cmp.getName());
                    if (converter != null) {
                        paramValues[i] = converter.convert(requestInfo.getHeaders().get(cmp.getName()));
                    }
                }
                case PATH_VARIABLE -> {
                    paramValues[i] = this.getPathVariable(requestInfo.getRequest().uri(), mapping.getUrl(), cmp.getName());
                    if (converter != null) {
                        paramValues[i] = converter.convert(paramValues[i]);
                    }
                }
                case URL_ENCODED_FORM -> paramValues[i] = requestInfo.getFormData();
                case UPLOAD_FILE ->
                        paramValues[i] = requestInfo.getFiles().size() > 0 ? requestInfo.getFiles().get(0) : null;
                case UPLOAD_FILES -> paramValues[i] = requestInfo.getFiles().size() > 0 ? requestInfo.getFiles() : null;
            }
            if (cmp.isRequired() && paramValues[i] == null) {
                throw new HandleRequestException("参数 " + cmp.getName() + " 为null");
            }
            paramTypes[i] = cmp.getDataType();
        }

        // 执行method
        ChannelFuture channelFuture;
        try {
            HttpContextHolder.setRequest(requestInfo.getRequest());
            HttpContextHolder.setResponse(requestInfo.getResponse());
            Object result = this.execute(mapping, paramTypes, paramValues);

            if (!(result instanceof ResponseEntity)) {
                result = ResponseEntity.ok().build(result);
            }
            channelFuture = this.writeResponse((ResponseEntity<?>) result, mapping.isJsonResponse());
        } catch (Exception e) {
            // 全局异常处理
            if (WebServer.getExceptionHandler() != null) {
                WebServer.getExceptionHandler().doHandle(e);
                return null;
            }
            throw new HandleRequestException(e);
        } finally {
            HttpContextHolder.removeRequest();
            HttpContextHolder.removeResponse();
        }
        return channelFuture;
    }

    /**
     * 得到Controller类的实例
     */
    private Object execute(ControllerMapping mapping, Class<?>[] paramTypes, Object[] paramValues) throws Exception {
        ControllerMappingRegistry controllerMappingRegistry = ControllerMappingRegistry.newInstance();
        ControllerBean bean = controllerMappingRegistry.getBean(mapping.getClassName());
        Object instance;
        if (bean.getSingleton()) {
            instance = controllerMappingRegistry.getSingleton(mapping.getClassName());
        } else {
            Class<?> clazz = Class.forName(mapping.getClassName());
            instance = clazz.newInstance();
        }
        Method method = instance.getClass().getMethod(mapping.getClassMethod(), paramTypes);
        return method.invoke(instance, paramValues);
    }

    /**
     * 输出响应结果
     */
    private ChannelFuture writeResponse(ResponseEntity<?> responseEntity, boolean jsonResponse) throws IOException {
        if (responseEntity.getBody() instanceof RandomAccessFile) {
            return this.writeFileResponse(responseEntity);
        }
        FullHttpResponse response;
        HttpResponseStatus status = HttpResponseStatus.parseLine(String.valueOf(responseEntity.getStatus().value()));
        if (responseEntity.getBody() != null) {
            String jsonStr = JacksonUtils.toJson(responseEntity.getBody());
            response = new DefaultFullHttpResponse(HTTP_1_1, status, Unpooled.copiedBuffer(jsonStr, CharsetUtil.UTF_8));
        } else {
            response = new DefaultFullHttpResponse(HTTP_1_1, status);
        }

        String contentType = jsonResponse ? "application/json; charset=UTF-8" : "text/plain; charset=UTF-8";
        response.headers().set("Content-Type", contentType);

        // 写入Cookie
        Map<String, String> cookies = HttpContextHolder.getResponse().getCookies();
        Set<Entry<String, String>> cookiesEntrySet = cookies.entrySet();
        for (Entry<String, String> entry : cookiesEntrySet) {
            Cookie cookie = new DefaultCookie(entry.getKey(), entry.getValue());
            cookie.setPath("/");
            response.headers().set(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.STRICT.encode(cookie));
        }

        Map<String, String> responseHeaders = HttpContextHolder.getResponse().getHeaders();
        Set<Entry<String, String>> headersEntrySet = responseHeaders.entrySet();
        for (Entry<String, String> entry : headersEntrySet) {
            response.headers().add(entry.getKey(), entry.getValue());
        }
        response.headers().setInt("Content-Length", response.content().readableBytes());
        return HttpContextHolder.getResponse().getChannelHandlerContext().writeAndFlush(response);
    }

    /**
     * 输出文件响应
     */
    private ChannelFuture writeFileResponse(ResponseEntity<?> responseEntity) throws IOException {
        RandomAccessFile raf = (RandomAccessFile) responseEntity.getBody();
        long fileLength = raf.length();

        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
        HttpUtil.setContentLength(response, fileLength);
        if (responseEntity.getMimetype() != null && !responseEntity.getMimetype().trim().equals("")) {
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, responseEntity.getMimetype());
        }
        if (responseEntity.getFileName() != null && !responseEntity.getFileName().trim().equals("")) {
            String fileName = new String(responseEntity.getFileName().getBytes("gb2312"), "ISO8859-1");
            response.headers().set(HttpHeaderNames.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        }
        if (HttpUtil.isKeepAlive(HttpContextHolder.getRequest())) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }

        ChannelHandlerContext ctx = HttpContextHolder.getResponse().getChannelHandlerContext();
        ctx.write(response);
        ChannelFuture sendFileFuture;
        ChannelFuture lastContentFuture;
        if (ctx.pipeline().get(SslHandler.class) == null) {
            sendFileFuture =
                    ctx.write(new DefaultFileRegion(raf.getChannel(), 0, fileLength), ctx.newProgressivePromise());
            // Write the end marker.
            lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        } else {
            sendFileFuture = ctx.writeAndFlush(new HttpChunkedInput(new ChunkedFile(raf, 0, fileLength, 8192)),
                    ctx.newProgressivePromise());
            // HttpChunkedInput will write the end marker (LastHttpContent) for us.
            lastContentFuture = sendFileFuture;
        }
        return lastContentFuture;
    }

    /**
     * 查找映射
     */
    private ControllerMapping lookupMappings(RequestInfo requestInfo) {
        String lookupPath = requestInfo.getRequest().uri().endsWith("/")
                ? requestInfo.getRequest().uri().substring(0, requestInfo.getRequest().uri().length() - 1)
                : requestInfo.getRequest().uri();
        int paramStartIndex = lookupPath.indexOf("?");
        if (paramStartIndex > 0) {
            lookupPath = lookupPath.substring(0, paramStartIndex);
        }

        Map<String, ControllerMapping> mappings = this.getMappings(requestInfo.getRequest().method().name());
        if (mappings == null || mappings.size() == 0) {
            return null;
        }
        Set<Entry<String, ControllerMapping>> entrySet = mappings.entrySet();
        for (Entry<String, ControllerMapping> entry : entrySet) {
            // 完全匹配
            if (entry.getKey().equals(lookupPath)) {
                return entry.getValue();
            }
        }
        for (Entry<String, ControllerMapping> entry : entrySet) {
            // 包含PathVariable
            String matcher = this.getMatcher(entry.getKey());
            if (lookupPath.startsWith(matcher)) {
                boolean matched = true;
                String[] lookupPathSplit = lookupPath.split("/");
                String[] mappingUrlSplit = entry.getKey().split("/");
                if (lookupPathSplit.length != mappingUrlSplit.length) {
                    continue;
                }
                for (int i = 0; i < lookupPathSplit.length; i++) {
                    if (!lookupPathSplit[i].equals(mappingUrlSplit[i])) {
                        if (!mappingUrlSplit[i].startsWith("{")) {
                            matched = false;
                            break;
                        }
                    }
                }
                if (matched) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 得到控制器映射哈希表
     */
    private Map<String, ControllerMapping> getMappings(String httpMethod) {
        if (httpMethod == null) {
            return null;
        }
        ControllerMappingRegistry controllerMappingRegistry = ControllerMappingRegistry.newInstance();
        return switch (httpMethod.toUpperCase()) {
            case "GET" -> controllerMappingRegistry.getGetMappings();
            case "POST" -> controllerMappingRegistry.getPostMappings();
            case "PUT" -> controllerMappingRegistry.getPutMappings();
            case "DELETE" -> controllerMappingRegistry.getDeleteMappings();
            case "PATCH" -> controllerMappingRegistry.getPatchMappings();
            default -> null;
        };
    }

    /**
     * 得到匹配url
     */
    private String getMatcher(String url) {
        StringBuilder matcher = new StringBuilder(128);
        for (char c : url.toCharArray()) {
            if (c == '{') {
                break;
            }
            matcher.append(c);
        }
        return matcher.toString();
    }

    /**
     * 得到路径变量
     */
    private String getPathVariable(String url, String mappingUrl, String name) {
        String[] urlSplit = url.split("/");
        String[] mappingUrlSplit = mappingUrl.split("/");
        for (int i = 0; i < mappingUrlSplit.length; i++) {
            if (mappingUrlSplit[i].equals("{" + name + "}")) {
                if (urlSplit[i].contains("?")) {
                    return urlSplit[i].split("[?]")[0];
                }
                if (urlSplit[i].contains("&")) {
                    return urlSplit[i].split("&")[0];
                }
                return urlSplit[i];
            }
        }
        return null;
    }

}
