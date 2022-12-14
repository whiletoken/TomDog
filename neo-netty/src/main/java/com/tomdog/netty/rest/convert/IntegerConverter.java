package com.tomdog.netty.rest.convert;

/**
 * 整数转换器
 *
 * @author Leo
 */
final class IntegerConverter implements Converter<Integer> {

    /**
     * 类型转换
     */
    @Override
    public Integer convert(Object source) {
        return Integer.parseInt(source.toString());
    }

}
