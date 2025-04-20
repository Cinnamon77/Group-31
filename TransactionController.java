package com.bookkeeping.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bookkeeping.core.api.CommonResult;
import com.bookkeeping.entity.dto.TransactionDTO;
import com.bookkeeping.entity.dto.TransactionPageDTO;
import com.bookkeeping.entity.model.TransactionRecord;
import com.bookkeeping.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author bookkeeping
 **/
@Tag(name = "交易相关")
@RequestMapping(value = "api/transaction")
@RestController(value = "TransactionController")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "新增交易记录")
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> addTransaction(@Validated @RequestBody TransactionDTO dto) {
        transactionService.addTransaction(StpUtil.getLoginIdAsLong(), dto);
        return CommonResult.success();
    }

    @Operation(summary = "修改交易记录")
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @ResponseBody
    public CommonResult<Void> updateTransaction(
            @PathVariable("id") Long transactionId,
            @Validated @RequestBody TransactionDTO dto) {
        transactionService.updateTransaction(StpUtil.getLoginIdAsLong(), transactionId, dto);
        return CommonResult.success();
    }

    @Operation(summary = "分页数据")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<IPage<TransactionRecord>> pageDat(@ParameterObject @Validated TransactionPageDTO dto) {
        return CommonResult.success(transactionService.pageData(StpUtil.getLoginIdAsLong(), dto));
    }

}
