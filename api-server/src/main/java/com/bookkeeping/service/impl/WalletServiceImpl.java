package com.bookkeeping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bookkeeping.core.api.ResultCode;
import com.bookkeeping.core.exception.Asserts;
import com.bookkeeping.entity.dto.WalletDTO;
import com.bookkeeping.entity.model.Wallet;
import com.bookkeeping.mapper.WalletMapper;
import com.bookkeeping.service.WalletService;
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
public class WalletServiceImpl implements WalletService {
    private final WalletMapper walletMapper;

    @Override
    public List<Wallet> getWalletList(Long userId) {
        LambdaQueryWrapper<Wallet> walletLambdaQueryWrapper = new LambdaQueryWrapper<>();
        walletLambdaQueryWrapper.eq(Wallet::getUserId, userId);
        return walletMapper.selectList(walletLambdaQueryWrapper);
    }

    @Override
    public void addWalletAmount(Long userId, WalletDTO dto) {
        LambdaQueryWrapper<Wallet> walletLambdaQueryWrapper = new LambdaQueryWrapper<>();
        walletLambdaQueryWrapper.eq(Wallet::getUserId, userId);
        walletLambdaQueryWrapper.eq(Wallet::getAccountName, dto.getAccountTypeName());
        Wallet wallet = walletMapper.selectOne(walletLambdaQueryWrapper);

        wallet.setBalance(wallet.getBalance() + dto.getAmount());
        walletMapper.updateById(wallet);
    }
}
