package com.bookkeeping.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author bookkeeping
 **/
@Data
@Schema(description = "分类占比参数")
public class CategoryRateDTO {

    @Schema(description = "查询年份", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer year;
}
