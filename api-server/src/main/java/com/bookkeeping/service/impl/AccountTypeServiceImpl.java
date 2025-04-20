package com.bookkeeping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bookkeeping.core.api.ResultCode;
import com.bookkeeping.core.exception.Asserts;
import com.bookkeeping.entity.dto.AccountTypeAddDTO;
import com.bookkeeping.entity.dto.AccountTypeEditDTO;
import com.bookkeeping.entity.model.AccountCategory;
import com.bookkeeping.entity.model.User;
import com.bookkeeping.entity.model.Wallet;
import com.bookkeeping.mapper.AccountCategoryMapper;
import com.bookkeeping.mapper.UserMapper;
import com.bookkeeping.mapper.WalletMapper;
import com.bookkeeping.service.AccountTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author bookkeeping
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountTypeServiceImpl implements AccountTypeService {
    private final UserMapper userMapper;
    private final WalletMapper walletMapper;
    private final AccountCategoryMapper accountCategoryMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAccountType(Long userId, AccountTypeAddDTO dto) {
        // 验证名称是否重复
        LambdaQueryWrapper<AccountCategory> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.eq(AccountCategory::getUserId, userId);
        categoryLambdaQueryWrapper.eq(AccountCategory::getAccountName, dto.getName());
        AccountCategory category = accountCategoryMapper.selectOne(categoryLambdaQueryWrapper);
        if (category != null) {
            Asserts.fail(ResultCode.ACCOUNT_NAME_EXIST);
        }

        category = new AccountCategory();
        category.setUserId(userId);
        category.setAccountName(dto.getName());
        category.setIcon(dto.getIcon());
        accountCategoryMapper.insert(category);

        final User finalUser = userMapper.selectById(userId);

        Wallet wallet = new Wallet();
        wallet.setUserId(finalUser.getId());
        wallet.setUsername(finalUser.getUsername());
        wallet.setAccountName(category.getAccountName());
        wallet.setAccountIcon(category.getIcon());
        wallet.setBalance(0L);
        walletMapper.insert(wallet);
    }

    @Override
    public void updateAccountType(Long userId, Long accountTypeId, AccountTypeEditDTO dto) {
        // 获取分类数据
        AccountCategory category = accountCategoryMapper.selectById(accountTypeId);
        if (!category.getUserId().equals(userId)) {
            Asserts.fail(ResultCode.CHECK_PARAMS_ERROR);
        }
        final String oldName = category.getAccountName();
        // 查询分类名称是否重复
        LambdaQueryWrapper<AccountCategory> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.ne(AccountCategory::getId, accountTypeId);
        categoryLambdaQueryWrapper.eq(AccountCategory::getAccountName, dto.getName());
        categoryLambdaQueryWrapper.eq(AccountCategory::getUserId, userId);
        AccountCategory checkCategory = accountCategoryMapper.selectOne(categoryLambdaQueryWrapper);
        if (checkCategory != null) {
            Asserts.fail(ResultCode.ACCOUNT_NAME_EXIST);
        }

        // 更新分类数据
        category.setAccountName(dto.getName());
        category.setIcon(dto.getIcon());
        accountCategoryMapper.updateById(category);

        // 同步修改账户钱包名称对应数据
        LambdaQueryWrapper<Wallet> walletLambdaQueryWrapper = new LambdaQueryWrapper<>();
        walletLambdaQueryWrapper.eq(Wallet::getUserId, userId);
        walletLambdaQueryWrapper.eq(Wallet::getAccountName, oldName);
        Wallet wallet = walletMapper.selectOne(walletLambdaQueryWrapper);
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setBalance(0L);
        }
        wallet.setUserId(userId);
        wallet.setUsername(userMapper.selectById(userId).getUsername());
        wallet.setAccountName(dto.getName());
        wallet.setAccountIcon(dto.getIcon());
        if (wallet.getId() == null) {
            walletMapper.insert(wallet);
        } else {
            walletMapper.updateById(wallet);
        }
    }

    @Override
    public void deleteAccountType(Long userId, Long accountTypeId) {
        AccountCategory category = accountCategoryMapper.selectById(accountTypeId);
        if (!category.getUserId().equals(userId)) {
            Asserts.fail(ResultCode.CHECK_PARAMS_ERROR);
        }

        accountCategoryMapper.deleteById(accountTypeId);
    }

    @Override
    public List<AccountCategory> listAccountType(Long userId) {
        return accountCategoryMapper.selectList(
                new LambdaQueryWrapper<AccountCategory>()
                        .eq(AccountCategory::getUserId, userId)
                        .orderByDesc(AccountCategory::getId)
        );
    }
}
