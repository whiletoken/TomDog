package com.tomdog.netty.annotation;

import java.lang.annotation.*;

/**
 * Http 请求参数注解
 *
 * @author Leo
 
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {

    String value() default "";

    boolean required() default true;

}
