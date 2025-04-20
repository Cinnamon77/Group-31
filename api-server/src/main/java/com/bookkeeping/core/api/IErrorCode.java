package com.bookkeeping.core.api;

/**
 * 封装API的错误码
 * @author bookkeeping
 */
public interface IErrorCode {

    /**
     * 错误码
     * @return code
     */
    long getCode();

    /**
     * 错误描述
     * @return message
     */
    String getMessage();
}
