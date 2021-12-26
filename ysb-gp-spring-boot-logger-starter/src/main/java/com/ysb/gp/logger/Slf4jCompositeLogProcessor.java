package com.ysb.gp.logger;

import com.ysb.logger.UserOpLog;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ysb.logger.CompositeLogProcessor;
import com.ysb.logger.JsonResultLogOps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 使用Slf4j规范记录用户请求日志，用于覆盖平台{@link CompositeLogProcessor}，使用Slf4j方式输出日志，移除对{@link brave.Tracer}依赖
 */
@Slf4j
public class Slf4jCompositeLogProcessor extends CompositeLogProcessor {

    private static final Logger operationLog = LoggerFactory.getLogger("gp.access.user-operation");

    // 后续需要优化为参数配置
    private static final int MAX_PARAMS_LENGTH = 3000;
    private static final int MAX_RESULT_DATA_LENGTH = 1000;

    @Autowired
    private ObjectMapper objectMapper;

    public Slf4jCompositeLogProcessor() {
        super(null);// 不再使用brave.Tracer
    }

    @Async("logAsync")
    @Override
    public void process(UserOpLog<JsonResultLogOps> userOpLog) {
        try {
            if (operationLog.isInfoEnabled()) {
                operationLog.info(objectMapper.writeValueAsString(convert(userOpLog)));
            }
        } catch (JsonProcessingException jpe) {
            log.warn("用户请求日志序列化错误", jpe);
        }
    }

    /**
     * 将原用户操作日志对象转换{@link UserOpLogJsonModel}，便于Json序列化写入的日志对象
     * 
     * @param userOpLog 用户操作日志对象
     * @return {@link UserOpLogJsonModel}
     * @throws JsonProcessingException
     */
    private UserOpLogJsonModel convert(UserOpLog<JsonResultLogOps> userOpLog) throws JsonProcessingException {
        UserOpLogJsonModel result = new UserOpLogJsonModel();
        result.setUserId(userOpLog.getuId());
        result.setIp(userOpLog.getIp());
        result.setMethod(userOpLog.getMethod());
        result.setUrl(userOpLog.getUrl());
        result.setUrl(userOpLog.getUrl());
        result.setCost(userOpLog.getMs());

        if (userOpLog.getParams() != null && !userOpLog.getParams().isEmpty()) {
            String paramsStr = objectMapper.writeValueAsString(userOpLog.getParams());
            result.setParams(
                    paramsStr.length() <= MAX_PARAMS_LENGTH ? paramsStr : paramsStr.substring(0, MAX_PARAMS_LENGTH));
        }

        if (userOpLog.getResult() != null) {
            result.setResultCode(userOpLog.getResult().getCode());
            result.setResultMessage(userOpLog.getResult().getMessage());
            String resultDataStr = objectMapper.writeValueAsString(userOpLog.getResult().getData());
            result.setResultData(resultDataStr.length() <= MAX_RESULT_DATA_LENGTH ? resultDataStr
                    : resultDataStr.substring(0, MAX_RESULT_DATA_LENGTH));
        }
        return result;
    }

    @Getter
    @Setter
    static class UserOpLogJsonModel {

        @JsonProperty("user_id")
        private int userId; // 用户ID

        private String ip;// 请求IP

        private String method;// 方法名

        private String url;// 请求URL

        private long cost;// 耗时，单位：毫秒

        private String params;// 所有未经过处理的参数，包括JSON String，Form Data，和Query String

        @JsonProperty("result_code")
        private String resultCode;// 返回代码

        @JsonProperty("result_message")
        private String resultMessage;// 返回信息

        @JsonProperty("result_data")
        private String resultData;// 返回信息
    }
}
