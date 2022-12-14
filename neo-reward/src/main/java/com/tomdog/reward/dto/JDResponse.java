package com.tomdog.reward.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JDResponse {

    @JsonProperty("code")
    private String code;

    @JsonProperty("data")
    private JDData JDData;
}