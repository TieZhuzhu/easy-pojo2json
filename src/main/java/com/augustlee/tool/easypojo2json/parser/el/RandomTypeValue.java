package com.augustlee.tool.easypojo2json.parser.el;

import java.util.Random;

/**
 * 支持随机示例值的类型提供器。
 * 
 * @author August Lee
 * @see RandomTypeValue
 * @since 2026-05-15 15:29:08
 *
 */
public interface RandomTypeValue extends PresetTypeValue {

    /**
     * 随机数生成器。
     */
    Random random = new Random();

    /**
     * 获取随机值。
     *
     * @return 随机值
     */
    Object getRandomValue();
}
