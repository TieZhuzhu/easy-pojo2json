package com.augustlee.tool.easypojo2json.parser;

/**
 * 插件可预期异常。
 * 
 * @author August Lee
 * @see KnownException
 * @since 2026-05-15 15:29:08
 *
 */
public class KnownException extends RuntimeException {

    /**
     * 创建空异常。
     */
    public KnownException() {
    }

    /**
     * 使用消息创建异常。
     *
     * @param message 异常消息
     */
    public KnownException(String message) {
        super(message);
    }

    /**
     * 使用消息和根因创建异常。
     *
     * @param message 异常消息
     * @param cause   根因异常
     */
    public KnownException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 使用根因创建异常。
     *
     * @param cause 根因异常
     */
    public KnownException(Throwable cause) {
        super(cause);
    }

    /**
     * 创建包含完整异常控制参数的异常。
     *
     * @param message            异常消息
     * @param cause              根因异常
     * @param enableSuppression  是否启用抑制
     * @param writableStackTrace 是否生成堆栈
     */
    public KnownException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
