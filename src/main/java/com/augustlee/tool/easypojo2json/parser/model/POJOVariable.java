package com.augustlee.tool.easypojo2json.parser.model;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiVariable;

import java.util.List;
import java.util.Map;

/**
 * 解析期变量模型。
 * 
 * @author August Lee
 * @see POJOVariable
 * @since 2026-05-15 15:29:08
 *
 */
public class POJOVariable extends POJOObject {

    protected final PsiVariable psiVariable;

    protected PsiType psiType;

    protected PsiClass psiClass;

    protected final String srcName;

    protected String name;

    /**
     * 创建变量模型。
     *
     * @param psiVariable 目标变量
     */
    protected POJOVariable(PsiVariable psiVariable) {
        this.psiVariable = psiVariable;
        this.psiType = psiVariable.getType();
        this.srcName = psiVariable.getName();
        this.name = psiVariable.getName();
    }

    /**
     * 初始化根变量模型。
     *
     * @param psiVariable       目标变量
     * @param psiClassGenerics  当前泛型映射
     * @return 初始化后的变量模型
     */
    public static POJOVariable init(PsiVariable psiVariable,
                                    Map<String, PsiType> psiClassGenerics) {
        var pojo = new POJOVariable(psiVariable);
        pojo.recursionLevel = 0;
        pojo.ignoreProperties = List.of();
        pojo.psiClassGenerics = psiClassGenerics;
        return pojo;
    }

    /**
     * 下钻到变量内部更深一层的目标类型。
     *
     * @param deepType          更深层的真实类型
     * @param psiClassGenerics  该层对应的泛型映射
     * @return 当前变量模型自身
     */
    public POJOVariable deepVariable(PsiType deepType, Map<String, PsiType> psiClassGenerics) {
        this.recursionLevel++;
        this.psiType = deepType;
        this.psiClassGenerics = psiClassGenerics;
        return this;
    }

    /**
     * 下钻到对象类型并转为类模型。
     *
     * @param psiClass          目标类
     * @param psiClassGenerics  当前层泛型映射
     * @return 基于当前上下文构建出的类模型
     */
    public POJOClass deepClass(PsiClass psiClass, Map<String, PsiType> psiClassGenerics) {
        this.recursionLevel++;
        this.psiClass = psiClass;
        this.psiClassGenerics = psiClassGenerics;
        return POJOClass.init(this);
    }

    /**
     * 设置当前变量输出到 JSON 中的名称。
     *
     * @param name JSON 字段名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取底层 PSI 变量。
     *
     * @return PSI 变量
     */
    public PsiVariable getPsiVariable() {
        return psiVariable;
    }

    /**
     * 获取当前正在解析的类型。
     *
     * @return 当前类型
     */
    public PsiType getPsiType() {
        return psiType;
    }

    /**
     * 获取当前变量对应的 PSI 类。
     *
     * @return PSI 类
     */
    public PsiClass getPsiClass() {
        return psiClass;
    }

    /**
     * 获取变量源名称。
     *
     * @return 源名称
     */
    public String getSrcName() {
        return srcName;
    }

    /**
     * 获取当前输出名称。
     *
     * @return 输出名称
     */
    public String getName() {
        return name;
    }
}
