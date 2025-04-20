package com.bookkeeping.core.exception;

import com.bookkeeping.core.api.IErrorCode;

/**
 * 断言处理类，用于抛出各种API异常
 * @author bookkeeping
 */
public class Asserts {
    public static void fail(IErrorCode errorCode) {
        throw new ApiException(errorCode);
    }
}
