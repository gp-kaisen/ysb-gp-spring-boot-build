package com.ysb.gp.web.exception.handler;

import com.ysb.gp.common.exception.BaseErrorCodeException;
import com.ysb.gp.web.config.WebProperties;
import com.ysb.web.common.result.JsonResult;
import com.ysb.web.common.result.ReturnCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Optional;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class DefaultExceptionHandler {

    private final WebProperties webProperties;

    /**
     * 参数相关异常处理
     * 
     * @param ex 参数异常
     * @param request 请求对象
     * @return 返回结果
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class, ConstraintViolationException.class})
    @SuppressWarnings("rawtypes")
    public JsonResult paramExceptionHandler(Exception ex, HttpServletRequest request) {
        // 参数错误，返回message格式形如：【age不能为null】
        log.debug("ParamException: {}", request.getRequestURI(), ex);

        String message = "参数验证异常";
        if (ex instanceof MethodArgumentNotValidException) {
            FieldError fieldError = ((MethodArgumentNotValidException)ex).getBindingResult().getFieldError();
            if (fieldError != null) {
                message = fieldError.getField() + fieldError.getDefaultMessage();
            }
        }

        if (ex instanceof ConstraintViolationException) {
            Optional<ConstraintViolation<?>> item =
                    ((ConstraintViolationException)ex).getConstraintViolations().stream().findFirst();
            if (item.isPresent()) {
                message = item.get().getMessage();
            }
        }

        return JsonResult.build(ReturnCode.PARAM_ERROR.getCode(), message);
    }

    /**
     * 业务异常处理
     * 
     * @param ex 业务异常
     * @param request 用户请求
     * @return 返回结果
     */
    @ExceptionHandler(value = {BaseErrorCodeException.class})
    @SuppressWarnings("rawtypes")
    public JsonResult bizExceptionHandler(Exception ex, HttpServletRequest request) {
        String errorCode = ReturnCode.ERROR.getCode();

        if (ex instanceof BaseErrorCodeException) {
            BaseErrorCodeException errorCodeException = (BaseErrorCodeException)ex;
            errorCode = errorCodeException.getErrorCode();
        }

        log.warn("BusinessException: {}, {}", errorCode, request.getRequestURI(), ex);

        return JsonResult.build(errorCode, StringUtils.isBlank(ex.getMessage()) ? "业务异常" : ex.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @SuppressWarnings("rawtypes")
    public JsonResult exceptionHandler(Exception ex, HttpServletRequest request) {

        if (shouldIgnore(ex)) {
            log.warn("SystemException: {}", request.getRequestURI(), ex);
            return JsonResult.build(ReturnCode.PARAM_ERROR.getCode(), "参数错误");
        } else {
            log.error("SystemException: {}", request.getRequestURI(), ex);
            return JsonResult.build(ReturnCode.EXCEPTION.getCode(), "服务器开小差，请稍后再试");
        }

    }

    private boolean shouldIgnore(Exception exception) {
        Class<? extends Exception> clazz = exception.getClass();
        for (String ignoredExceptionName : webProperties.getIgnoreException()) {
            try {
                Class<?> parentClazz = Class.forName(ignoredExceptionName, true, clazz.getClassLoader());
                if (parentClazz.isAssignableFrom(clazz)) {
                    return true;
                }
            } catch (ClassNotFoundException e) {
                log.error("ysb.gp.web.ignoreException config error! class not found: {}", e.getMessage(), e);
            }
        }
        return false;
    }

}
