package com.tomdog.reward.service.task;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.spring.ioc.Bean;
import com.spring.ioc.Injector;
import com.tomdog.netty.util.JacksonUtils;
import com.tomdog.reward.constant.Constant;
import com.tomdog.reward.dto.GenShinHeader;
import com.tomdog.reward.dto.GenShinResult;
import com.tomdog.reward.dto.ReqInfo;
import com.tomdog.reward.service.ReqService;
import com.tomdog.reward.util.GenShinUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Bean(isAop = true, proxyClassName = "com.tomdog.reward.hystrix.HystrixProxyFactory")
public class GenShinSignService extends AbstractTaskExecutor {

    private final ReqService reqService;

    public GenShinSignService() {
        Injector injector = Injector.getInstance();
        this.reqService = injector.getBean(ReqService.class);
    }

    @Override
    public void job() {

        Optional<ReqInfo> reqInfo1 = reqService.selectByName(Constant.GenShinConstant.myAccount);
        ysSign(getYsHeaders(reqInfo1.get().getHeader()), reqInfo1.get());

        Optional<ReqInfo> reqInfo2 = reqService.selectByName(Constant.GenShinConstant.linaAccount);
        ysSign(getYsHeaders(reqInfo2.get().getHeader()), reqInfo2.get());
    }

    private void ysSign(Map<String, String> headers, ReqInfo reqInfo) {

        HttpRequest httpRequest = HttpUtil.createPost(Constant.GenShinConstant.BBS_SIGN_REWARD)
                .body(reqInfo.getBody()).headerMap(headers, true)
                .contentType(Constant.contentType);
        try (HttpResponse response = httpRequest.timeout(1000 * 60).execute()) {
            if (response.isOk()) {
                String body = UnicodeUtil.toString(response.body());
                log.info("req succeed, response is {}", body);
                GenShinResult obj2 = JacksonUtils.jsonToBean(body, GenShinResult.class);
                String msg;
                if (obj2.getRetcode() != 0 || obj2.getData().getRiskCode() != 0) {
                    msg = "原神签到失败 " + reqInfo.getName();
                    log.error(msg);
                } else {
                    msg = "原神签到成功 " + reqInfo.getName();
                    log.info(msg);
                }
                HttpUtil.get(Constant.PushConstant.URL + msg);
            } else {
                HttpUtil.get(Constant.PushConstant.URL + "原神签到请求失败");
                log.info("req failed, response is {}", UnicodeUtil.toString(response.toString()));
            }
        }
    }

    private String getDS() {
        String t = Integer.toString((int) (LocalDateTimeUtil.toEpochMilli(LocalDateTime.now()) / 1000));
        String r = GenShinUtil.getRandomFromArray(null, 6);
        String c = SecureUtil.md5().digestHex(("salt=9nQiU3AV0rJSIBWgdynfoGMGKaklfbM7" + "&t=" + t + "&r=" + r).getBytes());
        return t + "," + r + "," + c;
    }

    private Map<String, String> getYsHeaders(String headerStr) {
        GenShinHeader genShinHeader = JacksonUtils.jsonToBean(headerStr, GenShinHeader.class);
        Map<String, String> headers = new HashMap<>();
        headers.put("DS", getDS());
        headers.put("x-rpc-app_version", genShinHeader.getXRpcAppVersion());
        headers.put("x-rpc-client_type", genShinHeader.getXRpcClientType());
        headers.put("x-rpc-device_id", genShinHeader.getXRpcDeviceId());
        headers.put("UserAgent", genShinHeader.getUserAgent());
        headers.put("Cookie", genShinHeader.getCookie());
        return headers;
    }
}
