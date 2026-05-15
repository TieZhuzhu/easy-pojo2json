package com.augustlee.tool.easypojo2json.parser;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.SettingsCategory;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.augustlee.tool.easypojo2json.parser.el.EvaluationContextFactory;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 插件持久化配置。
 * <p>
 * 该状态对象保存两类用户可配置内容：
 * 1. 类型默认值映射 {@link #classNameSpELMap}
 * 2. JSON 字段名生成表达式 {@link #fieldNameSpEL}
 */
@State(name = "com.augustlee.tool.easypojo2json.parser.SettingsState",
        storages = @Storage("Pojo2jsonPlugin.xml"),
        category = SettingsCategory.TOOLS)
public class SettingsState implements PersistentStateComponent<SettingsState> {

    /**
     * Java 类型到 SpEL 表达式的映射。
     * <p>
     * 例如：{@code {"java.util.UUID":"#{#uuid.getValue()}"}}
     */
    public Map<String, String> classNameSpELMap;

    /**
     * JSON 字段名的生成表达式。
     */
    public String fieldNameSpEL;

    /**
     * 使用默认表达式初始化配置。
     */
    public SettingsState() {
        classNameSpELMap = EvaluationContextFactory.initExpressionMap();
        fieldNameSpEL = EvaluationContextFactory.initJsonKeysExpression();
    }

    /**
     * 获取应用级单例配置。
     *
     * @return 配置单例
     */
    public static SettingsState getInstance() {
        return ApplicationManager.getApplication().getService(SettingsState.class);
    }

    /**
     * 返回当前持久化状态对象。
     *
     * @return 当前状态
     */
    @Override
    public @Nullable SettingsState getState() {
        return this;
    }

    /**
     * 使用 IDEA 序列化后的状态覆盖当前对象。
     *
     * @param state 外部加载到的状态
     */
    @Override
    public void loadState(@NotNull SettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    /**
     * 将 properties 风格文本解析为映射表。
     *
     * @param prop 用户在设置页中维护的文本
     * @return 解析后的映射表
     */
    public Map<String, String> fromProp(@NotNull String prop) {
        if (StringUtils.isBlank(prop)) {
            return new LinkedHashMap<>();
        }
        return Arrays.stream(prop.split("\n"))
                .filter(StringUtils::isNotBlank)
                .map(s -> s.split("="))
                .collect(Collectors.toMap(s -> s[0], s -> s[1], (ov, nv) -> ov, LinkedHashMap::new));
    }

    /**
     * 将当前类型映射导出为 properties 风格文本。
     *
     * @return 可展示在设置页中的文本内容
     */
    public String toProp() {
        if (classNameSpELMap.isEmpty()) {
            return "";
        }
        return classNameSpELMap.entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("\n"));
    }
}
