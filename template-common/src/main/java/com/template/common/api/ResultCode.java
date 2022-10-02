package com.template.common.api;


/**
 * @author qiuqfang
 * @date 2021/6/13
 */
public enum ResultCode {
    /**
     * 操作成功
     */
    SUCCESS(2000, "操作成功"),
    /**
     * 操作失败
     */
    FAILED(5000, "操作失败"),
    /**
     * 暂未登录或身份已过期
     */
    AUTHENTICATE_FAILED(4001, "暂未登录或身份已过期"),
    /**
     * 没有相关权限
     */
    AUTHORIZE_FAILED(4003, "没有相关权限"),
    /**
     * 参数有误
     */
    VALIDATE_FAILED(4004, "参数有误");

    private long code;
    private String message;

    private ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
