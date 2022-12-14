package com.tomdog.reward.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DailyAward {

    @JsonProperty("subTitle")
    private String subTitle;

    @JsonProperty("beanAward")
    private Object beanAward;

    @JsonProperty("type")
    private String type;

    @JsonProperty("title")
    private String title;
}