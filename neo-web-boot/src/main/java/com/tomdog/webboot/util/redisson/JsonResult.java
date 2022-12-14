package com.tomdog.webboot.util.redisson;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * JsonResult
 *
 * @author william
 **/
@Getter
@Setter
@NoArgsConstructor
public class JsonResult<T> implements Serializable {

    private String success;

    private T obj;

    private String code;

    public static JsonResult<String> succeed() {
        return new JsonResult<>("success", StrUtil.EMPTY, "200");
    }

    public static <T> JsonResult<T> succeed(T obj) {
        return new JsonResult<>("success", obj, "200");
    }

    public static <T> JsonResult<T> failed(T obj) {
        return new JsonResult<>("failed", obj, "200");
    }

    public static JsonResult<String> failed() {
        return new JsonResult<>("failed", StrUtil.EMPTY, "200");
    }

    public JsonResult(String success, T obj, String code) {
        this.success = success;
        this.obj = obj;
        this.code = code;
    }

}
