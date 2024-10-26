package com.xu.lease.web.admin.controller.login;


import com.xu.lease.common.login.LoginUserHolder;
import com.xu.lease.common.result.Result;
import com.xu.lease.web.admin.service.LoginService;
import com.xu.lease.web.admin.vo.login.CaptchaVo;
import com.xu.lease.web.admin.vo.login.LoginVo;
import com.xu.lease.web.admin.vo.system.user.SystemUserInfoVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "后台管理系统登录管理")
@RestController
@RequestMapping("/admin")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Operation(summary = "获取图形验证码")
    @GetMapping("login/captcha")
    public Result<CaptchaVo> getCaptcha() {
        CaptchaVo captchaVo = loginService.getCaptcha();
        return Result.ok(captchaVo);
    }

    @Operation(summary = "登录")
    @PostMapping("login")
    public Result<String> login(@RequestBody LoginVo loginVo) {
       String token = loginService.login(loginVo);

        return Result.ok(token);
    }

    @Operation(summary = "获取登陆用户个人信息")
    @GetMapping("info")
//    两次解析jwt,效率低
//    public Result<SystemUserInfoVo> info(@RequestHeader("access-token") String token) {
//        Claims claims = JwtUtil.parseToken(token);
//        Long userId = claims.get("userId", Long.class);
//        SystemUserInfoVo userInfo = loginService.getLoginUserInfo(userId);
//        return Result.ok(userInfo);
//    }
    public Result<SystemUserInfoVo> Info() {
        SystemUserInfoVo userInfoVo = loginService.getLoginUserInfo(LoginUserHolder.getLoginUser().getUserId());

    return Result.ok(userInfoVo);}
}