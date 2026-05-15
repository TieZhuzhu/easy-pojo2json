package com.augustlee.tool.easypojo2json.parser.model;

import com.google.common.base.CaseFormat;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;

import java.util.List;

/**
 * 解析期字段模型。
 * 
 * @author August Lee
 * @see POJOField
 * @since 2026-05-15 15:29:08
 *
 */
public class POJOField extends POJOVariable {

    protected final PsiField psiField;

    protected final PsiClass psiFieldClass;

    /**
     * 创建字段模型。
     *
     * @param psiField      目标字段
     * @param psiFieldClass 字段所属类
     */
    protected POJOField(PsiField psiField, PsiClass psiFieldClass) {
        super(psiField);
        this.psiField = psiField;
        this.psiFieldClass = psiFieldClass;
    }

    /**
     * 基于类上下文初始化字段模型。
     *
     * @param psiField   目标字段
     * @param pojoClass  所属类模型
     * @return 初始化后的字段模型
     */
    public static POJOField init(PsiField psiField, POJOClass pojoClass) {
        var pojo = new POJOField(psiField, pojoClass.psiClass);
        pojo.recursionLevel = pojoClass.recursionLevel;
        pojo.ignoreProperties = pojoClass.ignoreProperties;
        pojo.psiClassGenerics = pojoClass.psiClassGenerics;
        return pojo;
    }

    /**
     * 设置当前字段向下展开时需要过滤的属性。
     *
     * @param ignoreProperties 忽略属性列表
     */
    public void setIgnoreProperties(List<String> ignoreProperties) {
        this.ignoreProperties = ignoreProperties;
    }

    /**
     * 获取底层 PSI 字段。
     *
     * @return PSI 字段
     */
    public PsiField getPsiField() {
        return psiField;
    }

    /**
     * 获取字段所属 PSI 类。
     *
     * @return 所属类
     */
    public PsiClass getPsiFieldClass() {
        return psiFieldClass;
    }

    /**
     * 获取 lowerCamelCase 字段名。
     *
     * @return lowerCamelCase 名称
     */
    public String getCamelCaseName() {
        return this.getName();
    }

    /**
     * 获取 snake_case 字段名。
     *
     * @return snake_case 名称
     */
    public String getSnakeCaseName() {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this.psiField.getName());
    }

    /**
     * 获取 kebab-case 字段名。
     *
     * @return kebab-case 名称
     */
    public String getKebabCaseName() {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, this.psiField.getName());
    }

    /**
     * 获取 PascalCase 字段名。
     *
     * @return PascalCase 名称
     */
    public String getPascalCaseName() {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, this.psiField.getName());
    }

    /**
     * 获取 UPPER_SNAKE_CASE 字段名。
     *
     * @return UPPER_SNAKE_CASE 名称
     */
    public String getSnakeCaseUpperName() {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, this.psiField.getName());
    }

    /**
     * 获取全小写字段名。
     *
     * @return 全小写名称
     */
    public String getLowerCaseName() {
        return this.getCamelCaseName().toLowerCase();
    }

    /**
     * 获取 lower.dot.case 字段名。
     *
     * @return lower.dot.case 名称
     */
    public String getLowerDotCaseName() {
        return this.getKebabCaseName().replace("-", ".");
    }
}
