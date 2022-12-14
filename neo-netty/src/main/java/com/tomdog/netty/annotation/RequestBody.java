package com.tomdog.netty.annotation;

import java.lang.annotation.*;

/**
 * Request 请求体注解
 *
 * @author Leo
 
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestBody {

    String value() default "";

}
