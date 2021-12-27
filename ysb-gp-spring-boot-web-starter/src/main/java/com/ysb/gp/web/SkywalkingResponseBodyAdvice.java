package com.ysb.gp.web;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.ysb.web.common.result.JsonResult;

import org.apache.skywalking.apm.toolkit.trace.ActiveSpan;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import lombok.extern.slf4j.Slf4j;

/**
 * 根据请求返回结果，补充Skywalking追踪信息
 */
@ControllerAdvice
@ConditionalOnProperty(name = "ysb.gp.web.skywalking.jsonResult.enabled", havingValue = "true", matchIfMissing = true)
@Slf4j
public class SkywalkingResponseBodyAdvice implements ResponseBodyAdvice<Object>, InitializingBean {

    @Value("${ysb.gp.web.skywalking.jsonResult.errorCode:}")
    private String errorCode;

    private Set<String> errorCodeSet;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
            ServerHttpResponse response) {
        if (body instanceof JsonResult) {
            JsonResult jsonResult = (JsonResult)body;

            // 补充Skywalking追踪信息
            if (StringUtils.hasText(jsonResult.getCode())) {
                ActiveSpan.tag("result.code", jsonResult.getCode());
            }
            if (StringUtils.hasText(jsonResult.getMessage())) {
                ActiveSpan.tag("result.message", jsonResult.getMessage());
            }

            // 设置指定的业务错误码的追踪状态为错误状态，用于监控报警
            if (errorCodeSet.contains(jsonResult.getCode())) {
                log.info("ActiveSpan Error: %s", jsonResult.getMessage());
                ActiveSpan.error(jsonResult.getMessage());
            }
        }
        return body;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        errorCodeSet = StringUtils.hasText(errorCode)
                ? new HashSet<>(Arrays.asList(errorCode.split(",")))
                : Collections.emptySet();
    }

}
