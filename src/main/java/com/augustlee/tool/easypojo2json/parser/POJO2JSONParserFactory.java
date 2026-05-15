package com.augustlee.tool.easypojo2json.parser;

/**
 * 解析器工厂。
 * 
 * @author August Lee
 * @see POJO2JSONParserFactory
 * @since 2026-05-15 15:29:08
 *
 */
public class POJO2JSONParserFactory {

    private static final POJO2JSONParser pojo2JSONParser = new POJO2JSONParser();

    /**
     * 工具类不允许实例化。
     */
    private POJO2JSONParserFactory() {
    }

    /**
     * 获取解析器单例。
     *
     * @return 解析器实例
     */
    public static POJO2JSONParser getInstant() {
        return pojo2JSONParser;
    }
}
