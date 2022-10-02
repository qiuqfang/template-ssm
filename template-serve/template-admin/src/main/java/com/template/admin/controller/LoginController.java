package com.template.admin.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.template.admin.bo.SysAdminDetail;
import com.template.admin.dto.AdminLogin;
import com.template.admin.dto.AdminUpdatePassword;
import com.template.admin.service.AdminService;
import com.template.common.api.CommonResult;
import com.template.common.service.RedisService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qiuqfang
 * @date 2021/6/13
 */
@RestController
@RequestMapping("admin")
public class LoginController {

    @Value("${security.tokenHead}")
    private String tokenHead;

    private final AdminService adminService;
    private final RedisService redisService;

    @Autowired
    public LoginController(AdminService adminService, RedisService redisService) {
        this.adminService = adminService;
        this.redisService = redisService;
    }

    @GetMapping("captcha")
    public void captcha(HttpServletResponse httpServletResponse) {
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
        httpServletResponse.setHeader("Content-Type", "image/png");
        /* 获取验证码 */
        String code = lineCaptcha.getCode();
        /* 保存验证码（一般保存到redis中） */
        redisService.set("code", code);
        /* 设置验证码有效时间 5分钟 */
        redisService.expire("code", 60 * 5);
        try {
            lineCaptcha.write(httpServletResponse.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("login")
    public CommonResult<?> login(@RequestBody @Validated AdminLogin userLogin, BindingResult bindingResult) {
        String token = adminService.login(userLogin);
        Map<String, String> data = new HashMap<>();
        data.put("tokenHead", tokenHead);
        data.put("token", token);
        return CommonResult.success(data);
    }

    @GetMapping("info")
    public CommonResult<?> info() {
        SysAdminDetail adminDetail = (SysAdminDetail) SecurityUtils.getSubject().getPrincipal();
        return CommonResult.success(adminDetail);
    }

    @PostMapping("update/password")
    public CommonResult<?> updatePassword(@RequestBody @Validated AdminUpdatePassword adminUpdatePassword, BindingResult bindingResult) {
        int count = adminService.updatePassword(adminUpdatePassword.getOldPassword(), adminUpdatePassword.getPassword());
        if (count == 0) {
            return CommonResult.failed();
        }
        return CommonResult.success();
    }

    @PostMapping("logout")
    public CommonResult<?> logout() {
        int count = adminService.logout();
        return CommonResult.success();
    }
}
