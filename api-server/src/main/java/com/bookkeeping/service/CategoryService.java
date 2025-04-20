package com.bookkeeping.service;

import com.bookkeeping.entity.dto.CategoryAddDTO;
import com.bookkeeping.entity.model.TransactionCategory;

import java.util.List;

/**
 * 交易类型服务
 * @author bookkeeping
 **/
public interface CategoryService {
    /**
     * 新增交易类型
     * @param userId 用户id
     * @param dto 交易类型新增参数
     */
    void addCategory(Long userId, CategoryAddDTO dto);

    /**
     * 修改交易类型
     * @param userId 用户id
     * @param categoryId 交易类型id
     * @param dto 交易类型修改参数
     */
    void updateCategory(Long userId, Long categoryId, CategoryAddDTO dto);

    /**
     * 删除交易类型
     * @param userId 用户id
     * @param categoryId 交易类型id
     */
    void deleteCategory(Long userId, Long categoryId);

    /**
     * 交易类型列表
     * @param userId 用户id
     * @return 交易类型列表
     */
    List<TransactionCategory> listCategory(Long userId);
}
