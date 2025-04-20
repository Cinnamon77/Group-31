package com.bookkeeping.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bookkeeping.core.api.ResultCode;
import com.bookkeeping.core.enums.AuthType;
import com.bookkeeping.core.exception.Asserts;
import com.bookkeeping.entity.dto.LoginDTO;
import com.bookkeeping.entity.dto.RegisterDTO;
import com.bookkeeping.entity.model.*;
import com.bookkeeping.entity.vo.LoginVO;
import com.bookkeeping.entity.vo.UserinfoVO;
import com.bookkeeping.mapper.*;
import com.bookkeeping.service.BudgetService;
import com.bookkeeping.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author bookkeeping
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserAuthMapper userAuthMapper;
    private final TransactionCategoryMapper categoryMapper;
    private final AccountCategoryMapper accountCategoryMapper;
    private final WalletMapper walletMapper;
    private final BudgetService budgetService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterDTO dto) {
        // 检查账号是否存在，不允许重复添加
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, dto.getUsername());
        User user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            Asserts.fail(ResultCode.ACCOUNT_EXIST);
        }
        // 构建user
        user = new User();
        user.setUsername(dto.getUsername());
        userMapper.insert(user);
        User finalUser = user;

        // 填充userAuth
        UserAuth userAuth = new UserAuth();
        userAuth.setUserId(finalUser.getId());
        userAuth.setAuthType(AuthType.PASSWORD.getType());
        userAuth.setAuthIdentifier(dto.getUsername());
        userAuth.setAuthSecret(BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt()));
        userAuthMapper.insert(userAuth);

        // 填充默认交易类型
        List<Map<String, String>> categoryList = new ArrayList<>();
        categoryList.add(Map.of("name", "Food", "icon", "svg:canyin"));
        categoryList.add(Map.of("name", "Fashion", "icon", "svg:fushilei"));
        categoryList.add(Map.of("name", "Pet", "icon", "svg:chongwu"));
        categoryList.add(Map.of("name", "Family", "icon", "svg:qinziyou"));
        categoryList.add(Map.of("name", "Shopping", "icon", "svg:icon_shopping"));
        categoryList.add(Map.of("name", "Travel", "icon", "svg:icon_tour"));
        categoryList.add(Map.of("name", "Hotel", "icon", "svg:icon_houserent"));
        categoryList.add(Map.of("name", "Entertainment", "icon", "svg:yule"));
        categoryList.add(Map.of("name", "Education", "icon", "svg:wenti"));
        categoryList.add(Map.of("name", "Charity", "icon", "svg:jiankang"));
        categoryList.add(Map.of("name", "Daily Use", "icon", "svg:riyongpin"));
        categoryList.add(Map.of("name", "Health", "icon", "svg:jiankang_1"));
        categoryList.add(Map.of("name", "Payment", "icon", "svg:icon_phone"));
        categoryList.add(Map.of("name", "Rent", "icon", "svg:fangzu"));
        categoryList.add(Map.of("name", "Insurance", "icon", "svg:baoxian"));
        categoryList.add(Map.of("name", "Cash", "icon", "svg:xianjinliu"));
        categoryList.add(Map.of("name", "Transfer", "icon", "svg:zhuanzhang"));
        categoryList.add(Map.of("name", "Red Packet", "icon", "svg:hongbao"));
        categoryList.add(Map.of("name", "Fee", "icon", "svg:0shouxufei"));
        categoryList.add(Map.of("name", "Repayment", "icon", "svg:huankuandaihuan"));
        categoryList.add(Map.of("name", "Other", "icon", "svg:icon_other"));

        categoryList.forEach(item -> {
            TransactionCategory category = new TransactionCategory();
            category.setUserId(finalUser.getId());
            category.setCategoryName(item.get("name"));
            category.setIcon(item.get("icon"));
            categoryMapper.insert(category);
        });

        // 填充默认账户类型
        List<Map<String, String>> accountTypeList = new ArrayList<>();
        accountTypeList.add(Map.of("name", "Wechat", "icon", "svg:wechat-pay"));
        accountTypeList.add(Map.of("name", "Alipay", "icon", "svg:alipay"));
        accountTypeList.add(Map.of("name", "Card", "icon", "svg:yinlian"));
        accountTypeList.forEach(item -> {
            AccountCategory accountCategory = new AccountCategory();
            accountCategory.setUserId(finalUser.getId());
            accountCategory.setAccountName(item.get("name"));
            accountCategory.setIcon(item.get("icon"));
            accountCategoryMapper.insert(accountCategory);
        });

        // 根据账户类型填充默认钱包
        accountTypeList.forEach(item -> {
            Wallet wallet = new Wallet();
            wallet.setUserId(finalUser.getId());
            wallet.setUsername(finalUser.getUsername());
            wallet.setAccountName(item.get("name"));
            wallet.setAccountIcon(item.get("icon"));
            wallet.setBalance(0L);
            walletMapper.insert(wallet);
        });

        budgetService.generateBudget(finalUser.getId());
    }

    @Override
    public LoginVO login(LoginDTO dto) {
        // 检查用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, dto.getUsername());
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            Asserts.fail(ResultCode.ACCOUNT_NOT_EXIST);
        }

        // 检查密码
        LambdaQueryWrapper<UserAuth> userAuthLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userAuthLambdaQueryWrapper.eq(UserAuth::getUserId, user.getId());
        userAuthLambdaQueryWrapper.eq(UserAuth::getAuthType, AuthType.PASSWORD.getType());
        UserAuth userAuth = userAuthMapper.selectOne(userAuthLambdaQueryWrapper);
        if (!BCrypt.checkpw(dto.getPassword(), userAuth.getAuthSecret())) {
            Asserts.fail(ResultCode.ACCOUNT_PASSWORD_ERROR);
        }

        StpUtil.login(user.getId());

        // 成功后返回信息
        return LoginVO.builder()
                .token(StpUtil.getTokenValue())
                .homePath("/dashboard")
                .build();
    }

    @Override
    public UserinfoVO userinfo(long userId) {
        User user = userMapper.selectById(userId);

        List<String> roles = new ArrayList<>();
        roles.add("super");

        return UserinfoVO.builder()
                .id(userId)
                .username(user.getUsername())
                .roles(roles)
                .build();
    }
}
