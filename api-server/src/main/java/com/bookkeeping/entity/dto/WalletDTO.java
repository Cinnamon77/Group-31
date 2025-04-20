package com.bookkeeping.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author bookkeeping
 **/
@Data
@Schema(description = "钱包参数")
public class WalletDTO {

    @Schema(description = "账号类型")
    private String accountTypeName;

    @Schema(description = "增加余额")
    private Long amount;
}
