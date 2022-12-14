package com.tomdog.netty.rest.convert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 日期转换器
 *
 * @author Leo
 */
final class DateConverter implements Converter<LocalDateTime> {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 类型转换
     */
    @Override
    public LocalDateTime convert(Object source) {
        return LocalDateTime.parse(source.toString(), dateTimeFormatter);
    }

}
