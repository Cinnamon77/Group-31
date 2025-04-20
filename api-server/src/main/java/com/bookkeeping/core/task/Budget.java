package com.bookkeeping.core.task;

import com.bookkeeping.service.BudgetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author bookkeeping
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class Budget {
    private final BudgetService budgetService;

    /**
     * 每月生成一次预算
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void run() {
        budgetService.generateBudget(null);
    }
}
