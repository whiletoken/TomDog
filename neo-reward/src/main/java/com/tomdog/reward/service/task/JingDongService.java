package com.tomdog.reward.service.task;

import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.spring.ioc.Bean;
import com.spring.ioc.Injector;
import com.tomdog.netty.util.JacksonUtils;
import com.tomdog.reward.constant.Constant;
import com.tomdog.reward.dto.JDResponse;
import com.tomdog.reward.dto.NeoMapDto;
import com.tomdog.reward.service.NeoMapService;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 京东签到
 */
@Slf4j
@Bean(isAop = true, proxyClassName = "com.tomdog.reward.hystrix.HystrixProxyFactory")
public class JingDongService extends AbstractTaskExecutor {

    private final NeoMapService neoMapService;

    public JingDongService() {
        Injector injector = Injector.getInstance();
        this.neoMapService = injector.getBean(NeoMapService.class);
    }

    /**
     * 京东京豆
     */
    private void JingDongBean(NeoMapDto neoMapDto) {
        Map<String, String> map = new HashMap<>();
        map.put("Cookie", neoMapDto.getValue());
        HttpRequest httpRequest = HttpUtil.createGet(Constant.JingDongConstant.JingDongBeanUrl)
                .headerMap(map, true);
        try (HttpResponse httpResponse = httpRequest.timeout(1000 * 60).execute()) {
            if (httpResponse.isOk()) {
                log.info("req succeed, response is {}", UnicodeUtil.toString(httpResponse.body()));
                JDResponse obj2 = JacksonUtils.jsonToBean(UnicodeUtil.toString(httpResponse.body()), JDResponse.class);
                String msg;
                if (obj2 != null && obj2.getCode().equals("0")) {
                    msg = "京东签到成功";
                } else {
                    msg = "账户需要重新登陆";
                }
                log.info(msg);
                HttpUtil.get(Constant.PushConstant.URL + msg);
            } else {
                HttpUtil.get(Constant.PushConstant.URL + "京东签到请求失败");
                log.info("req failed, response is {}", UnicodeUtil.toString(httpResponse.toString()));
            }
        }
    }

    @Override
    public void job() {
        Optional<List<NeoMapDto>> neoMapDtoList = neoMapService.listByName("jingdong");
        if (neoMapDtoList.isEmpty()) {
            throw new RuntimeException("data not found");
        }
        neoMapDtoList.get().forEach(this::JingDongBean);
    }

}
