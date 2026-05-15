package com.augustlee.tool.easypojo2json.test.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.intellij.openapi.actionSystem.AnAction;
import com.augustlee.tool.easypojo2json.test.MyTestCase;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * JavaDoc 标记相关能力的断言模型。
 */
public class DocTestModel extends TestModel {

    /**
     * 创建 JavaDoc 断言模型。
     *
     * @param testCase 测试基类
     */
    public DocTestModel(MyTestCase testCase) {
        super(testCase);
    }

    /**
     * 验证 JavaDoc 中的 {@code @JsonIgnoreProperties} 语义。
     *
     * @param fileName 测试文件名
     * @param action   执行动作
     */
    public void testJsonIgnorePropertiesDocTestPOJO(String fileName, AnAction action) {
        JsonNode result = testCase.testAction(fileName, action);

        assertNotNull(result.get("roles").get(0).get("roleName"));
        assertNull(result.get("roles").get(0).get("users"));
    }

    /**
     * 验证 JavaDoc 中的 {@code @JsonIgnore} 语义。
     *
     * @param fileName 测试文件名
     * @param action   执行动作
     */
    public void testJsonIgnoreDocTestPOJO(String fileName, AnAction action) {
        JsonNode result = testCase.testAction(fileName, action);

        assertNull(result.get("username"));
        assertNotNull(result.get("password"));
    }
}
