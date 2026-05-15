package com.augustlee.tool.easypojo2json.parser.el;

/**
 * 短 UUID 默认值提供器。
 * 
 * @author August Lee
 * @see ShortUUIDTypeValue
 * @since 2026-05-15 15:29:08
 *
 */
public class ShortUUIDTypeValue extends UUIDTypeValue {

    /**
     * 返回标准 UUID 的最后一段，作为较短的标识片段。
     *
     * @return 短 UUID 片段
     */
    @Override
    public Object getValue() {
        String uuid = (String) super.getValue();
        return uuid.substring(uuid.lastIndexOf("-") + 1);
    }
}
