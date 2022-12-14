package com.tomdog.gateway.util;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

/**
 * 时间工具类
 */
@Slf4j
public class ClockUtil {

    /**
     * sleep
     * 捕获异常
     *
     * @param seconds 单位秒
     */
    public static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            log.error("sleep exception is ", e);
        }
    }

    /**
     * 返回时间戳，单位毫秒
     *
     * @param localDateTime localDateTime
     * @return long
     */
    public static long toEpochMilli(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.of("Asia/Shanghai")).toInstant().toEpochMilli();
    }

    /**
     * 计算时间差，和当前时间比较
     *
     * @param inst2    传入时间
     * @param timeUnit 计算单位
     * @return long
     */
    public static long toEpochTime(Instant inst2, TimeUnit timeUnit) {
        // 当前的时间
        Instant inst1 = Instant.now();
        return switch (timeUnit) {

            // 毫秒
            case MICROSECONDS -> Duration.between(inst1, inst2).toMillis();

            // 秒
            case MILLISECONDS -> Duration.between(inst1, inst2).toSeconds();

            // 默认毫秒
            default -> Duration.between(inst1, inst2).toMillis();
        };
    }

}
