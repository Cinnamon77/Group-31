package com.bookkeeping.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author bookkeeping
 **/
@Data
@Schema(description = "分页参数")
public class PageDTO {

    @Schema(description = "页码", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long page;

    @Schema(description = "每页数量", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long pageSize;
}
