package com.tomdog.reward.hystrix;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface HystrixCommand {

    /**
     * 默认超时时间
     *
     * @return int
     */
    int timeout() default 1000;

    /**
     * 回退方法
     *
     * @return string
     */
    String fallback() default "";

}
