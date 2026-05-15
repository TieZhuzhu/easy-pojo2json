package com.augustlee.tool.easypojo2json.test.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.intellij.openapi.actionSystem.AnAction;
import com.augustlee.tool.easypojo2json.test.MyTestCase;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 * JavaDoc 标记相关能力的断言模型。
 * 
 * @author August Lee
 * @see DocTestModel
 * @since 2026-05-15 15:29:08
 *
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

    /**
     * 验证“复制 JSON + JavaDoc”文本中会把字段说明写到属性上方。
     *
     * @param fileName 测试文件名
     * @param action   执行动作
     */
    public void testJsonWithComment(String fileName, AnAction action) {
        String result = testCase.testActionRawText(fileName, action);

        assertTrue(result.contains("// 用户名称"));
        assertTrue(result.contains("\"username\": \"\""));
        assertTrue(result.indexOf("用户名称") < result.indexOf("\"username\": \"\""));

        assertTrue(result.contains("// 角色列表"));
        assertTrue(result.contains("\"roles\": ["));
        assertTrue(result.indexOf("角色列表") < result.indexOf("\"roles\": ["));

        assertTrue(result.contains("// 角色编码"));
        assertTrue(result.contains("\"roleId\": 0"));
        assertTrue(result.indexOf("角色编码") < result.indexOf("\"roleId\": 0"));
        assertTrue(!result.contains("/**"));
    }
}
