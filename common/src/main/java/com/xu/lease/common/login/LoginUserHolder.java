package com.xu.lease.common.login;

/**
 * @author peter
 * @date 2024/10/26 10:26
 **/
public class LoginUserHolder {
//    ThreadLocal的主要作用是为每个使用它的线程提供一个独立的变量副本，使每个线程都可以操作自己的变量，而不会互相干扰
    public static ThreadLocal<LoginUser> threadLocal = new ThreadLocal<>();
    public static void setLoginUser(LoginUser loginUser){
        threadLocal.set(loginUser);
    }
    public static LoginUser getLoginUser() {
        return threadLocal.get();
    }
    public static void clear(){
        threadLocal.remove();
    }

}
