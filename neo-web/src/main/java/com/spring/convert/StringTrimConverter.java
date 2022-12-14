package com.spring.convert;

import org.springframework.core.convert.converter.Converter;

import java.util.Objects;

/**
 * StringTrimConverter
 *
 * @author liujunjie
 */
public class StringTrimConverter implements Converter<String, String> {

    @Override
    public String convert(String source) {
        try {

            //去掉字符串两边空格，如果去除后为空设置为null
            source = source.trim();
            if (Objects.equals(source, "")) {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return source;
    }
}
