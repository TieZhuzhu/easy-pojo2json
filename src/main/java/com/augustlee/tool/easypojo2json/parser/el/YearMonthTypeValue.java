package com.augustlee.tool.easypojo2json.parser.el;

import java.time.format.DateTimeFormatter;

/**
 * 年月默认值提供器。
 * 
 * @author August Lee
 * @see YearMonthTypeValue
 * @since 2026-05-15 15:29:08
 *
 */
public class YearMonthTypeValue extends LocalDateTimeTypeValue {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

    /**
     * 获取随机年月字符串。
     *
     * @return 随机年月字符串
     */
    @Override
    public Object getRandomValue() {
        return this.getRandomValue(formatter);
    }

    /**
     * 获取当前年月字符串。
     *
     * @return 当前年月字符串
     */
    @Override
    public Object getValue() {
        return this.getValue(formatter);
    }
}
