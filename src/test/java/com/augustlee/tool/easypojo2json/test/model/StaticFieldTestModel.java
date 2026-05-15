package com.augustlee.tool.easypojo2json.test.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.intellij.openapi.actionSystem.AnAction;
import com.augustlee.tool.easypojo2json.test.MyTestCase;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * 静态字段过滤能力的断言模型。
 * 
 * @author August Lee
 * @see StaticFieldTestModel
 * @since 2026-05-15 15:29:08
 *
 */
public class StaticFieldTestModel extends TestModel {

    /**
     * 创建静态字段断言模型。
     *
     * @param testCase 测试基类
     */
    public StaticFieldTestModel(MyTestCase testCase) {
        super(testCase);
    }

    /**
     * 验证实例字段会保留，静态字段会被过滤。
     *
     * @param fileName 测试文件名
     * @param action   执行动作
     */
    public void testStaticFieldPOJO(String fileName, AnAction action) {
        JsonNode result = testCase.testAction(fileName, action);

        assertNotNull(result.get("thisFinal"));
        assertNotNull(result.get("thisTransient"));
        assertNotNull(result.get("thisFinalTransient"));

        assertNull(result.get("serialVersionUID"));
        assertNull(result.get("thisStatic"));
        assertNull(result.get("thisStaticFinal"));
        assertNull(result.get("thisStaticTransient"));
        assertNull(result.get("thisStaticFinalTransient"));
    }
}
