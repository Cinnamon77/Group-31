package com.bookkeeping.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.bookkeeping.core.api.CommonResult;
import com.bookkeeping.entity.dto.LoginDTO;
import com.bookkeeping.entity.dto.RegisterDTO;
import com.bookkeeping.entity.vo.LoginVO;
import com.bookkeeping.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bookkeeping
 **/
@Tag(name = "认证相关")
@RestController(value = "AuthController")
@RequestMapping(value = "api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @Operation(summary = "账号注册")
    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> register(@Valid @RequestBody RegisterDTO dto) {
        userService.register(dto);
        return CommonResult.success();
    }

    @Operation(summary = "账号登录")
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        LoginVO vo = userService.login(dto);
        return CommonResult.success(vo);
    }

    @Operation(summary = "codes")
    @RequestMapping(value = "codes", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<String>> codes() {
        List<String> codes = new ArrayList<>();
        return CommonResult.success(codes);
    }

    @Operation(summary = "退出登录")
    @RequestMapping(value = "logout", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> logout() {
        StpUtil.logout();
        return CommonResult.success();
    }
}
