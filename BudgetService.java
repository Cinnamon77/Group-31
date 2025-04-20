package com.bookkeeping.service;

import com.bookkeeping.entity.dto.BudgetSaveDTO;
import com.bookkeeping.entity.vo.BudgetVO;

/**
 * 预算服务
 * @author bookkeeping
 */
public interface BudgetService {
    /**
     * 生成本月预算
     * @param userId 用户ID，如果传空生成所有用户数据
     */
    void generateBudget(Long userId);

    /**
     * 获取用户当月预算数据
     * @param userId 用户ID
     * @return BudgetVO
     */
    BudgetVO getBudget(Long userId);

    /**
     * 保存预算
     * @param userId 用户ID
     * @param dto BudgetSaveDTO
     */
    void saveBudget(Long userId, BudgetSaveDTO dto);
}
