package com.augustlee.tool.easypojo2json.parser.el;

/**
 * 类型默认值提供器。
 * 
 * @author August Lee
 * @see TypeValue
 * @since 2026-05-15 15:29:08
 *
 */
public interface TypeValue {

    /**
     * 获取默认值。
     *
     * @return 默认值
     */
    Object getValue();
}
