package com.bookkeeping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bookkeeping.core.api.ResultCode;
import com.bookkeeping.core.exception.Asserts;
import com.bookkeeping.entity.dto.TransactionDTO;
import com.bookkeeping.entity.dto.TransactionPageDTO;
import com.bookkeeping.entity.model.*;
import com.bookkeeping.mapper.*;
import com.bookkeeping.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author bookkeeping
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final UserMapper userMapper;
    private final TransactionRecordMapper transactionMapper;
    private final TransactionCategoryMapper categoryMapper;
    private final AccountCategoryMapper accountCategoryMapper;
    private final WalletMapper walletMapper;

    /**
     * 校验用户、分类和账户类别是否存在
     */
    private void validateTransactionParams(Long userId, Long categoryId, Long accountTypeId) {
        if (userId == null || categoryId == null || accountTypeId == null) {
            log.error("Invalid parameters: userId={}, categoryId={}, accountTypeId={}", userId, categoryId, accountTypeId);
            Asserts.fail(ResultCode.CHECK_PARAMS_ERROR);
        }

        User user = userMapper.selectById(userId);
        TransactionCategory category = categoryMapper.selectById(categoryId);
        AccountCategory accountType = accountCategoryMapper.selectById(accountTypeId);

        if (user == null || category == null || accountType == null) {
            log.error("Validation failed: userId={}, categoryId={}, accountTypeId={}", userId, categoryId, accountTypeId);
            Asserts.fail(ResultCode.CHECK_PARAMS_ERROR);
        }
    }

    /**
     * 设置TransactionRecord的属性
     */
    private void setTransactionRecordAttributes(TransactionRecord record, Long userId, TransactionDTO dto) {
        User user = userMapper.selectById(userId);
        TransactionCategory category = categoryMapper.selectById(dto.getCategoryId());
        AccountCategory accountType = accountCategoryMapper.selectById(dto.getAccountTypeId());

        if (user == null || category == null || accountType == null) {
            log.error("Failed to set attributes: userId={}, categoryId={}, accountTypeId={}", userId, dto.getCategoryId(), dto.getAccountTypeId());
            Asserts.fail(ResultCode.CHECK_PARAMS_ERROR);
        }

        record.setUserId(userId);
        record.setUsername(user.getUsername());
        record.setAmount(dto.getAmount());
        record.setTransactionDate(dto.getDate());
        record.setCategoryName(category.getCategoryName());
        record.setCategoryIcon(category.getIcon());
        record.setAccountName(accountType.getAccountName());
        record.setAccountIcon(accountType.getIcon());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTransaction(Long userId, TransactionDTO dto) {
        try {
            validateTransactionParams(userId, dto.getCategoryId(), dto.getAccountTypeId());

            TransactionRecord record = new TransactionRecord();
            setTransactionRecordAttributes(record, userId, dto);
            transactionMapper.insert(record);

            // 钱包更新
            LambdaQueryWrapper<Wallet> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Wallet::getUserId, userId);
            queryWrapper.eq(Wallet::getAccountName, record.getAccountName());
            Wallet wallet = walletMapper.selectOne(queryWrapper);
            if (wallet != null) {
                wallet.setBalance(wallet.getBalance() + record.getAmount());
                walletMapper.updateById(wallet);
            }

            log.info("Transaction added successfully: userId={}", userId);
        } catch (Exception e) {
            log.error("Failed to add transaction: userId={}, dto={}", userId, dto, e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTransaction(Long userId, Long transactionId, TransactionDTO dto) {
        try {
            if (userId == null || transactionId == null) {
                log.error("Invalid parameters: userId={}, transactionId={}", userId, transactionId);
                Asserts.fail(ResultCode.CHECK_PARAMS_ERROR);
            }

            validateTransactionParams(userId, dto.getCategoryId(), dto.getAccountTypeId());

            TransactionRecord record = transactionMapper.selectById(transactionId);
            if (record == null || !record.getUserId().equals(userId)) {
                log.error("Transaction not found or unauthorized: userId={}, transactionId={}", userId, transactionId);
                Asserts.fail(ResultCode.CHECK_PARAMS_ERROR);
            }
            final Long oldAmount = record.getAmount();

            setTransactionRecordAttributes(record, userId, dto);
            transactionMapper.updateById(record);

            // 钱包更新
            LambdaQueryWrapper<Wallet> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Wallet::getUserId, userId);
            queryWrapper.eq(Wallet::getAccountName, record.getAccountName());
            Wallet wallet = walletMapper.selectOne(queryWrapper);
            if (wallet != null) {
                wallet.setBalance(wallet.getBalance() + (record.getAmount() - oldAmount));
                walletMapper.updateById(wallet);
            }
            log.info("Transaction updated successfully: userId={}, transactionId={}", userId, transactionId);
        } catch (Exception e) {
            log.error("Failed to update transaction: userId={}, transactionId={}, dto={}", userId, transactionId, dto, e);
            throw e;
        }
    }

    @Override
    public IPage<TransactionRecord> pageData(Long userId, TransactionPageDTO dto) {
        if (userId == null || dto.getPage() <= 0 || dto.getPageSize() <= 0) {
            log.error("Invalid pagination parameters: userId={}, page={}, pageSize={}", userId, dto.getPage(), dto.getPageSize());
            Asserts.fail(ResultCode.CHECK_PARAMS_ERROR);
        }

        Page<TransactionRecord> page = new Page<>(dto.getPage(), dto.getPageSize());
        LambdaQueryWrapper<TransactionRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TransactionRecord::getUserId, userId);
        queryWrapper.orderByDesc(TransactionRecord::getTransactionDate, TransactionRecord::getId);

        return transactionMapper.selectPage(page, queryWrapper);
    }
}
