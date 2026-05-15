package com.augustlee.tool.easypojo2json.parser.el;

import com.intellij.psi.PsiElement;

/**
 * 依赖 PSI 上下文的动态值提供器基类。
 */
public abstract class DynamicTypeValue implements TypeValue {

    protected final PsiElement psiElement;

    /**
     * 创建动态值提供器。
     *
     * @param psiElement 当前 PSI 上下文
     */
    public DynamicTypeValue(PsiElement psiElement) {
        this.psiElement = psiElement;
    }
}
