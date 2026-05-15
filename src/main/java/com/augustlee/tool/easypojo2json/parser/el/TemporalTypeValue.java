package com.augustlee.tool.easypojo2json.parser.el;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 时间戳默认值提供器。
 */
public class TemporalTypeValue implements RandomTypeValue {

    /**
     * 生成一个位于当前时间前后十年范围内的随机时间戳。
     *
     * @return 随机时间戳
     */
    @Override
    public Object getRandomValue() {
        LocalDateTime now = LocalDateTime.now();
        long begin = now.minusYears(10).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long end = now.plusYears(10).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return begin + (long) (Math.random() * (end - begin));
    }

    /**
     * 获取当前时刻时间戳。
     *
     * @return 当前毫秒时间戳
     */
    @Override
    public Object getValue() {
        return Instant.now().toEpochMilli();
    }
}
