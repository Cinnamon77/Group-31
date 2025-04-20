package com.bookkeeping.core.exception;

import cn.dev33.satoken.exception.NotLoginException;
import com.bookkeeping.core.api.CommonResult;
import com.bookkeeping.core.api.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.transaction.TransactionException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author bookkeeping
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 全局异常拦截
     */
    @ExceptionHandler()
    public CommonResult<String> handlerException(Exception e) {
        // 打印堆栈，以供调试
        log.error("全局异常", e);
        // 返回给前端
        return CommonResult.failed();
    }

    /**
     * 事物异常
     */
    @ExceptionHandler(TransactionException.class)
    public CommonResult<String> handlerException(TransactionException e) {
        log.error("事物异常 {}", e.getMessage());
        return CommonResult.failed();
    }

    /**
     * 参数校验异常MethodArgumentNotValidException、BindException
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public CommonResult<String> handlerException(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String message = fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("；"));
        return CommonResult.failed(ResultCode.CHECK_PARAMS_ERROR, message);
    }

    /**
     * 参数校验异常IllegalArgumentException
     */
    @ExceptionHandler({IllegalArgumentException.class})
    public CommonResult<String> handlerException(IllegalArgumentException e) {
        return CommonResult.failed(ResultCode.CHECK_PARAMS_ERROR, e.getMessage());
    }

    /**
     * API异常
     */
    @ExceptionHandler(ApiException.class)
    public CommonResult<String> handlerException(ApiException e) {
        return CommonResult.failed(e.getErrorCode());
    }

    /**
     * 账号未登录异常拦截
     */
    @ExceptionHandler(NotLoginException.class)
    public CommonResult<String> handlerException() {
        return CommonResult.failed(ResultCode.LOGIN_UNKNOWN);
    }
}
