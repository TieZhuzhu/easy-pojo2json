package com.augustlee.tool.easypojo2json.parser.el;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiEnumConstant;
import com.intellij.psi.PsiField;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 枚举值默认值提供器。
 */
public class EnumType implements RandomTypeValue {

    private PsiClass psiClass;

    /**
     * 默认构造器。
     */
    public EnumType() {
    }

    /**
     * 使用指定枚举类初始化提供器。
     *
     * @param psiClass 枚举 PSI 类
     */
    public EnumType(PsiClass psiClass) {
        this.psiClass = psiClass;
    }

    /**
     * 随机返回一个枚举常量名称。
     *
     * @return 枚举常量名称
     */
    @Override
    public Object getRandomValue() {
        List<String> psiFieldNames = Arrays.stream(psiClass.getAllFields())
                .filter(psiField -> psiField instanceof PsiEnumConstant)
                .map(PsiField::getName)
                .collect(Collectors.toList());

        if (psiFieldNames.isEmpty()) {
            return "";
        }

        return psiFieldNames.get(random.nextInt(0, psiFieldNames.size()));
    }

    /**
     * 返回第一个枚举常量名称作为稳定默认值。
     *
     * @return 首个枚举常量名称
     */
    @Override
    public Object getValue() {
        return Arrays.stream(psiClass.getAllFields())
                .filter(psiField -> psiField instanceof PsiEnumConstant)
                .findFirst()
                .map(PsiField::getName)
                .orElse("");
    }
}
