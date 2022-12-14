package com.tomdog.netty.annotation;

import java.lang.annotation.*;

/**
 * REST 控制器注解
 *
 * @author Leo
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestController {

    boolean singleton() default true;

}
