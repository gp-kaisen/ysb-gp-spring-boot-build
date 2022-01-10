package com.ysb.gp.common.exception;

import com.ysb.gp.common.constant.ErrorCode;

/**
 * 通用业务异常类
 */
public class BusinessException extends BaseErrorCodeException {

    /**
     * 默认错误码
     */
    public static final String DEFAULT_ERROR_CODE = ErrorCode.ERROR;

    /**
     * 构造函数
     * 
     * @param errorCode 错误码
     * @param message 错误信息
     * @param cause 级联异常对象
     */
    public BusinessException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    /**
     * 构造函数，错误码为{@link BusinessException}默认错误码
     * 
     * @param message 错误消息
     */
    public BusinessException(String message) {
        super(DEFAULT_ERROR_CODE, message, null);
    }

    /**
     * 构造函数，错误码为{@link BusinessException}默认错误码
     * 
     * @param message 错误消息
     * @param cause 级联异常对象
     */
    public BusinessException(String message, Throwable cause) {
        super(DEFAULT_ERROR_CODE, message, null);
    }

    /**
     * 构造函数
     * 
     * @param errorCode 错误码
     * @param message 错误信息
     */
    public BusinessException(String errorCode, String message) {
        super(errorCode, message, null);
    }

}
