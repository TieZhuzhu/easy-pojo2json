package com.augustlee.tool.easypojo2json.parser.el;

import java.util.UUID;

/**
 * UUID 默认值提供器。
 */
public class UUIDTypeValue implements PresetTypeValue {

    /**
     * 生成标准 UUID 字符串。
     *
     * @return UUID 字符串
     */
    @Override
    public Object getValue() {
        return UUID.randomUUID().toString();
    }
}
