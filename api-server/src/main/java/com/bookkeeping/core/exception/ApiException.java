package com.bookkeeping.core.exception;

import com.bookkeeping.core.api.IErrorCode;
import lombok.Getter;

/**
 * 自定义API异常
 *
 * @author im
 */
@Getter
public class ApiException extends RuntimeException {
    private final IErrorCode errorCode;

    public ApiException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
