package com.augustlee.tool.easypojo2json.parser.el;


public class BooleanTypeValue implements RandomTypeValue {

    @Override
    public Object getRandomValue() {
        return random.nextBoolean();
    }

    @Override
    public Object getValue() {
        return false;
    }
}
