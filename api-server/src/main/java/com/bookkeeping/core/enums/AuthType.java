package com.bookkeeping.core.enums;

import lombok.Getter;

/**
 * 用户认证类型
 * @author bookkeeping
 */
@Getter
public enum AuthType {
    PASSWORD("PASSWORD"),
    ;

    private final String type;

    AuthType(String type) {
        this.type = type;
    }
}
