package com.tomdog.reward.util;

import lombok.Data;

import java.io.Serializable;

@Data
public class JsonResult<T> implements Serializable {

    private int code;
    private String msg;
    private T obj;

    public static <T> JsonResult<T> succeed(T obj) {
        JsonResult<T> jsonResult = new JsonResult<>();
        jsonResult.code = 200;
        jsonResult.msg = "success";
        jsonResult.obj = obj;
        return jsonResult;
    }
}
