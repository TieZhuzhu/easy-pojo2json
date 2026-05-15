package com.augustlee.tool.easypojo2json;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.util.NlsContexts;
import com.augustlee.tool.easypojo2json.parser.SettingsState;
import com.augustlee.tool.easypojo2json.parser.el.EvaluationContextFactory;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * 插件设置页配置入口。
 * <p>
 * 该类负责将 IDEA Settings 页面与 {@link SettingsState} 持久化配置绑定起来，
 * 包括修改检测、保存回写以及界面重置。
 */
public class SettingsConfigurable implements Configurable {

    private SettingsComponent settingsComponent;

    /**
     * 获取在 IDEA 设置页展示的配置名称。
     *
     * @return 配置名称
     */
    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "Easy POJO To JSON";
    }

    /**
     * 创建设置页界面组件。
     *
     * @return 设置页组件
     */
    @Override
    public @Nullable JComponent createComponent() {
        settingsComponent = new SettingsComponent();
        return settingsComponent.getPanel();
    }

    /**
     * 判断当前界面内容是否与持久化配置不一致。
     *
     * @return 存在未保存修改时返回 {@code true}
     */
    @Override
    public boolean isModified() {
        SettingsState settings = SettingsState.getInstance();
        return !settingsComponent.getClassNameSpELEditor().getText().equals(settings.toProp()) ||
                !settingsComponent.getFieldNameSpELEditor().getText().equals(settings.fieldNameSpEL);
    }

    /**
     * 将设置页内容写回持久化配置。
     * <p>
     * 当用户清空输入内容时，这里会回退到默认表达式配置，
     * 保证插件始终能够生成一套可用的 JSON 示例值。
     */
    @Override
    public void apply() {
        SettingsState settings = SettingsState.getInstance();
        String prop = settingsComponent.getClassNameSpELEditor().getText();
        if (StringUtils.isBlank(prop)) {
            settings.classNameSpELMap = EvaluationContextFactory.initExpressionMap();
        } else {
            settings.classNameSpELMap = settings.fromProp(prop);
        }

        String prop2 = settingsComponent.getFieldNameSpELEditor().getText();
        if (StringUtils.isBlank(prop2)) {
            settings.fieldNameSpEL = EvaluationContextFactory.initJsonKeysExpression();
        } else {
            settings.fieldNameSpEL = prop2;
        }
    }

    /**
     * 使用当前持久化配置重置设置页内容。
     */
    @Override
    public void reset() {
        SettingsState settings = SettingsState.getInstance();
        settingsComponent.getClassNameSpELEditor().setText(settings.toProp());
        settingsComponent.getFieldNameSpELEditor().setText(settings.fieldNameSpEL);
    }

    /**
     * 释放设置页引用，避免界面资源被长期持有。
     */
    @Override
    public void disposeUIResources() {
        settingsComponent = null;
    }
}
