package com.bookkeeping.service;

import com.bookkeeping.entity.dto.WalletDTO;
import com.bookkeeping.entity.model.Wallet;

import java.util.List;

/**
 * 钱包服务
 *
 * @author bookkeeping
 */
public interface WalletService {

    /**
     * 获取用户的钱包信息
     *
     * @param userId 用户ID
     * @return 钱包信息
     */
    List<Wallet> getWalletList(Long userId);

    /**
     * 钱包增加余额
     *
     * @param userId   用户ID
     * @param dto      钱包信息
     */
    void addWalletAmount(Long userId, WalletDTO dto);
}
