package com.augustlee.tool.easypojo2json.test;

import com.augustlee.tool.easypojo2json.EditorPopupMenuAction;

/**
 * Kotlin 测试入口集合。
 */
public class KotlinTestCase extends MyTestCase {

    /**
     * 获取 Kotlin 测试数据目录。
     *
     * @return Kotlin 测试数据目录
     */
    @Override
    protected String getTestDataPath() {
        return "src/test/java/testdata/kotlin";
    }

    /**
     * 验证 Kotlin 变量、参数与局部变量场景。
     */
    public void testKotlinVariable() {
        variableTestModel.testVariableTestPOJO("VariableTestPOJO.kt", new EditorPopupMenuAction());
    }

    /**
     * 验证 Kotlin 基础类型、数组、集合、泛型与特殊对象场景。
     */
    public void testKotlinDateType() {
        dataTypeTestModel.testPrimitiveTestPOJO("PrimitiveTestPOJO.kt", new EditorPopupMenuAction());
        dataTypeTestModel.testPrimitiveArrayTestPOJO("PrimitiveArrayTestPOJO.kt", new EditorPopupMenuAction());
        dataTypeTestModel.testEnumTestPOJO("EnumTestPOJO.kt", new EditorPopupMenuAction());
        dataTypeTestModel.testIterableTestPOJO("IterableTestPOJO.kt", new EditorPopupMenuAction());
        dataTypeTestModel.testGenericTestPOJO("GenericTestPOJO.kt", new EditorPopupMenuAction());
        dataTypeTestModel.testSpecialObjectTestPOJO("SpecialObjectTestPOJO.kt", new EditorPopupMenuAction());
    }

    /**
     * 验证 Kotlin 注解驱动的字段名和忽略规则。
     */
    public void testKotlinAnnotation() {
        annotationTestModel.testJsonPropertyTestPOJO("JsonPropertyTestPOJO.kt", new EditorPopupMenuAction());
        annotationTestModel.testJsonIgnoreTestPOJO("JsonIgnoreTestPOJO.kt", new EditorPopupMenuAction());
        annotationTestModel.testJsonIgnorePropertiesTestPOJO("JsonIgnorePropertiesTestPOJO.kt", new EditorPopupMenuAction());
        annotationTestModel.testJsonIgnoreTypeTestPOJO("JsonIgnoreTypeTestPOJO.kt", new EditorPopupMenuAction());
    }

//    public void testKotlinDoc() {
//        docTestModel.testJsonIgnoreDocTestPOJO("JsonIgnoreDocTestPOJO.kt", new POJO2JsonDefaultAction());
//        docTestModel.testJsonIgnorePropertiesDocTestPOJO("JsonIgnorePropertiesDocTestPOJO.kt", new POJO2JsonDefaultAction());
//    }

    /**
     * 验证 Kotlin 静态字段过滤逻辑。
     */
    public void testKotlinStaticField() {
        staticFieldTestModel.testStaticFieldPOJO("StaticFieldPOJO.kt", new EditorPopupMenuAction());
    }

    /**
     * 验证 Kotlin 成员内部类转换逻辑。
     */
    public void testKotlinMemberClass() {
        memberClassTestModel.testMemberClassTestPOJO("MemberClassTestPOJO.kt", new EditorPopupMenuAction());
    }
}
