package com.augustlee.tool.easypojo2json.parser.el;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 小数值默认值提供器。
 */
public class DecimalTypeValue implements RandomTypeValue {

    /**
     * 获取默认精度的随机小数。
     *
     * @return 随机小数
     */
    @Override
    public Object getRandomValue() {
        return this.getRandomValue(0, 100, 2);
    }

    /**
     * 获取默认精度的稳定小数。
     *
     * @return 固定值 {@code 0.00}
     */
    @Override
    public Object getValue() {
        return this.getValue(2);
    }

    /**
     * 获取指定精度的随机小数。
     *
     * @param scale 小数位数
     * @return 随机小数
     */
    public Object getRandomValue(int scale) {
        return this.getRandomValue(0, 100, scale);
    }

    /**
     * 获取指定范围与精度的随机小数。
     *
     * @param origin 下界
     * @param bound  上界
     * @param scale  小数位数
     * @return 随机小数
     */
    public Object getRandomValue(double origin, double bound, int scale) {
        return BigDecimal.valueOf(random.nextDouble(origin, bound)).setScale(scale, RoundingMode.DOWN);
    }

    /**
     * 获取指定精度的稳定小数。
     *
     * @param scale 小数位数
     * @return 固定小数值
     */
    public Object getValue(int scale) {
        return BigDecimal.ZERO.setScale(scale, RoundingMode.UNNECESSARY);
    }
}
