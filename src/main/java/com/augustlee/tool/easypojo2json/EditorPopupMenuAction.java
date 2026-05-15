package com.augustlee.tool.easypojo2json;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

/**
 * 编辑器右键菜单动作。
 * <p>
 * 该动作用于在当前打开的 Java/Kotlin 文件中，根据光标所在的类、字段、
 * 参数等位置触发 POJO 转 JSON。
 */
public class EditorPopupMenuAction extends POJO2JSONAction {

    /**
     * 根据当前编辑器上下文控制菜单是否展示。
     *
     * @param e IDEA 动作事件
     */
    @Override
    public void update(@NotNull AnActionEvent e) {
        final Project project = e.getProject();
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        final PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);

        boolean menuAllowed = false;
        if (psiFile != null && editor != null && project != null) {
            menuAllowed = uastSupported(psiFile);
        }
        e.getPresentation().setEnabledAndVisible(menuAllowed);
    }

    /**
     * 执行从编辑器上下文转换 JSON 的动作。
     *
     * @param e IDEA 动作事件
     */
    @Override
    public void actionPerformed(AnActionEvent e) {
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        final PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        final PsiElement psiElement = e.getData(CommonDataKeys.PSI_ELEMENT);

        pojo2jsonAction(psiFile, editor, psiElement);
    }
}
