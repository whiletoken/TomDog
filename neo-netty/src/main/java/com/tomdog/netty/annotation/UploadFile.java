package com.tomdog.netty.annotation;

import java.lang.annotation.*;

/**
 * 上传文件注解
 *
 * @author Leo
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UploadFile {

}
