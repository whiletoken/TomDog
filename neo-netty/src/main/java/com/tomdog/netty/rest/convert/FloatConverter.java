package com.tomdog.netty.rest.convert;

/**
 * 单精度转换器
 *
 * @author Leo
 */
final class FloatConverter implements Converter<Float> {

    /**
     * 类型转换
     */
    @Override
    public Float convert(Object source) {
        return Float.parseFloat(source.toString());
    }

}
