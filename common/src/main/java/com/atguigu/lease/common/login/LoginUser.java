package com.atguigu.lease.common.login;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author peter
 * @date 2024/10/26 10:42
 **/
@Data
@AllArgsConstructor //包含所有参数的构造函数
public class LoginUser {
    private Long userId;
    private String userName;
}
