package com.tomdog.reward.service.task;

import bitbucket.neo.util.JacksonUtils;
import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.spring.ioc.Bean;
import com.spring.ioc.Injector;
import com.tomdog.reward.constant.Constant;
import com.tomdog.reward.dto.NeoMapDto;
import com.tomdog.reward.service.NeoMapService;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.tomdog.reward.constant.Constant.PaoLuYunConstant.userInfo;

@Slf4j
@Bean(isAop = true, proxyClassName = "com.tomdog.reward.hystrix.HystrixProxyFactory")
public class PaoLuYunService extends AbstractTaskExecutor {

    private final NeoMapService neoMapService;

    public PaoLuYunService() {
        Injector injector = Injector.getInstance();
        this.neoMapService = injector.getBean(NeoMapService.class);
    }

    @Override
    public void job() {
        login();
    }

    /**
     * ✈️ 自动签到
     */
    private void login() {

        Optional<NeoMapDto> optional = neoMapService.selectByName(userInfo);
        if (optional.isEmpty()) {
            log.error("config not existed");
            return;
        }
        Map<String, Object> map = JacksonUtils.jsonToBean(optional.get().getValue(), HashMap.class);
        HttpRequest httpRequest = HttpUtil.createPost(Constant.PaoLuYunConstant.loginUrl)
                .form(map);
        try (HttpResponse httpResponse = httpRequest.timeout(1000 * 60).execute()) {
            if (httpResponse.isOk()) {
                log.info("req succeed, response is {}", UnicodeUtil.toString(httpResponse.body()));
                Map<String, List<String>> headers = httpResponse.headers();
                List<String> cookies = headers.get("set-cookie");
                StringBuilder stringBuilder = new StringBuilder(1024);
                for (String cookie : cookies) {
                    String cookieStr = cookie.substring(0, cookie.indexOf(";"));
                    stringBuilder.append(cookieStr).append(";");
                }
                stringBuilder.substring(0, stringBuilder.lastIndexOf(";"));
                checkIn(stringBuilder.toString());
            } else {
                log.info("req failed, response is {}", UnicodeUtil.toString(httpResponse.toString()));
            }
        }
    }

    private void checkIn(String cookie) {

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Cookie", cookie);

        try (HttpResponse httpResponse = HttpUtil.createPost(Constant.PaoLuYunConstant.url)
                .timeout(1000 * 60).headerMap(headers, true).execute()) {
            if (httpResponse.isOk()) {
                log.info("req succeed, response is {}", UnicodeUtil.toString(httpResponse.body()));
            } else {
                log.info("req failed, response is {}", UnicodeUtil.toString(httpResponse.toString()));
            }
        }
    }

}
