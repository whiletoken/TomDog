package com.tomdog.reward.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GenShinHeader {

    @JsonProperty("Cookie")
    private String cookie;

    @JsonProperty("x-rpc-device_id")
    private String xRpcDeviceId;

    @JsonProperty("x-rpc-app_version")
    private String xRpcAppVersion;

    @JsonProperty("UserAgent")
    private String userAgent;

    @JsonProperty("x-rpc-client_type")
    private String xRpcClientType;

    @JsonProperty("DS")
    private String dS;

    public String getCookie() {
        return cookie;
    }

    public String getXRpcDeviceId() {
        return xRpcDeviceId;
    }

    public String getXRpcAppVersion() {
        return xRpcAppVersion;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getXRpcClientType() {
        return xRpcClientType;
    }

    public String getDS() {
        return dS;
    }
}