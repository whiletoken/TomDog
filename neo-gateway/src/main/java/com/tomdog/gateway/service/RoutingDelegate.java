package com.tomdog.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 转发http请求，对于restTemplate的二次封装
 * 主要功能是复制请求头+请求体然后使用restTemplate转发请求
 */
@Service
public class RoutingDelegate {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 转发
     */
    public ResponseEntity<String> redirect(HttpServletRequest request, String routeUrl, String prefix) {
        try {
            // build up the redirect URL
            String redirectUrl = this.createRedirectUrl(request, routeUrl, prefix);
            RequestEntity<Object> requestEntity = this.createRequestEntity(request, redirectUrl);
            return this.route(requestEntity);
        } catch (Exception e) {
            return new ResponseEntity<>("REDIRECT ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 拼接请求URL
     */
    private String createRedirectUrl(HttpServletRequest request, String routeUrl, String prefix) {
        String queryString = request.getQueryString();
        queryString = queryString != null ? "?" + queryString : "";
        String uri = request.getRequestURI().replace(prefix, "");
        return routeUrl + uri + queryString;
    }

    /**
     * 拼接请求头+请求体
     */
    private RequestEntity<Object> createRequestEntity(HttpServletRequest request, String url) throws URISyntaxException, IOException {
        String method = request.getMethod();
        HttpMethod httpMethod = HttpMethod.resolve(method);
        MultiValueMap<String, String> headers = this.parseRequestHeader(request);
        byte[] body = this.parseRequestBody(request);
        return new RequestEntity<>(body, headers, httpMethod, new URI(url));
    }

    /**
     * 路由寻址，转发请求
     */
    private ResponseEntity<String> route(RequestEntity<Object> requestEntity) {
        return this.restTemplate.exchange(requestEntity, String.class);
    }

    /**
     * 读取数据流，byte数组接收
     */
    private byte[] parseRequestBody(HttpServletRequest request) throws IOException {
        InputStream inputStream = request.getInputStream();
        return StreamUtils.copyToByteArray(inputStream);
    }

    /**
     * 拼接请求头
     */
    private MultiValueMap<String, String> parseRequestHeader(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        while (request.getHeaderNames().hasMoreElements()) {
            String headerName = request.getHeaderNames().nextElement();
            while (request.getHeaders(headerName).hasMoreElements()) {
                String headerValue = request.getHeaders(headerName).nextElement();
                headers.add(headerName, headerValue);
            }
        }
        return headers;
    }
}