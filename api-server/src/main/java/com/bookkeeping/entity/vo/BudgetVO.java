package com.bookkeeping.entity.vo;

import com.bookkeeping.entity.model.Budget;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author bookkeeping
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "BudgetVO", description = "预算数据")
public class BudgetVO extends Budget {

    @Schema(description = "支出总金额")
    private Long spendingTotalAmount;
}
