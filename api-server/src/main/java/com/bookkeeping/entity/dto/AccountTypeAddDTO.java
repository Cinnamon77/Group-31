package com.bookkeeping.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author bookkeeping
 **/
@Data
@Schema(description = "账户类型新增")
public class AccountTypeAddDTO {

    @Schema(description = "账户类型名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "账户类型图标", requiredMode = Schema.RequiredMode.REQUIRED)
    private String icon;
}
