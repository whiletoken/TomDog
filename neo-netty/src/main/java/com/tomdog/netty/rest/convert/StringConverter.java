package com.tomdog.netty.rest.convert;

/**
 * 字符串转换器
 *
 * @author Leo
 */
final class StringConverter implements Converter<String> {

    /**
     * 类型转换
     */
    @Override
    public String convert(Object source) {
        return source.toString();
    }

}
