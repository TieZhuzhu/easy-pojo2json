package com.augustlee.tool.easypojo2json.parser.el;

import com.intellij.psi.PsiElement;

public abstract class DynamicTypeValue implements TypeValue {

    protected final PsiElement psiElement;

    public DynamicTypeValue(PsiElement psiElement) {
        this.psiElement = psiElement;
    }
}
