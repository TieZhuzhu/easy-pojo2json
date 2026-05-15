package com.augustlee.tool.easypojo2json.test.model;

import com.augustlee.tool.easypojo2json.test.MyTestCase;

import java.util.Arrays;

import static junit.framework.Assert.assertTrue;

/**
 * 测试断言模型基类。
 */
public class TestModel {

    protected final MyTestCase testCase;

    /**
     * 创建测试模型。
     *
     * @param testCase 测试基类
     */
    public TestModel(MyTestCase testCase) {
        this.testCase = testCase;
    }

    /**
     * 断言两个对象数组内容相等。
     *
     * @param expected 期望值
     * @param actual   实际值
     */
    public static void assertArrayEquals(Object[] expected, Object[] actual) {
        assertTrue(Arrays.equals(expected, actual));
    }
}
