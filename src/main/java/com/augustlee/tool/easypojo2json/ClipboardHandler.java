package com.augustlee.tool.easypojo2json;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * 剪贴板写入工具。
 * 
 * @author August Lee
 * @see ClipboardHandler
 * @since 2026-05-15 15:29:08
 *
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
