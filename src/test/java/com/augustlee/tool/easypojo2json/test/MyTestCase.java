package com.augustlee.tool.easypojo2json.test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.LanguageLevelProjectExtension;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.pom.java.LanguageLevel;
import com.intellij.psi.PsiElement;
import com.intellij.testFramework.LightProjectDescriptor;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import com.intellij.testFramework.fixtures.MavenDependencyUtil;
import com.augustlee.tool.easypojo2json.test.model.AnnotationTestModel;
import com.augustlee.tool.easypojo2json.test.model.DataTypeTestModel;
import com.augustlee.tool.easypojo2json.test.model.DocTestModel;
import com.augustlee.tool.easypojo2json.test.model.MemberClassTestModel;
import com.augustlee.tool.easypojo2json.test.model.StaticFieldTestModel;
import com.augustlee.tool.easypojo2json.test.model.VariableTestModel;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * 插件测试基类。
 * 
 * @author August Lee
 * @see MyTestCase
 * @since 2026-05-15 15:29:08
 *
 */
public abstract class MyTestCase extends LightJavaCodeInsightFixtureTestCase {

    public static final LanguageLevel LANGUAGE_LEVEL = LanguageLevel.JDK_11;

    /**
     * 测试专用轻量项目描述。
     * <p>
     * 这里通过 Maven 动态补入 Jackson/Fastjson 依赖，让测试样例中的注解和类型能够正常解析。
     */
    public static final LightProjectDescriptor MOCK_JDK = new ProjectDescriptor(LANGUAGE_LEVEL) {

        /**
         * 为测试模块补充外部依赖。
         *
         * @param module       当前模块
         * @param model        可修改的根模型
         * @param contentEntry 内容根
         */
        @Override
        public void configureModule(@NotNull Module module, @NotNull ModifiableRootModel model, @NotNull ContentEntry contentEntry) {
            MavenDependencyUtil.addFromMaven(model, "com.alibaba:fastjson:1.2.83");
            MavenDependencyUtil.addFromMaven(model, "com.fasterxml.jackson.core:jackson-databind:2.14.3");
            super.configureModule(module, model, contentEntry);
        }
    };

    protected final ObjectMapper objectMapper = new ObjectMapper();

    protected final DataTypeTestModel dataTypeTestModel = new DataTypeTestModel(this);

    protected final AnnotationTestModel annotationTestModel = new AnnotationTestModel(this);

    protected final DocTestModel docTestModel = new DocTestModel(this);

    protected final StaticFieldTestModel staticFieldTestModel = new StaticFieldTestModel(this);

    protected final MemberClassTestModel memberClassTestModel = new MemberClassTestModel(this);

    protected final VariableTestModel variableTestModel = new VariableTestModel(this);

    /**
     * 初始化 ObjectMapper，使断言中的 BigDecimal 与文本格式更加稳定。
     */
    public MyTestCase() {
        // 让数值断言保持精度一致，避免 Jackson 在测试中把小数自动转成 double。
        this.objectMapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
        this.objectMapper.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
        this.objectMapper.setNodeFactory(JsonNodeFactory.withExactBigDecimals(true));
    }

    /**
     * 初始化测试工程语言级别。
     *
     * @throws Exception 初始化异常
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        LanguageLevelProjectExtension.getInstance(getProject()).setLanguageLevel(LANGUAGE_LEVEL);

        System.out.println(MOCK_JDK.getSdk().getHomePath());
        System.out.println(MOCK_JDK.getSdk().getHomeDirectory());
        System.out.println(MOCK_JDK.getSdk().getVersionString());
    }

    /**
     * 获取测试数据目录。
     *
     * @return 测试数据目录
     */
    @Override
    protected abstract String getTestDataPath();

    /**
     * 指定测试项目描述。
     *
     * @return 轻量项目描述
     */
    @Override
    protected @NotNull LightProjectDescriptor getProjectDescriptor() {
        return MOCK_JDK;
    }

    /**
     * 在默认类位置触发动作。
     *
     * @param fileName 测试文件名
     * @param action   要执行的动作
     * @return 解析后的 JSON 结果
     */
    public JsonNode testAction(@NotNull String fileName, @NotNull AnAction action) {
        return this.testAction(fileName, action, "class");
    }

    /**
     * 在指定光标定位文本处触发动作，并从剪贴板读取 JSON 结果。
     *
     * @param fileName             测试文件名
     * @param action               要执行的动作
     * @param cursorPositionByText 用于定位光标的文本片段
     * @return 解析后的 JSON 结果
     */
    public JsonNode testAction(@NotNull String fileName, @NotNull AnAction action, String cursorPositionByText) {
        // 打开文件并模拟用户把光标放到目标类、字段或参数所在位置。
        myFixture.configureByFile(fileName);
        PsiElement psiElement = myFixture.findElementByText(cursorPositionByText, PsiElement.class);
        int offset = psiElement.getTextOffset();
        myFixture.getEditor().getCaretModel().moveToOffset(offset);

        myFixture.testAction(action);

        Transferable result = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);

        try {
            String jsonStr = String.valueOf(result.getTransferData(DataFlavor.stringFlavor));
            JsonNode jsonNode = objectMapper.readTree(jsonStr);
            System.out.println(jsonNode.toPrettyString());
            return jsonNode;
        } catch (UnsupportedFlavorException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
