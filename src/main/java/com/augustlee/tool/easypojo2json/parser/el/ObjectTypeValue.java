package com.augustlee.tool.easypojo2json.parser.el;

import java.util.Map;

public class ObjectTypeValue implements PresetTypeValue {
    @Override
    public Object getValue() {
        return Map.of();
    }
}
