package com.augustlee.tool.easypojo2json;

import com.intellij.lang.properties.PropertiesLanguage;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.LanguageTextField;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.UI;

import javax.swing.*;

/**
 * 插件设置页 UI 组件。
 * 
 * @author August Lee
 * @see SettingsComponent
 * @since 2026-05-15 15:29:08
 *
 */
public class SettingsComponent {

    private final JPanel panel;

    private final LanguageTextField classNameSpELEditor;

    private final EditorTextField fieldNameSpELEditor;

    /**
     * 初始化设置页中的编辑器组件与表单布局。
     */
    public SettingsComponent() {
        classNameSpELEditor = new LanguageTextField(PropertiesLanguage.INSTANCE, null, "", false);
        fieldNameSpELEditor = new EditorTextField();

        String comment = """
                <p>
                This is a .properties configuration.
                The Key is Reference Class, and Value is <a href="https://docs.spring.io/spring-framework/reference/core/expressions.html">SpEL expression.</a><br>
                If this plugin does not work, you can try clearing this configuration and saving it, this plugin will automatically initialize the default configuration.
                <a href="https://github.com/AugustLee/easy-pojo2json#configure-spel-expression">More details.</a>
                </p>
                """;

        panel = FormBuilder.createFormBuilder()
                .addComponentFillVertically(classNameSpELEditor, 0)
                .addComponent(
                        UI.PanelFactory.panel(new JPanel())
                                .withComment(comment)
                                .createPanel()
                )
                .addVerticalGap(10)
                .addComponent(
                        UI.PanelFactory.panel(fieldNameSpELEditor)
                                .withLabel("Global JSON keys style")
                                .withComment("""
                                        <p>
                                        <a href="https://github.com/AugustLee/easy-pojo2json#json-keys-style-configuration">More details.</a>
                                        </p>
                                        """)
                                .createPanel()
                )
                .getPanel();
    }

    /**
     * 获取设置页根面板。
     *
     * @return 设置页根面板
     */
    public JPanel getPanel() {
        return panel;
    }

    /**
     * 获取类型默认值映射的编辑器。
     *
     * @return SpEL 映射编辑器
     */
    public LanguageTextField getClassNameSpELEditor() {
        return classNameSpELEditor;
    }

    /**
     * 获取 JSON 字段名规则编辑器。
     *
     * @return 字段名规则编辑器
     */
    public EditorTextField getFieldNameSpELEditor() {
        return fieldNameSpELEditor;
    }
}
