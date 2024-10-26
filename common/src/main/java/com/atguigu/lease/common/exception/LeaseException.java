package com.atguigu.lease.common.exception;

import com.atguigu.lease.common.result.ResultCodeEnum;

/**
 * @author peter
 * @date 2024/10/21 17:20
 **/
@lombok.Getter
//为灵活设置响应信息，可自定义异常类
public class LeaseException extends RuntimeException {
    //异常状态码
    public Integer code;
    /**
     * 通过状态码和错误消息创建异常对象
     * @param message
     * @param code
     */
    public LeaseException(String message,Integer code) {
        super(message);
        this.code = code;
    }
    /**
     * 根据响应结果枚举对象创建异常对象
     * @param resultCodeEnum
     */
    public LeaseException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "LeaseException [code=" + code + ", message=" + this.getMessage() + "]";
    }

}
