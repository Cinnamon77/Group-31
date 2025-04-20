package com.bookkeeping.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author bookkeeping
 **/
@Data
@Schema(description = "交易新增")
public class TransactionDTO {

    @Schema(description = "交易金额（分）", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long amount;

    @Schema(description = "交易分类ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long categoryId;

    @Schema(description = "交易账号类型ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long accountTypeId;

    @Schema(description = "交易日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate date;
}
