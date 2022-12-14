package com.tomdog.netty.rest.convert;

/**
 * 双精度转换器
 *
 * @author Leo
 */
final class DoubleConverter implements Converter<Double> {

    /**
     * 类型转换
     */
    @Override
    public Double convert(Object source) {
        return Double.parseDouble(source.toString());
    }

}
