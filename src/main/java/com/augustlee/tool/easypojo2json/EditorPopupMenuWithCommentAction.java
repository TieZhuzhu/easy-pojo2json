package com.augustlee.tool.easypojo2json;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

/**
 * 编辑器右键菜单的“复制 JSON + JavaDoc”动作。
 * 
 * @author August Lee
 * @see EditorPopupMenuWithCommentAction
 * @since 2026-05-15 18:29:08
 *
 */
public class EditorPopupMenuWithCommentAction extends EditorPopupMenuAction {

    /**
     * 执行从编辑器上下文复制带字段 JavaDoc 注释的 JSON 文本。
     *
     * @param e IDEA 动作事件
     */
    @Override
    public void actionPerformed(AnActionEvent e) {
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        final PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        final PsiElement psiElement = e.getData(CommonDataKeys.PSI_ELEMENT);

        pojo2jsonWithCommentAction(psiFile, editor, psiElement);
    }
}
