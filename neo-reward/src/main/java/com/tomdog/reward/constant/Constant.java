package com.tomdog.reward.constant;

import cn.hutool.core.thread.ThreadUtil;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Constant {

    public static class GenShinConstant {

        public static final String BBS_SIGN_REWARD = "https://api-takumi.mihoyo.com/event/bbs_sign_reward/sign";

        public static final String myAccount = "194319201";

        public static final String linaAccount = "154424184";

    }

    public static final String contentType = "application/json; charset=utf-8";

    public static class JingDongConstant {

        // https://plogin.m.jd.com/login/login?appid=828&returnurl=https%3A%2F%2Fbean.m.jd.com%2Fbean%2FsignIndex.action
        public static final String JingDongBeanUrl = "https://api.m.jd.com/client.action?functionId=signBeanIndex&appid=ld";//京东京豆
    }

    public static class PaoLuYunConstant {

        public static final String url = "https://paoluz.link/user/checkin";
        public static final String loginUrl = "https://paoluz.link/auth/login";
        public static final String userInfo = "paoluz";
    }

    public static class PushConstant {
        public static final String URL = "https://api.day.app/rBn2QeusxkbbaFXE7XSXMP/";
    }

    public static class DomainResolve {

        public static final String DOMAIN_RESOLVE = "domain_resolve";
        public static final String RAW_URL = "raw_url";

        public static final String HOST_NAME = "host_name";
    }

    public static final ThreadPoolExecutor executorService =
            (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(),
                    ThreadUtil.createThreadFactory("common-"));

    static {
        executorService.setKeepAliveTime(1, TimeUnit.HOURS);
        executorService.allowCoreThreadTimeOut(true);
    }


}
