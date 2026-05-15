package com.augustlee.tool.easypojo2json.test.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.intellij.openapi.actionSystem.AnAction;
import com.augustlee.tool.easypojo2json.test.MyTestCase;

import static junit.framework.Assert.*;

/**
 * 注解相关能力的断言模型。
 */
public class AnnotationTestModel extends TestModel {

    /**
     * 创建注解断言模型。
     *
     * @param testCase 测试基类
     */
    public AnnotationTestModel(MyTestCase testCase) {
        super(testCase);
    }

    /**
     * 验证字段重命名注解。
     *
     * @param fileName 测试文件名
     * @param action   执行动作
     */
    public void testJsonPropertyTestPOJO(String fileName, AnAction action) {
        JsonNode result = testCase.testAction(fileName, action);

        assertTrue(result.get("name").isTextual());
        assertTrue(result.get("pass").isTextual());
        assertTrue(result.get("userId").isTextual());
    }

    /**
     * 验证字段忽略注解。
     *
     * @param fileName 测试文件名
     * @param action   执行动作
     */
    public void testJsonIgnoreTestPOJO(String fileName, AnAction action) {
        JsonNode result = testCase.testAction(fileName, action);

        assertNull(result.get("username"));
        assertNotNull(result.get("password"));
    }

    /**
     * 验证忽略子属性注解。
     *
     * @param fileName 测试文件名
     * @param action   执行动作
     */
    public void testJsonIgnorePropertiesTestPOJO(String fileName, AnAction action) {
        JsonNode result = testCase.testAction(fileName, action);

        assertNotNull(result.get("roles").get(0).get("roleName"));
        assertNull(result.get("roles").get(0).get("users"));
    }

    /**
     * 验证忽略整个类型注解。
     *
     * @param fileName 测试文件名
     * @param action   执行动作
     */
    public void testJsonIgnoreTypeTestPOJO(String fileName, AnAction action) {
        JsonNode result = testCase.testAction(fileName, action);

        assertTrue(result.get("roles").isEmpty());
    }

    /**
     * 验证不同命名策略生成的字段名。
     *
     * @param fileName 测试文件名
     * @param action   执行动作
     */
    public void testJsonNamingTestPOJO(String fileName, AnAction action) {
        JsonNode result = testCase.testAction(fileName, action, "LowerCamelCaseStrategyTestPOJO");
        assertNotNull(result.get("firstName"));
        result = testCase.testAction(fileName, action, "UpperCamelCaseStrategyTestPOJO");
        assertNotNull(result.get("FirstName"));
        result = testCase.testAction(fileName, action, "SnakeCaseStrategyTestPOJO");
        assertNotNull(result.get("first_name"));
        result = testCase.testAction(fileName, action, "UpperSnakeCaseStrategyTestPOJO");
        assertNotNull(result.get("FIRST_NAME"));
        result = testCase.testAction(fileName, action, "KebabCaseStrategyTestPOJO");
        assertNotNull(result.get("first-name"));
        result = testCase.testAction(fileName, action, "LowerCaseStrategyTestPOJO");
        assertNotNull(result.get("firstname"));
        result = testCase.testAction(fileName, action, "LowerDotCaseStrategyTestPOJO");
        assertNotNull(result.get("first.name"));
    }
}
