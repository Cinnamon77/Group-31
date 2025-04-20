package com.bookkeeping.core.config;

import com.bookkeeping.service.BudgetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author bookkeeping
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class StartupRunner implements ApplicationRunner {
    private final BudgetService budgetService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("ApplicationRunner start finish");
        // 服务器启动自动生成用户预算数据
        budgetService.generateBudget(null);
        log.info("ApplicationRunner generateBudget finish");
    }
}
