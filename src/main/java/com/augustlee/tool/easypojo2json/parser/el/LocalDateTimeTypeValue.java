package com.augustlee.tool.easypojo2json.parser.el;

import java.time.format.DateTimeFormatter;

/**
 * 本地日期时间默认值提供器。
 * 
 * @author August Lee
 * @see LocalDateTimeTypeValue
 * @since 2026-05-15 15:29:08
 *
 */
public class LocalDateTimeTypeValue extends ZonedDateTimeTypeValue {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取随机本地日期时间字符串。
     *
     * @return 随机本地日期时间字符串
     */
    @Override
    public Object getRandomValue() {
        return this.getRandomValue(formatter);
    }

    /**
     * 获取当前本地日期时间字符串。
     *
     * @return 当前本地日期时间字符串
     */
    @Override
    public Object getValue() {
        return this.getValue(formatter);
    }
}
