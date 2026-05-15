package com.augustlee.tool.easypojo2json.parser.model;

import com.intellij.psi.PsiType;

import java.util.List;
import java.util.Map;

/**
 * 解析期 POJO 模型基类。
 * 
 * @author August Lee
 * @see POJOObject
 * @since 2026-05-15 15:29:08
 *
 */
public abstract class POJOObject {

    /**
     * 当前递归深度。
     * <p>
     * 用于在循环引用或过深嵌套时及时中断，避免无限递归。
     */
    protected int recursionLevel;

    /**
     * 当前节点下需要过滤的属性列表。
     * <p>
     * 通常来自 {@code @JsonIgnoreProperties} 注解或 JavaDoc 标签。
     */
    protected List<String> ignoreProperties;

    /**
     * 当前泛型上下文中的“泛型名称 -> 实际类型”映射。
     * <p>
     * 例如 {@code T -> UserDTO}，供后续解析字段时替换泛型占位符使用。
     */
    protected Map<String, PsiType> psiClassGenerics;

    /**
     * 获取当前递归深度。
     *
     * @return 当前递归深度
     */
    public int getRecursionLevel() {
        return recursionLevel;
    }

    /**
     * 获取当前节点的忽略属性列表。
     *
     * @return 忽略属性列表
     */
    public List<String> getIgnoreProperties() {
        return ignoreProperties;
    }

    /**
     * 获取当前节点的泛型映射。
     *
     * @return 泛型映射
     */
    public Map<String, PsiType> getPsiClassGenerics() {
        return psiClassGenerics;
    }
}
