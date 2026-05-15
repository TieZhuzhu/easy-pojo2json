package com.augustlee.tool.easypojo2json.parser;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 解析器辅助工具。
 * 
 * @author August Lee
 * @see POJO2JSONParserUtils
 * @since 2026-05-15 15:29:08
 *
 */
public class POJO2JSONParserUtils {

    /**
     * 工具类不允许实例化。
     */
    private POJO2JSONParserUtils() {
    }

    /**
     * 去除 PSI 文本中的双引号。
     *
     * @param psiText PSI 文本
     * @return 去除双引号后的纯文本
     */
    public static String psiTextToString(String psiText) {
        return psiText.replace("\"", "");
    }

    /**
     * 将注解中的数组文本转换为字符串列表。
     * <p>
     * 同时兼容 Java 中的 {@code {"a","b"}} 与 Kotlin 中的 {@code ("a","b")} 写法。
     *
     * @param text 注解属性文本
     * @return 解析后的列表
     */
    public static List<String> arrayTextToList(String text) {
        text = StringUtils.deleteWhitespace(text);

        boolean array = text.length() > 2 &&
                ((text.startsWith("{") && text.endsWith("}")) ||
                        (text.startsWith("(") && text.endsWith(")")));
        if (array) {
            return Arrays.stream(text.substring(1, text.length() - 1)
                            .replace("\"", "")
                            .split(","))
                    .collect(Collectors.toList());
        } else if (text.matches("^\"\\w+\"$")) {
            return List.of(text.replace("\"", ""));
        }

        return List.of();
    }

    /**
     * 从 JavaDoc 文本中提取指定标签后面的逗号分隔值。
     *
     * @param tags 目标标签，例如 {@code @JsonIgnoreProperties}
     * @param text JavaDoc 原文
     * @return 解析后的值列表
     */
    public static List<String> docTextToList(String tags, String text) {
        if (!text.contains(tags)) {
            return List.of();
        }

        int start = text.indexOf(tags) + tags.length();
        int end = start;
        while (text.charAt(end) != '\n') {
            end++;
        }
        if (start == end) {
            return List.of();
        }

        return Arrays.stream(StringUtils.deleteWhitespace(text.substring(start, end + 1)).split(","))
                .collect(Collectors.toList());
    }
}
