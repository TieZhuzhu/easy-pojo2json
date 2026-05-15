package com.augustlee.tool.easypojo2json.parser.el;

/**
 * 整数默认值提供器。
 */
public class IntegerTypeValue implements RandomTypeValue {

    /**
     * 获取默认范围内的随机整数。
     *
     * @return 随机整数
     */
    @Override
    public Object getRandomValue() {
        return this.getRandomValue(0, 100);
    }

    /**
     * 获取稳定整数默认值。
     *
     * @return {@code 0}
     */
    @Override
    public Object getValue() {
        return 0;
    }

    /**
     * 获取指定范围内的随机整数。
     *
     * @param origin 下界
     * @param bound  上界
     * @return 随机整数
     */
    public Object getRandomValue(int origin, int bound) {
        return random.nextInt(origin, bound);
    }
}
