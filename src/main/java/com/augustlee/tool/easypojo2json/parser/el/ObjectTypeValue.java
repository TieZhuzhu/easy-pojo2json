package com.augustlee.tool.easypojo2json.parser.el;

import java.util.Map;

/**
 * 对象默认值提供器。
 * 
 * @author August Lee
 * @see ObjectTypeValue
 * @since 2026-05-15 15:29:08
 *
 */
public class ObjectTypeValue implements PresetTypeValue {

    /**
     * 返回空对象结构。
     *
     * @return 空映射
     */
    @Override
    public Object getValue() {
        return Map.of();
    }
}
