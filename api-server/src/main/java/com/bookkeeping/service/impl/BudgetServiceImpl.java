package com.bookkeeping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bookkeeping.dao.TransactionRecordDao;
import com.bookkeeping.entity.dto.BudgetSaveDTO;
import com.bookkeeping.entity.model.Budget;
import com.bookkeeping.entity.model.User;
import com.bookkeeping.entity.vo.BudgetVO;
import com.bookkeeping.mapper.BudgetMapper;
import com.bookkeeping.mapper.UserMapper;
import com.bookkeeping.service.BudgetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bookkeeping
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {
    private final UserMapper userMapper;
    private final BudgetMapper budgetMapper;
    private final TransactionRecordDao transactionRecordDao;

    @Override
    public void generateBudget(Long userId) {
        // 检测所有账号本月预算是否有生成
        List<User> userList = new ArrayList<>();
        if (userId == null) {
            userList = userMapper.selectList(null);
        } else {
            User user = userMapper.selectById(userId);
            userList.add(user);
        }

        // 获取当月开始日期
        final LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        final LocalDate lastDayOfMonth = LocalDate.now().withDayOfMonth(firstDayOfMonth.lengthOfMonth());
        final String budgetMonth = firstDayOfMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        userList.forEach(user -> {
            // 检测用户本月是否已经有预算数据，没有生成默认数据
            LambdaQueryWrapper<Budget> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Budget::getUserId, user.getId());
            queryWrapper.eq(Budget::getBudgetMonth, budgetMonth);
            Budget budget = budgetMapper.selectOne(queryWrapper);
            if (budget == null) {
                budget = new Budget();
                budget.setUserId(user.getId());
                budget.setUsername(user.getUsername());
                budget.setAmount(0L);
                budget.setBudgetMonth(budgetMonth);
                budget.setStartDate(firstDayOfMonth);
                budget.setEndDate(lastDayOfMonth);
                budgetMapper.insert(budget);
            }
        });
    }

    @Override
    public BudgetVO getBudget(Long userId) {
        LambdaQueryWrapper<Budget> queryWrapper = new LambdaQueryWrapper<Budget>()
                .eq(Budget::getUserId, userId)
                .eq(Budget::getBudgetMonth, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));
        Budget budget = budgetMapper.selectOne(queryWrapper);
        BudgetVO vo = new BudgetVO();
        BeanUtils.copyProperties(budget, vo);
        // 汇获取本月支出总额
        vo.setSpendingTotalAmount(transactionRecordDao.getSpendingTotalAmount(userId, budget.getStartDate(), budget.getEndDate()));
        return vo;
    }

    @Override
    public void saveBudget(Long userId, BudgetSaveDTO dto) {
        LambdaQueryWrapper<Budget> queryWrapper = new LambdaQueryWrapper<Budget>()
                .eq(Budget::getUserId, userId)
                .eq(Budget::getBudgetMonth, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));
        Budget budget = budgetMapper.selectOne(queryWrapper);
        budget.setAmount(dto.getAmount());
        budgetMapper.updateById(budget);
    }
}
