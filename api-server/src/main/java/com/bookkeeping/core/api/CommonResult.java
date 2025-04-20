package com.bookkeeping.core.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

/**
 * @author bookkeeping
 * 通用返回对象
 * @param <T>
 */
@Data
@Builder
public class CommonResult<T> {
    private long code;
    private String msg;
    private T data;
    @JsonIgnore
    private Object[] arguments;

    protected CommonResult() {
    }

    protected CommonResult(long code, String msg, T data, Object... arguments) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.arguments = arguments;
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     * @param  message 提示信息
     */
    public static <T> CommonResult<T> success(T data, String message) {
        return new CommonResult<T>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     * @param  message 提示信息
     * @param  arguments 占位符数据
     */
    public static <T> CommonResult<T> success(T data, String message, Object... arguments) {
        return new CommonResult<T>(ResultCode.SUCCESS.getCode(), message, data, arguments);
    }

    /**
     * 成功返回结果
     */
    public static <T> CommonResult<T> success() {
        return success(null);
    }

    /**
     * 失败返回结果
     * @param errorCode 错误码
     */
    public static <T> CommonResult<T> failed(IErrorCode errorCode) {
        return new CommonResult<T>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    /**
     * 失败返回结果
     * @param errorCode 错误码
     */
    public static <T> CommonResult<T> failed(T data, IErrorCode errorCode) {
        return new CommonResult<T>(errorCode.getCode(), errorCode.getMessage(), data);
    }

    /**
     * 失败返回结果
     * @param errorCode 错误码
     * @param arguments 占位符数据
     */
    public static <T> CommonResult<T> failed(IErrorCode errorCode,Object... arguments) {
        return new CommonResult<T>(errorCode.getCode(), errorCode.getMessage(), null, arguments);
    }

    /**
     * 失败返回结果
     * @param errorCode 错误码
     * @param arguments 占位符数据
     */
    public static <T> CommonResult<T> failed(T data, IErrorCode errorCode, Object... arguments) {
        return new CommonResult<T>(errorCode.getCode(), errorCode.getMessage(), data, arguments);
    }

    /**
     * 失败返回结果
     * @param errorCode 错误码
     * @param message 错误信息
     */
    public static <T> CommonResult<T> failed(IErrorCode errorCode,String message) {
        return new CommonResult<T>(errorCode.getCode(), message, null);
    }

    /**
     * 失败返回结果
     * @param errorCode 错误码
     * @param message 错误信息
     * @param arguments 占位符数据
     */
    public static <T> CommonResult<T> failed(IErrorCode errorCode,String message,Object... arguments) {
        return new CommonResult<T>(errorCode.getCode(), message, null, arguments);
    }

    /**
     * 失败返回结果
     * @param message 提示信息
     */
    public static <T> CommonResult<T> failed(String message) {
        return new CommonResult<T>(ResultCode.FAILED.getCode(), message, null);
    }

    /**
     * 失败返回结果
     * @param message 提示信息
     * @param arguments 占位符数据
     */
    public static <T> CommonResult<T> failed(String message,Object... arguments) {
        return new CommonResult<T>(ResultCode.FAILED.getCode(), message, null, arguments);
    }

    /**
     * 失败返回结果
     */
    public static <T> CommonResult<T> failed() {
        return failed(ResultCode.FAILED);
    }
}
