package com.bookkeeping.core.api;

import lombok.Getter;

/**
 * 枚举了一些常用API操作码
 *
 * @author bookkeeping
 */
@Getter
public enum ResultCode implements IErrorCode {
    // 操作成功
    SUCCESS(10000, "success"),
    // 操作失败
    FAILED(20000, "fail"),
    CHECK_PARAMS_ERROR(20001, "Parameter validation failed"),
    DATA_NOT_EXIST(20002, "Data does not exist"),
    FILE_UPLOAD_ERROR(20003, "Upload file"),
    FILE_UPLOAD_IMG(20004, "Only images are allowed to upload"),
    // ---------- 账号相关 ----------
    ACCOUNT_NOT_EXIST(30001, "Account does not exist"),
    ACCOUNT_PASSWORD_ERROR(30002, "Wrong login password"),
    LOGIN_UNKNOWN(30003, "Please login first"),
    ACCOUNT_EXIST(30004, "The username already exists"),
    // ---------- 预算相关 ----------

    // ---------- 交易记录相关 ----------
    CATEGORY_NAME_EXIST(50001, "The transaction category already exists"),
    ACCOUNT_NAME_EXIST(50002, "The transaction account type already exists"),
    ;

    private final long code;
    private final String message;

    ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }
}
