package com.bookkeeping.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @author bookkeeping
 **/
@Data
@Builder
@Schema(name = "LoginVO", description = "登录返回信息")
public class LoginVO {

    @Schema(description = "用户凭证")
    private String token;

    @Schema(description = "首页地址")
    private String homePath;
}
