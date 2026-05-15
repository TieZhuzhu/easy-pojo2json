package com.augustlee.tool.easypojo2json.parser.el;

import java.util.UUID;

public class UUIDTypeValue implements PresetTypeValue {
    @Override
    public Object getValue() {
        return UUID.randomUUID().toString();
    }
}
