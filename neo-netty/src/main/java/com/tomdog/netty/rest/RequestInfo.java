package com.tomdog.netty.rest;

import com.tomdog.netty.multipart.MultipartFile;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 请求信息类
 *
 * @author Leo
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public final class RequestInfo {

    private FullHttpRequest request;

    private HttpResponse response;

    private final Map<String, Object> parameters = new HashMap<>();

    private final Map<String, String> headers = new HashMap<>();

    private String body;

    private final Map<String, String> formData = new HashMap<>();

    private final List<MultipartFile> files = new ArrayList<>(8);

}
