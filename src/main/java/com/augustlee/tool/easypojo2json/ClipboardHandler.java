package com.augustlee.tool.easypojo2json;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * 剪贴板写入工具。
 * <p>
 * 插件在完成 POJO 转 JSON 后统一通过该工具将结果复制到系统剪贴板，
 * 以便用户可以直接粘贴到接口调试工具、文档或测试代码中。
 */
public class ClipboardHandler {

    /**
     * 将文本内容写入系统剪贴板。
     *
     * @param content 要写入剪贴板的文本内容
     */
    public static void copyToClipboard(String content) {
        StringSelection selection = new StringSelection(content);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }
}
