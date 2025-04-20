package com.bookkeeping.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.bookkeeping.core.api.CommonResult;
import com.bookkeeping.entity.dto.BudgetSaveDTO;
import com.bookkeeping.entity.vo.BudgetVO;
import com.bookkeeping.service.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author bookkeeping
 **/
@Tag(name = "预算相关")
@RestController(value = "BudgetController")
@RequestMapping(value = "api/budget")
@RequiredArgsConstructor
public class BudgetController {
    private final BudgetService budgetService;

    @Operation(summary = "预算数据获取")
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<BudgetVO> getBudget() {
        return CommonResult.success(budgetService.getBudget(StpUtil.getLoginIdAsLong()));
    }

    @Operation(summary = "保存预算")
    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    public CommonResult<Void> saveBudget(@RequestBody @Validated BudgetSaveDTO dto) {
        budgetService.saveBudget(StpUtil.getLoginIdAsLong(), dto);
        return CommonResult.success();
    }
}
