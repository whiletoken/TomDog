package com.tomdog.netty.annotation;

import java.lang.annotation.*;

/**
 * 路径变量注解
 *
 * @author Leo
 
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PathVariable {

    String value() default "";

}
