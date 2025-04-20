package com.bookkeeping.service;

import com.bookkeeping.entity.dto.LoginDTO;
import com.bookkeeping.entity.dto.RegisterDTO;
import com.bookkeeping.entity.vo.LoginVO;
import com.bookkeeping.entity.vo.UserinfoVO;

/**
 * 用户服务
 * @author bookkeeping
 */
public interface UserService {

    /**
     * 注册用户
     * @param dto 注册参数
     */
    void register(RegisterDTO dto);

    /**
     * 登录用户
     * @param dto 登录参数
     */
    LoginVO login(LoginDTO dto);

    UserinfoVO userinfo(long userId);
}
