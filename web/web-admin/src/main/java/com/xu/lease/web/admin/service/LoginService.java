package com.xu.lease.web.admin.service;

import com.xu.lease.web.admin.vo.login.CaptchaVo;
import com.xu.lease.web.admin.vo.login.LoginVo;
import com.xu.lease.web.admin.vo.system.user.SystemUserInfoVo;

public interface LoginService {

    CaptchaVo getCaptcha();

    String login(LoginVo loginVo);

    SystemUserInfoVo getLoginUserInfo(Long userId);
}
