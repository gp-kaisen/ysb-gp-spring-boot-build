package com.ysb.gp.common.model;

import com.ysb.gp.common.constant.ErrorCode;
import com.ysb.gp.common.exception.BaseErrorCodeException;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * API调用结果对象
 */
@Getter
@Setter
@NoArgsConstructor
public class CommonResult<T> {
    // 错误码
    private String code;

    // 错误消息
    private String message;

    // 返回结果
    private T data;

    public CommonResult(T data) {
        this.code = ErrorCode.SUCCESS;
        this.data = data;
    }

    public CommonResult(BaseErrorCodeException ex) {
        this.code = ex.getErrorCode();
        this.message = ex.getMessage();
    }

    public CommonResult(T data, String errorCode, String errorMessage) {
        this.data = data;
        this.code = errorCode;
        this.message = errorMessage;
    }

    /**
     * API调用是否成功
     * 
     * @return 是否成功
     */
    public boolean isSuccess() {
        return ErrorCode.SUCCESS.equals(code);
    }

    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(data);
    }

    public static <T> CommonResult<T> error(BaseErrorCodeException ex) {
        return new CommonResult<>(ex);
    }

    public static <T> CommonResult<T> error(String errorCode, String errorMessage) {
        return new CommonResult<>(null, errorCode, errorMessage);
    }
}
