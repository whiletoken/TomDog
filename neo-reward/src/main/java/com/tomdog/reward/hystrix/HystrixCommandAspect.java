package com.tomdog.reward.hystrix;

import cn.hutool.aop.aspects.Aspect;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class HystrixCommandAspect implements Aspect {

    public static HystrixCommandAspect getInstance() {
        return new HystrixCommandAspect();
    }

    @Override
    public boolean before(Object target, Method method, Object[] args) {
        return true;
    }

    @Override
    public boolean after(Object target, Method method, Object[] args, Object returnVal) {
        return true;
    }

    @Override
    public boolean afterException(Object target, Method method, Object[] args, Throwable e) {
        log.info("执行回调");
        HystrixCommand hystrixCommand = method.getAnnotation(HystrixCommand.class);
        if (StrUtil.isBlank(hystrixCommand.fallback())) {
            return true;
        } else {
            try {
                fallBack(target, method, hystrixCommand, args);
                return false;
            } catch (NoSuchMethodException | InvocationTargetException
                     | IllegalAccessException exception) {
                log.error("fallBack exception is", exception);
            }
        }
        return true;
    }

    private Object fallBack(Object target, Method method, HystrixCommand hystrixCommand, Object[] args)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Method fallbackMethod;
        Class<?> objClazz = target.getClass();
        fallbackMethod = objClazz.getMethod(hystrixCommand.fallback(), parameterTypes);
        fallbackMethod.setAccessible(true);
        return fallbackMethod.invoke(target, args);
    }
}