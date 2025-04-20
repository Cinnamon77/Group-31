package com.bookkeeping.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author bookkeeping
 **/
@Data
@Schema(description = "交易类型新增")
public class CategoryAddDTO {

    @Schema(description = "交易类型名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "交易类型图标", requiredMode = Schema.RequiredMode.REQUIRED)
    private String icon;
}
