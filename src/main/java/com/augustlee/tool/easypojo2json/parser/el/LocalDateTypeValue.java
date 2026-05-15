package com.augustlee.tool.easypojo2json.parser.el;

import java.time.format.DateTimeFormatter;

public class LocalDateTypeValue extends LocalDateTimeTypeValue {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public Object getRandomValue() {
        return this.getRandomValue(formatter);
    }

    @Override
    public Object getValue() {
        return this.getValue(formatter);
    }
}
