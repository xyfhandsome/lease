package com.atguigu.lease.web.admin.constant;

/**
 * @author peter
 * @date 2024/10/24 19:31
 **/
//可以将Reids相关的一些值定义为常量
public class RedisConstant {
//    key的前缀
    public static final String ADMIN_LOGIN_PREFIX = "admin:login:";
//    time to live
    public static final Integer ADMIN_LOGIN_CAPTCHA_TTL_SEC = 60;
    public static final String APP_LOGIN_PREFIX = "app:login:";
    public static final Integer APP_LOGIN_CODE_RESEND_TIME_SEC = 60;
    public static final Integer APP_LOGIN_CODE_TTL_SEC = 60 * 10;
    public static final String APP_ROOM_PREFIX = "app:room:";
}
