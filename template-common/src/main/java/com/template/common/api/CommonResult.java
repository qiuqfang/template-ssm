package com.template.common.api;


/**
 * @author qiuqfang
 * @date 2021/6/13
 */
public class CommonResult<T> {
    private long code;
    private String message;
    private T data;

    protected CommonResult() {
    }

    protected CommonResult(long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功返回结果
     *
     * @param <T> 泛型
     * @return 成功返回结果
     */
    public static <T> CommonResult<T> success() {
        return new CommonResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     * @param <T>  泛型
     * @return 成功返回结果
     */
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回结果
     *
     * @param data    获取的数据
     * @param message 提示信息
     * @param <T>     泛型
     * @return 成功返回结果
     */
    public static <T> CommonResult<T> success(T data, String message) {
        return new CommonResult<T>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败返回结果
     *
     * @param <T> 泛型
     * @return 失败返回结果
     */
    public static <T> CommonResult<T> failed() {
        return failed(ResultCode.FAILED);
    }

    /**
     * 失败返回结果
     *
     * @param message 提示信息
     * @param <T>     泛型
     * @return 失败返回结果
     */
    public static <T> CommonResult<T> failed(String message) {
        return failed(ResultCode.FAILED, message);
    }

    /**
     * 失败返回结果
     *
     * @param resultCode 错误码
     * @param <T>        泛型
     * @return 失败返回结果
     */
    public static <T> CommonResult<T> failed(ResultCode resultCode) {
        return new CommonResult<T>(resultCode.getCode(), resultCode.getMessage(), null);
    }

    /**
     * 失败返回结果
     *
     * @param resultCode 错误码
     * @param message    错误信息
     * @param <T>        泛型
     * @return 失败返回结果
     */
    public static <T> CommonResult<T> failed(ResultCode resultCode, String message) {
        return new CommonResult<T>(resultCode.getCode(), message, null);
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
