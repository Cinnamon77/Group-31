package com.bookkeeping.service;

import com.bookkeeping.entity.dto.AccountTypeAddDTO;
import com.bookkeeping.entity.dto.AccountTypeEditDTO;
import com.bookkeeping.entity.model.AccountCategory;

import java.util.List;

/**
 * 账号类型服务
 * @author bookkeeping
 */
public interface AccountTypeService {

    /**
     * 新增账号类型
     * @param userId 用户id
     * @param dto 账号类型信息
     */
    void addAccountType(Long userId, AccountTypeAddDTO dto);

    /**
     * 修改账号类型
     * @param userId 用户id
     * @param accountTypeId 账号类型id
     * @param dto 账号类型信息
     */
    void updateAccountType(Long userId, Long accountTypeId, AccountTypeEditDTO dto);

    /**
     * 删除账号类型
     * @param userId 用户id
     * @param accountTypeId 账号类型id
     */
    void deleteAccountType(Long userId, Long accountTypeId);

    /**
     * 获取账号类型列表
     * @param userId 用户id
     * @return 账号类型列表
     */
    List<AccountCategory> listAccountType(Long userId);
}
