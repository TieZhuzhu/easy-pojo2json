package com.augustlee.tool.easypojo2json.parser.el;

import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 带时区日期时间默认值提供器。
 * 
 * @author August Lee
 * @see ZonedDateTimeTypeValue
 * @since 2026-05-15 15:29:08
 *
 */
public class ZonedDateTimeTypeValue extends TemporalTypeValue {
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    /**
     * 获取随机的 ISO_OFFSET_DATE_TIME 字符串。
     *
     * @return 随机日期时间字符串
     */
    @Override
    public Object getRandomValue() {
        return this.getRandomValue(formatter);
    }

    /**
     * 获取当前时间的 ISO_OFFSET_DATE_TIME 字符串。
     *
     * @return 当前日期时间字符串
     */
    @Override
    public Object getValue() {
        return this.getValue(formatter);
    }

    /**
     * 按指定格式生成随机时间字符串。
     *
     * @param format 日期格式
     * @return 随机时间字符串
     */
    public Object getRandomValue(String format) {
        if (StringUtils.isNotBlank(format)) {
            return this.getRandomValue(DateTimeFormatter.ofPattern(format));
        } else {
            return this.getRandomValue();
        }
    }

    /**
     * 按指定格式生成当前时间字符串。
     *
     * @param format 日期格式
     * @return 当前时间字符串
     */
    public Object getValue(String format) {
        if (StringUtils.isNotBlank(format)) {
            return this.getValue(DateTimeFormatter.ofPattern(format));
        } else {
            return this.getValue();
        }
    }

    /**
     * 使用格式化器生成随机时间字符串。
     *
     * @param formatter 日期格式化器
     * @return 随机时间字符串
     */
    public Object getRandomValue(DateTimeFormatter formatter) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli((long) super.getRandomValue()), ZoneId.systemDefault()).format(formatter);
    }

    /**
     * 使用格式化器生成当前时间字符串。
     *
     * @param formatter 日期格式化器
     * @return 当前时间字符串
     */
    public Object getValue(DateTimeFormatter formatter) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli((long) super.getValue()), ZoneId.systemDefault()).format(formatter);
    }
}
