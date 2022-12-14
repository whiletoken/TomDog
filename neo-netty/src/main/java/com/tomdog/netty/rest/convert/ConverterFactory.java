package com.tomdog.netty.rest.convert;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 转换器工厂类
 *
 * @author Leo
 */
public final class ConverterFactory {

    private static final Map<Class<?>, Converter<?>> map = new ConcurrentHashMap<>();

    /**
     * 创建转换器
     */
    public static Converter<?> create(Class<?> clazz) {
        Converter<?> converter = map.get(clazz);
        if (converter != null) {
            return converter;
        }
        if (clazz.equals(String.class)) {
            converter = new StringConverter();
        }
        if (clazz.equals(int.class) || clazz.equals(Integer.class)) {
            converter = new IntegerConverter();
        }
        if (clazz.equals(long.class) || clazz.equals(Long.class)) {
            converter = new LongConverter();
        }
        if (clazz.equals(float.class) || clazz.equals(Float.class)) {
            converter = new FloatConverter();
        }
        if (clazz.equals(double.class) || clazz.equals(Double.class)) {
            converter = new DoubleConverter();
        }
        if (clazz.equals(Date.class)) {
            converter = new DateConverter();
        }
        if (converter == null) {
            return null;
        }
        map.put(clazz, converter);
        return converter;
    }

}
