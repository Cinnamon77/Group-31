package com.bookkeeping.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author bookkeeping
 **/
@Data
@Schema(name = "CategoryRateVO", description = "分类占比数据")
public class CategoryRateVO {

    @Schema(description = "名称")
    private String categoryName;

    @Schema(description = "支出（分）")
    private Long amountTotal;
}
