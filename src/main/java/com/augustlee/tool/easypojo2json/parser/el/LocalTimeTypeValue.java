package com.augustlee.tool.easypojo2json.parser.el;

import java.time.format.DateTimeFormatter;

/**
 * 本地时间默认值提供器。
 * 
 * @author August Lee
 * @see LocalTimeTypeValue
 * @since 2026-05-15 15:29:08
 *
 */
public class LocalTimeTypeValue extends LocalDateTimeTypeValue {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * 获取随机时间字符串。
     *
     * @return 随机时间字符串
     */
    @Override
    public Object getRandomValue() {
        return this.getRandomValue(formatter);
    }

    /**
     * 获取当前时间字符串。
     *
     * @return 当前时间字符串
     */
    @Override
    public Object getValue() {
        return this.getValue(formatter);
    }
}
