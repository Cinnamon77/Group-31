package com.bookkeeping.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.bookkeeping.core.api.CommonResult;
import com.bookkeeping.entity.dto.WalletDTO;
import com.bookkeeping.entity.model.Wallet;
import com.bookkeeping.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author bookkeeping
 **/
@Tag(name = "钱包相关")
@RestController(value = "WalletController")
@RequestMapping(value = "api/wallet")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @Operation(summary = "钱包数据")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<Wallet>> listWallet() {
        return CommonResult.success(walletService.getWalletList(StpUtil.getLoginIdAsLong()));
    }

    @Operation(summary = "新增钱包金额")
    @RequestMapping(value = "amount", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> addWallet(
            @Validated @RequestBody WalletDTO dto) {
        walletService.addWalletAmount(StpUtil.getLoginIdAsLong(), dto);
        return CommonResult.success();
    }
}
