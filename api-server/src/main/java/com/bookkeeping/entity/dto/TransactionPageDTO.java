package com.bookkeeping.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author bookkeeping
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "交易分页参数")
public class TransactionPageDTO extends PageDTO{
}
