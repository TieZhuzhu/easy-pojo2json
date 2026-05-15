package com.augustlee.tool.easypojo2json;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.augustlee.tool.easypojo2json.parser.KnownException;
import com.augustlee.tool.easypojo2json.parser.POJO2JSONParser;
import com.augustlee.tool.easypojo2json.parser.POJO2JSONParserFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.uast.*;

/**
 * POJO 转 JSON 动作基类。
 * 
 * @author August Lee
 * @see POJO2JSONAction
 * @since 2026-05-15 15:29:08
 *
 */
public abstract class POJO2JSONAction extends AnAction {

    protected final POJO2JSONParser pojo2JSONParser;

    /**
     * 初始化解析器实例。
     */
    public POJO2JSONAction() {
        this.pojo2JSONParser = POJO2JSONParserFactory.getInstant();
    }

    /**
     * 指定动作更新逻辑运行在线程池后台线程。
     *
     * @return 动作更新线程类型
     */
    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

    /**
     * 基于文件默认类位置执行 POJO 转 JSON。
     *
     * @param psiFile 当前 PSI 文件
     */
    public void pojo2jsonAction(@NotNull final PsiFile psiFile) {
        pojo2jsonAction(psiFile, null, null);
    }

    /**
     * 基于文件默认类位置执行 POJO 转 JSON，并保留字段 JavaDoc 注释。
     *
     * @param psiFile 当前 PSI 文件
     */
    public void pojo2jsonWithCommentAction(@NotNull final PsiFile psiFile) {
        pojo2jsonWithCommentAction(psiFile, null, null);
    }

    /**
     * 根据文件、编辑器和当前 PSI 元素上下文执行 POJO 转 JSON。
     * <p>
     * 解析顺序为：
     * 1. 优先使用显式传入的 PSI 元素。
     * 2. 如果存在编辑器，则使用光标位置向外查找所在类或变量。
     * 3. 如果以上都无法定位，则回退到文件中的第一个 class/record 关键字。
     *
     * @param psiFile    当前 PSI 文件
     * @param editor     当前编辑器，可为空
     * @param psiElement 当前选中 PSI 元素，可为空
     */
    public void pojo2jsonAction(@NotNull final PsiFile psiFile,
                                @Nullable final Editor editor,
                                @Nullable final PsiElement psiElement) {
        copyToClipboard(psiFile, editor, psiElement, false);
    }

    /**
     * 根据文件、编辑器和当前 PSI 元素上下文执行 POJO 转带注释文本。
     *
     * @param psiFile    当前 PSI 文件
     * @param editor     当前编辑器，可为空
     * @param psiElement 当前选中 PSI 元素，可为空
     */
    public void pojo2jsonWithCommentAction(@NotNull final PsiFile psiFile,
                                           @Nullable final Editor editor,
                                           @Nullable final PsiElement psiElement) {
        copyToClipboard(psiFile, editor, psiElement, true);
    }

    /**
     * 执行复制逻辑。
     *
     * @param psiFile         当前 PSI 文件
     * @param editor          当前编辑器
     * @param psiElement      当前 PSI 元素
     * @param withFieldDoc    是否输出字段 JavaDoc 注释
     */
    private void copyToClipboard(@NotNull final PsiFile psiFile,
                                 @Nullable final Editor editor,
                                 @Nullable final PsiElement psiElement,
                                 boolean withFieldDoc) {
        final Project project = psiFile.getProject();

        if (!uastSupported(psiFile)) {
            Notifier.notifyWarn("This file can't convert to json.", project);
            return;
        }

        UElement uElement = null;
        if (psiElement != null) {
            uElement = UastContextKt.toUElement(psiElement, UVariable.class);
        }

        if (uElement == null) {
            if (editor != null) {
                // 优先用用户当前光标位置定位上下文，符合编辑器右键与快捷键的使用直觉。
                PsiElement elementAt = psiFile.findElementAt(editor.getCaretModel().getOffset());
                uElement = UastUtils.findContaining(elementAt, UClass.class);
            }
        }

        if (uElement == null) {
            String fileText = psiFile.getText();
            int offset = fileText.contains("class") ? fileText.indexOf("class") : fileText.indexOf("record");
            if (offset < 0) {
                Notifier.notifyWarn("Can't find class scope.", project);
                return;
            }
            // 当无法从交互上下文定位元素时，回退到文件中的首个类定义作为默认转换对象。
            PsiElement elementAt = psiFile.findElementAt(offset);
            uElement = UastUtils.findContaining(elementAt, UClass.class);
        }

        try {
            String text = withFieldDoc
                    ? pojo2JSONParser.uElementToJSONStringWithComment(uElement)
                    : pojo2JSONParser.uElementToJSONString(uElement);
            ClipboardHandler.copyToClipboard(text);
            String successMessage = withFieldDoc
                    ? "Convert " + psiFile.getName() + " to JSON with JavaDoc success, copied to clipboard."
                    : "Convert " + psiFile.getName() + " to JSON success, copied to clipboard.";
            Notifier.notifyInfo(successMessage, project);
        } catch (KnownException ex) {
            Notifier.notifyWarn(ex.getMessage(), project);
        }
    }

    /**
     * 判断当前文件是否被任一 UAST 语言插件支持。
     *
     * @param psiFile 当前 PSI 文件
     * @return 支持 UAST 解析时返回 {@code true}
     */
    public boolean uastSupported(@NotNull final PsiFile psiFile) {
        return UastLanguagePlugin.Companion.getInstances()
                .stream()
                .anyMatch(l -> l.isFileSupported(psiFile.getName()));
    }
}
