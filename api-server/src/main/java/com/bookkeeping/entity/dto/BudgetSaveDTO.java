package com.bookkeeping.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author bookkeeping
 **/
@Data
@Schema(description = "保存预算参数")
public class BudgetSaveDTO {
    @Schema(description = "预算金额（分）")
    private Long amount;
}
