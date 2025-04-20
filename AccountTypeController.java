package com.bookkeeping.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.bookkeeping.core.api.CommonResult;
import com.bookkeeping.entity.dto.AccountTypeAddDTO;
import com.bookkeeping.entity.dto.AccountTypeEditDTO;
import com.bookkeeping.service.AccountTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author bookkeeping
 **/
@Tag(name = "账号类型")
@RestController(value = "AccountTypeController")
@RequestMapping(value = "api/account-type")
@RequiredArgsConstructor
public class AccountTypeController {
    private final AccountTypeService accountTypeService;

    @Operation(summary = "新增账号类型")
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> addAccountType(@Validated @RequestBody AccountTypeAddDTO dto) {
        accountTypeService.addAccountType(StpUtil.getLoginIdAsLong(), dto);
        return CommonResult.success();
    }

    @Operation(summary = "修改账号类型")
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @ResponseBody
    public CommonResult<Void> updateAccountType(
            @PathVariable("id") Long accountTypeId,
            @Validated @RequestBody AccountTypeEditDTO dto) {
        accountTypeService.updateAccountType(StpUtil.getLoginIdAsLong(), accountTypeId, dto);
        return CommonResult.success();
    }

    @Operation(summary = "删除账号类型")
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public CommonResult<Void> deleteAccountType(@PathVariable("id") Long accountTypeId) {
        accountTypeService.deleteAccountType(StpUtil.getLoginIdAsLong(), accountTypeId);
        return CommonResult.success();
    }

    @Operation(summary = "获取账号类型列表")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<Object> listAccountType() {
        return CommonResult.success(accountTypeService.listAccountType(StpUtil.getLoginIdAsLong()));
    }
}
