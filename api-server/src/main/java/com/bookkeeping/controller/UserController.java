package com.bookkeeping.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.bookkeeping.core.api.CommonResult;
import com.bookkeeping.entity.vo.UserinfoVO;
import com.bookkeeping.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bookkeeping
 **/
@Tag(name = "用户相关")
@RestController(value = "UserController")
@RequestMapping(value = "api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "用户信息")
    @RequestMapping(value = "info", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<UserinfoVO> userinfo() {
        return CommonResult.success(userService.userinfo(StpUtil.getLoginIdAsLong()));
    }
}
