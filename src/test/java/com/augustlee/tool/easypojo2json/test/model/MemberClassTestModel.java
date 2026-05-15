package com.augustlee.tool.easypojo2json.test.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.intellij.openapi.actionSystem.AnAction;
import com.augustlee.tool.easypojo2json.test.MyTestCase;

import static junit.framework.Assert.assertTrue;

/**
 * 成员内部类相关能力的断言模型。
 * 
 * @author August Lee
 * @see MemberClassTestModel
 * @since 2026-05-15 15:29:08
 *
 */
public class MemberClassTestModel extends TestModel {

    /**
     * 创建成员内部类断言模型。
     *
     * @param testCase 测试基类
     */
    public MemberClassTestModel(MyTestCase testCase) {
        super(testCase);
    }

    /**
     * 验证外部类和内部类都能单独被正确转换。
     *
     * @param fileName 测试文件名
     * @param action   执行动作
     */
    public void testMemberClassTestPOJO(String fileName, AnAction action) {
        JsonNode result = testCase.testAction(fileName, action, "class");
        assertTrue(result.size() == 1 && result.get("test") != null);
        result = testCase.testAction(fileName, action, "A");
        assertTrue(result.size() == 1 && result.get("a") != null);
        result = testCase.testAction(fileName, action, "B");
        assertTrue(result.size() == 1 && result.get("b") != null);
    }
}
