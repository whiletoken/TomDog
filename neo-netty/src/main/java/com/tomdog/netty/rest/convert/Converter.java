package com.tomdog.netty.rest.convert;

/**
 * 数据转换器接口
 *
 * @author Leo
 */
public interface Converter<T> {

    /**
     * 类型转换
     */
    T convert(Object source);

}
