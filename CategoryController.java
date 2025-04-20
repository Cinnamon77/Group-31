package com.bookkeeping.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.bookkeeping.core.api.CommonResult;
import com.bookkeeping.entity.dto.CategoryAddDTO;
import com.bookkeeping.entity.dto.CategoryEditDTO;
import com.bookkeeping.entity.model.TransactionCategory;
import com.bookkeeping.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author bookkeeping
 **/
@Tag(name = "交易类型")
@RestController(value = "CategoryController")
@RequestMapping(value = "api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "新增分类")
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> addCategory(@Validated @RequestBody CategoryAddDTO dto) {
        categoryService.addCategory(StpUtil.getLoginIdAsLong(), dto);
        return CommonResult.success();
    }

    @Operation(summary = "修改分类")
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @ResponseBody
    public CommonResult<Void> updateCategory(
            @PathVariable("id") Long id,
            @Validated @RequestBody CategoryEditDTO dto) {
        categoryService.updateCategory(StpUtil.getLoginIdAsLong(), id, dto);
        return CommonResult.success();
    }

    @Operation(summary = "删除分类")
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public CommonResult<Void> deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(StpUtil.getLoginIdAsLong(), id);
        return CommonResult.success();
    }

    @Operation(summary = "获取分类列表")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<TransactionCategory>> listCategory() {
        return CommonResult.success(categoryService.listCategory(StpUtil.getLoginIdAsLong()));
    }

}
