package com.tomdog.reward.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GenShinResult {

    @JsonProperty("data")
    private GenShinRisk genShinRisk;

    @JsonProperty("message")
    private String message;

    @JsonProperty("retcode")
    private int retcode;

    public GenShinRisk getData() {
        return genShinRisk;
    }

    public String getMessage() {
        return message;
    }

    public int getRetcode() {
        return retcode;
    }
}