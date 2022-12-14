package com.tomdog.reward.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class JDData {

    @JsonProperty("msgGuideSwitch")
    private String msgGuideSwitch;

    @JsonProperty("signCalendar")
    private Object signCalendar;

    @JsonProperty("sourceTips")
    private String sourceTips;

    @JsonProperty("dailyAward")
    private DailyAward dailyAward;

    @JsonProperty("signRemind")
    private Object signRemind;

    @JsonProperty("conductionBtn")
    private Object conductionBtn;

    @JsonProperty("beanUserType")
    private int beanUserType;

    @JsonProperty("recommend")
    private Object recommend;

    @JsonProperty("tomorrowSendBeans")
    private int tomorrowSendBeans;

    @JsonProperty("signedRan")
    private String signedRan;

    @JsonProperty("status")
    private String status;

    @JsonProperty("awardType")
    private String awardType;
}