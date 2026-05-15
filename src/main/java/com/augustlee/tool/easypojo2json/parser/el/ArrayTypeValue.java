package com.augustlee.tool.easypojo2json.parser.el;

import java.util.List;

public class ArrayTypeValue implements PresetTypeValue {

    @Override
    public Object getValue() {
        return List.of();
    }
}
