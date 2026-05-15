package com.augustlee.tool.easypojo2json.test;

import com.augustlee.tool.easypojo2json.EditorPopupMenuAction;

/**
 * Java 测试入口集合。
 * 
 * @author August Lee
 * @see JavaTestCase
 * @since 2026-05-15 15:29:08
 *
 */
public class JavaTestCase extends MyTestCase {

    /**
     * 获取 Java 测试数据目录。
     *
     * @return Java 测试数据目录
     */
    @Override
    protected String getTestDataPath() {
        return "src/test/java/testdata/java";
    }

    /**
     * 临时手工调试入口。
     */
    public void testCurrent() {
//        dataTypeTestModel.testSpecialObjectTestPOJO("SpecialObjectTestPOJO.java", new EditorPopupMenuAction());
//        dataTypeTestModel.testGenericTestPOJO("GenericTestPOJO.java", new EditorPopupMenuAction());
        annotationTestModel.testJsonNamingTestPOJO("JsonNamingTestPOJO.java", new EditorPopupMenuAction());
    }

    /**
     * 验证变量、参数与局部变量场景。
     */
    public void testJavaVariable() {
        variableTestModel.testVariableTestPOJO("VariableTestPOJO.java", new EditorPopupMenuAction());
    }

    /**
     * 验证 Java 基础类型、数组、集合、泛型与特殊对象场景。
     */
    public void testJavaDateType() {
        dataTypeTestModel.testPrimitiveTestPOJO("PrimitiveTestPOJO.java", new EditorPopupMenuAction());
        dataTypeTestModel.testPrimitiveArrayTestPOJO("PrimitiveArrayTestPOJO.java", new EditorPopupMenuAction());
        dataTypeTestModel.testEnumTestPOJO("EnumTestPOJO.java", new EditorPopupMenuAction());
        dataTypeTestModel.testIterableTestPOJO("IterableTestPOJO.java", new EditorPopupMenuAction());
        dataTypeTestModel.testGenericTestPOJO("GenericTestPOJO.java", new EditorPopupMenuAction());
        dataTypeTestModel.testSpecialObjectTestPOJO("SpecialObjectTestPOJO.java", new EditorPopupMenuAction());
    }

    /**
     * 验证注解驱动的字段名和忽略规则。
     */
    public void testJavaAnnotation() {
        annotationTestModel.testJsonPropertyTestPOJO("JsonPropertyTestPOJO.java", new EditorPopupMenuAction());
        annotationTestModel.testJsonIgnoreTestPOJO("JsonIgnoreTestPOJO.java", new EditorPopupMenuAction());
        annotationTestModel.testJsonIgnorePropertiesTestPOJO("JsonIgnorePropertiesTestPOJO.java", new EditorPopupMenuAction());
        annotationTestModel.testJsonIgnoreTypeTestPOJO("JsonIgnoreTypeTestPOJO.java", new EditorPopupMenuAction());
        annotationTestModel.testJsonNamingTestPOJO("JsonNamingTestPOJO.java", new EditorPopupMenuAction());
    }

    /**
     * 验证 JavaDoc 标记驱动的忽略规则。
     */
    public void testJavaDoc() {
        docTestModel.testJsonIgnoreDocTestPOJO("JsonIgnoreDocTestPOJO.java", new EditorPopupMenuAction());
        docTestModel.testJsonIgnorePropertiesDocTestPOJO("JsonIgnorePropertiesDocTestPOJO.java", new EditorPopupMenuAction());
    }

    /**
     * 验证静态字段过滤逻辑。
     */
    public void testJavaStaticField() {
        staticFieldTestModel.testStaticFieldPOJO("StaticFieldPOJO.java", new EditorPopupMenuAction());
    }

    /**
     * 验证成员内部类转换逻辑。
     */
    public void testJavaMemberClass() {
        memberClassTestModel.testMemberClassTestPOJO("MemberClassTestPOJO.java", new EditorPopupMenuAction());
    }
}
