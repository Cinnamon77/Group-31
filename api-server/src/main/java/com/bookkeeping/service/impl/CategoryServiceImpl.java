package com.bookkeeping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bookkeeping.core.api.ResultCode;
import com.bookkeeping.core.exception.Asserts;
import com.bookkeeping.entity.dto.CategoryAddDTO;
import com.bookkeeping.entity.model.TransactionCategory;
import com.bookkeeping.mapper.TransactionCategoryMapper;
import com.bookkeeping.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author bookkeeping
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final TransactionCategoryMapper categoryMapper;

    @Override
    public void addCategory(Long userId, CategoryAddDTO dto) {
        // 验证名称是否重复
        LambdaQueryWrapper<TransactionCategory> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.eq(TransactionCategory::getUserId, userId);
        categoryLambdaQueryWrapper.eq(TransactionCategory::getCategoryName, dto.getName());
        TransactionCategory category = categoryMapper.selectOne(categoryLambdaQueryWrapper);
        if (category != null) {
            Asserts.fail(ResultCode.CATEGORY_NAME_EXIST);
        }

        category = new TransactionCategory();
        category.setUserId(userId);
        category.setCategoryName(dto.getName());
        category.setIcon(dto.getIcon());
        categoryMapper.insert(category);
    }

    @Override
    public void updateCategory(Long userId, Long categoryId, CategoryAddDTO dto) {
        // 获取分类数据
        TransactionCategory category = categoryMapper.selectById(categoryId);
        if (!category.getUserId().equals(userId)) {
            Asserts.fail(ResultCode.CHECK_PARAMS_ERROR);
        }
        // 查询分类名称是否重复
        LambdaQueryWrapper<TransactionCategory> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.ne(TransactionCategory::getId, categoryId);
        categoryLambdaQueryWrapper.eq(TransactionCategory::getCategoryName, dto.getName());
        categoryLambdaQueryWrapper.eq(TransactionCategory::getUserId, userId);
        TransactionCategory checkCategory = categoryMapper.selectOne(categoryLambdaQueryWrapper);
        if (checkCategory != null) {
            Asserts.fail(ResultCode.CATEGORY_NAME_EXIST);
        }

        // 更新分类数据
        category.setCategoryName(dto.getName());
        category.setIcon(dto.getIcon());
        categoryMapper.updateById(category);
    }

    @Override
    public void deleteCategory(Long userId, Long categoryId) {
        // 获取分类数据
        TransactionCategory category = categoryMapper.selectById(categoryId);
        if (!category.getUserId().equals(userId)) {
            Asserts.fail(ResultCode.CHECK_PARAMS_ERROR);
        }

        categoryMapper.deleteById(categoryId);
    }

    @Override
    public List<TransactionCategory> listCategory(Long userId) {
        return categoryMapper.selectList(new LambdaQueryWrapper<TransactionCategory>()
                .eq(TransactionCategory::getUserId, userId).orderByDesc(TransactionCategory::getId));
    }
}
