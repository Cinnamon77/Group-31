package com.bookkeeping.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author bookkeeping
 **/
@Data
@Schema(description = "账户类型修改")
@EqualsAndHashCode(callSuper = true)
public class AccountTypeEditDTO extends AccountTypeAddDTO{
}
