package com.bookkeeping.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author bookkeeping
 **/
@Data
@Schema(description = "交易类型修改")
@EqualsAndHashCode(callSuper = true)
public class CategoryEditDTO extends CategoryAddDTO {
}
