package com.augustlee.tool.easypojo2json.test.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.intellij.openapi.actionSystem.AnAction;
import com.augustlee.tool.easypojo2json.test.MyTestCase;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.StreamSupport;

import static junit.framework.Assert.*;

/**
 * 变量、参数与局部变量相关能力的断言模型。
 * 
 * @author August Lee
 * @see VariableTestModel
 * @since 2026-05-15 15:29:08
 *
 */
public class VariableTestModel extends TestModel {

    /**
     * 创建变量断言模型。
     *
     * @param testCase 测试基类
     */
    public VariableTestModel(MyTestCase testCase) {
        super(testCase);
    }

    /**
     * 验证字段、构造参数、方法参数和局部变量都能被正确解析。
     *
     * @param fileName 测试文件名
     * @param action   执行动作
     */
    public void testVariableTestPOJO(String fileName, AnAction action) {
        JsonNode result = testCase.testAction(fileName, action, "listField");
        assertTrue(result.isArray());
        result = result.get(0);
        test(result);
        assertTrue(result.get("data").isTextual());

        result = testCase.testAction(fileName, action, "cParameter");
        assertTrue(result.isObject());
        test(result);
        assertEquals(0, result.get("data").asInt());

        result = testCase.testAction(fileName, action, "mParameter");
        assertTrue(result.isObject());
        test(result);
        assertTrue(result.get("data").isTextual());

        result = testCase.testAction(fileName, action, "localVariable");
        assertTrue(result.isObject());
        test(result);
        assertNull(result.get("data").get("username"));
        assertTrue(result.get("data").get("password").isTextual());
    }

    /**
     * 验证嵌套对象中的共性字段结构。
     *
     * @param result 解析结果
     */
    private void test(JsonNode result) {
        assertEquals(0, result.get("anInt").asInt());
        assertTrue(result.get("string").isTextual());
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.UNNECESSARY), result.get("bigDecimal").decimalValue());
        assertArrayEquals(new Integer[]{0},
                StreamSupport.stream(result.get("ints").spliterator(), false).map(JsonNode::intValue).toArray(Integer[]::new));
        assertArrayEquals(new Integer[]{0},
                StreamSupport.stream(result.get("linkedHashSet").spliterator(), false).map(JsonNode::intValue).toArray(Integer[]::new));
    }
}
