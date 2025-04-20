package com.bookkeeping.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author bookkeeping
 **/
@Data
@Schema(name = "LoginDTO", description = "登录请求参数")
public class LoginDTO {

    @NotBlank(message = "登录账号必填")
    @Schema(description = "登录账号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @NotBlank(message = "登录密码必填")
    @Schema(description = "登录密码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}
