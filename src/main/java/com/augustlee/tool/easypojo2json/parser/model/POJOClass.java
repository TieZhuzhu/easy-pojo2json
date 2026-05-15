package com.augustlee.tool.easypojo2json.parser.model;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;

import java.util.List;
import java.util.Map;

/**
 * 解析期的类模型。
 */
public class POJOClass extends POJOObject {

    protected final PsiClass psiClass;

    /**
     * 创建类模型。
     *
     * @param psiClass 目标 PSI 类
     */
    protected POJOClass(PsiClass psiClass) {
        this.psiClass = psiClass;
    }

    /**
     * 基于根类初始化类模型。
     *
     * @param psiClass 目标 PSI 类
     * @return 初始化后的类模型
     */
    public static POJOClass init(PsiClass psiClass) {
        var pojo = new POJOClass(psiClass);
        pojo.recursionLevel = 0;
        pojo.ignoreProperties = List.of();
        pojo.psiClassGenerics = Map.of();
        return pojo;
    }

    /**
     * 基于变量上下文创建类模型。
     *
     * @param pojoVariable 上一层变量模型
     * @return 继承上下文后的类模型
     */
    public static POJOClass init(POJOVariable pojoVariable) {
        var pojo = new POJOClass(pojoVariable.psiClass);
        pojo.recursionLevel = pojoVariable.recursionLevel;
        pojo.ignoreProperties = pojoVariable.ignoreProperties;
        pojo.psiClassGenerics = pojoVariable.psiClassGenerics;
        return pojo;
    }

    /**
     * 将类中的字段包装为解析期字段模型。
     *
     * @param psiField 目标字段
     * @return 字段模型
     */
    public POJOField toField(PsiField psiField) {
        return POJOField.init(psiField, this);
    }

    /**
     * 获取底层 PSI 类。
     *
     * @return PSI 类
     */
    public PsiClass getPsiClass() {
        return psiClass;
    }
}
