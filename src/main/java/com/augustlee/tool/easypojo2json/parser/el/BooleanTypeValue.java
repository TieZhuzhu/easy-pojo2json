package com.augustlee.tool.easypojo2json.parser.el;

/**
 * 布尔值默认值提供器。
 * 
 * @author August Lee
 * @see BooleanTypeValue
 * @since 2026-05-15 15:29:08
 *
 */
public class BooleanTypeValue implements RandomTypeValue {

    /**
     * 获取随机布尔值。
     *
     * @return 随机布尔值
     */
    @Override
    public Object getRandomValue() {
        return random.nextBoolean();
    }

    /**
     * 获取稳定布尔默认值。
     *
     * @return {@code false}
     */
    @Override
    public Object getValue() {
        return false;
    }
}
