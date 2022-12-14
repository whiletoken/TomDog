package com.tomdog.netty.annotation;

import java.lang.annotation.*;

/**
 * Http 请求头注解
 *
 * @author Leo
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestHeader {

    String value() default "";

    boolean required() default true;

}
