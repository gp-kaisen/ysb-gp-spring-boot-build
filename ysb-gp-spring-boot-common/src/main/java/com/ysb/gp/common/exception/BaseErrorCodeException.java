package com.ysb.gp.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 错误码异常基类
 */
public abstract class BaseErrorCodeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    @Getter
    @Setter
    private String errorCode;

    /**
     * 构造函数
     * 
     * @param errorCode 错误码
     * @param message 错误信息
     * @param cause 级联异常对象
     */
    protected BaseErrorCodeException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

}
