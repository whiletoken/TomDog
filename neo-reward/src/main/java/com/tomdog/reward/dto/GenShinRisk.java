package com.tomdog.reward.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GenShinRisk {

	@JsonProperty("code")
	private String code;

	@JsonProperty("success")
	private int success;

	@JsonProperty("challenge")
	private String challenge;

	@JsonProperty("risk_code")
	private int riskCode;

	@JsonProperty("gt")
	private String gt;

	public String getCode(){
		return code;
	}

	public int getSuccess(){
		return success;
	}

	public String getChallenge(){
		return challenge;
	}

	public int getRiskCode(){
		return riskCode;
	}

	public String getGt(){
		return gt;
	}
}