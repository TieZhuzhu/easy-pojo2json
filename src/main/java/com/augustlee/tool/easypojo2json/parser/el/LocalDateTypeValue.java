package com.augustlee.tool.easypojo2json.parser.el;

import java.time.format.DateTimeFormatter;

/**
 * 本地日期默认值提供器。
 */
public class LocalDateTypeValue extends LocalDateTimeTypeValue {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 获取随机日期字符串。
     *
     * @return 随机日期字符串
     */
    @Override
    public Object getRandomValue() {
        return this.getRandomValue(formatter);
    }

    /**
     * 获取当前日期字符串。
     *
     * @return 当前日期字符串
     */
    @Override
    public Object getValue() {
        return this.getValue(formatter);
    }
}
