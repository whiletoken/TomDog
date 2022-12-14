package com.tomdog.reward.hystrix;

import cn.hutool.aop.aspects.Aspect;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import static com.tomdog.reward.constant.Constant.executorService;

/**
 * 超时熔断不是及时熔断
 * <a href="https://gist.github.com/baymaxium/8a8aeffd2ca9d7aef3a63baf2c73d840">超时熔断</a>
 */
@Slf4j
public class HystrixInterceptor implements InvocationHandler, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Object target;
    private final Aspect aspect;

    /**
     * 构造
     *
     * @param target 被代理对象
     * @param aspect 切面实现
     */
    public HystrixInterceptor(Object target, Aspect aspect) {
        this.target = target;
        this.aspect = aspect;
    }

    public Object getTarget() {
        return this.target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final Object target = this.target;
        final Aspect aspect = this.aspect;
        Object result = null;

        HystrixCommand hystrixCommand = method.getAnnotation(HystrixCommand.class);
        if (hystrixCommand == null) {
            return method.invoke(ClassUtil.isStatic(method) ? null : target, args);
        }

        // 开始前回调
        if (aspect.before(target, method, args)) {
            ReflectUtil.setAccessible(method);

            FutureTask<Object> task = new FutureTask<>(() -> {
                try {
                    return method.invoke(ClassUtil.isStatic(method) ? null : target, args);
                } catch (Throwable throwable) {
                    log.error("exception is ", throwable);
                }
                return null;
            });
            executorService.submit(task);
            try {
                result = task.get(hystrixCommand.timeout(), TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                task.cancel(true);
                // 异常回调（只捕获业务代码导致的异常，而非反射导致的异常）
                if (aspect.afterException(target, method, args, e)) {
                    throw e;
                }
            }

            // 结束执行回调
            if (aspect.after(target, method, args, result)) {
                return result;
            }
        }
        return null;
    }

}