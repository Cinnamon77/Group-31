package com.bookkeeping.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author bookkeeping
 **/
@Data
@Schema(name = "CycleBarVO", description = "周期柱状图数据")
public class CycleBarVO {
    @Schema(description = "日期")
    private String date;

    @Schema(description = "支出（分）")
    private Long amount;
}
