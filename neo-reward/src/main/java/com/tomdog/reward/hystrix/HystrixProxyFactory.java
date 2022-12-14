package com.tomdog.reward.hystrix;

import cn.hutool.aop.ProxyUtil;
import com.spring.ioc.ProxyBean;

/**
 * JDK实现的切面代理
 */
public class HystrixProxyFactory implements ProxyBean {
    @Override
    public <T> T newProxyInstance(T target) {
        return ProxyUtil.newProxyInstance(
                target.getClass().getClassLoader(),
                new HystrixInterceptor(target, HystrixCommandAspect.getInstance()),
                target.getClass().getSuperclass().getInterfaces());
    }
}
