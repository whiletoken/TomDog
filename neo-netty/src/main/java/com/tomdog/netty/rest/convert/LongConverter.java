package com.tomdog.netty.rest.convert;

/**
 * 长整数转换器
 *
 * @author Leo
 */
final class LongConverter implements Converter<Long> {

    /**
     * 类型转换
     */
    @Override
    public Long convert(Object source) {
        return Long.parseLong(source.toString());
    }

}