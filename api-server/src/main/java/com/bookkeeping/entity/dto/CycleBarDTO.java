package com.bookkeeping.entity.dto;

import com.bookkeeping.core.enums.Cycle;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author bookkeeping
 **/
@Data
@Schema(description = "周期柱状图参数")
public class CycleBarDTO {

    @Schema(description = "周期", requiredMode = Schema.RequiredMode.REQUIRED)
    private Cycle cycle;
}
