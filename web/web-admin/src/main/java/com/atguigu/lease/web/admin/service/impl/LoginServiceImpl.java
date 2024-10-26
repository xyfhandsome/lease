package com.atguigu.lease.web.admin.service.impl;

import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.atguigu.lease.common.exception.LeaseException;
import com.atguigu.lease.common.result.ResultCodeEnum;
import com.atguigu.lease.model.entity.SystemUser;
import com.atguigu.lease.model.enums.BaseStatus;
import com.atguigu.lease.web.admin.constant.RedisConstant;
import com.atguigu.lease.web.admin.mapper.SystemUserMapper;
import com.atguigu.lease.web.admin.service.LoginService;
import com.atguigu.lease.web.admin.utils.JwtUtil;
import com.atguigu.lease.web.admin.vo.login.CaptchaVo;
import com.atguigu.lease.web.admin.vo.login.LoginVo;
import com.atguigu.lease.web.admin.vo.system.user.SystemUserInfoVo;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
//import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SystemUserMapper systemUserMapper;

    @Override
    public CaptchaVo getCaptcha() {
//        获取lwh的验证码
        SpecCaptcha specCaptcha = new SpecCaptcha(130,48,4);
//        设置验证码的字符类型为默认类型。
        specCaptcha.setCharType(Captcha.TYPE_DEFAULT);
//        获取验证码的文本内容，并将其转换为小写字母。
        String code = specCaptcha.text().toLowerCase();
        String key = RedisConstant.ADMIN_LOGIN_PREFIX+ UUID.randomUUID();
        stringRedisTemplate.opsForValue().set(key, code, RedisConstant.ADMIN_LOGIN_CAPTCHA_TTL_SEC, TimeUnit.SECONDS);
//        将图片编码为Sting
        String image = specCaptcha.toBase64();
        CaptchaVo captchaVo = new CaptchaVo(image, key);
        return captchaVo;
    }

    @Override
    public String login(LoginVo loginVo) {

        //1.判断是否输入了验证码
        if (!StringUtils.hasText(loginVo.getCaptchaCode())) {
            throw new LeaseException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_NOT_FOUND);
        }
        //2.校验验证码
//        根据key获取code
        String code = stringRedisTemplate.opsForValue().get(loginVo.getCaptchaKey());
//        验证码过期
        if (code == null) {
            throw new LeaseException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_EXPIRED);
        }
        if(!loginVo.getCaptchaCode().toLowerCase().equals(code)){
            throw new LeaseException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_ERROR);
        }
        //3.校验用户是否存在
        SystemUser systemUser = systemUserMapper.selectByUsername(loginVo.getUsername());
        if (systemUser == null) {
            throw new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_NOT_EXIST_ERROR);
        }
        //4.校验用户是否被禁
        if(systemUser.getStatus() == BaseStatus.DISABLE){
            throw new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_DISABLED_ERROR);
        }
        //5.校验用户密码
        if(!systemUser.getPassword().equals(DigestUtils.md5Hex( loginVo.getPassword()))){
            throw new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_ERROR);
        }
        //6.创建并返回TOKEN
        String token = JwtUtil.createToken(systemUser.getId(), systemUser.getUsername());
        return token;
    }

    @Override
    public SystemUserInfoVo getLoginUserInfo(Long userId) {
      SystemUser systemUser = systemUserMapper.selectById(userId);
        SystemUserInfoVo systemUserInfoVo = new SystemUserInfoVo();
        systemUserInfoVo.setName(systemUser.getName());
        systemUserInfoVo.setAvatarUrl(systemUser.getAvatarUrl());
        return systemUserInfoVo;


    }
}
