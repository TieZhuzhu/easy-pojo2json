package com.augustlee.tool.easypojo2json.parser.el;

import java.util.List;

/**
 * 数组或集合默认值提供器。
 */
public class ArrayTypeValue implements PresetTypeValue {

    /**
     * 返回空列表，表示数组/集合的默认示例结构。
     *
     * @return 空列表
     */
    @Override
    public Object getValue() {
        return List.of();
    }
}
