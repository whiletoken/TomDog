package com.tomdog.webboot.controller;

import com.tomdog.webboot.util.redisson.JsonResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "test")
public class TestController {

    @RequestMapping(value = "gateway")
    public String asyncTest() {
        return "aync";
    }

    @RequestMapping(value = "succeed")
    public JsonResult succeedTest() {
        return JsonResult.succeed("123123");
    }

}
