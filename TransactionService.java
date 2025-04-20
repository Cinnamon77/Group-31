package com.bookkeeping.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bookkeeping.entity.dto.TransactionDTO;
import com.bookkeeping.entity.dto.TransactionPageDTO;
import com.bookkeeping.entity.model.TransactionRecord;

/**
 * 交易服务
 * @author bookkeeping
 */
public interface TransactionService {

    /**
     * 新增交易
     * @param userId 用户ID
     * @param dto 交易信息
     */
    void addTransaction(Long userId, TransactionDTO dto);

    /**
     * 修改交易
     * @param userId 用户ID
     * @param transactionId 交易ID
     * @param dto 交易信息
     */
    void updateTransaction(Long userId, Long transactionId, TransactionDTO dto);

    /**
     * 分页查询交易记录
     * @param userId 用户ID
     * @param dto 分页参数
     * @return 分页数据
     */
    IPage<TransactionRecord> pageData(Long userId, TransactionPageDTO dto);
}
