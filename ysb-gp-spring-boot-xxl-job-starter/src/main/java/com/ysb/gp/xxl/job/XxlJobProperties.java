package com.ysb.gp.xxl.job;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * Xxl Job 配置
 */
@Data
@ConfigurationProperties(prefix = "xxl.job.executor")
public class XxlJobProperties {

    @Value("${xxl.job.admin.addresses}")
    private String addresses;

    @Value("${xxl.job.accessToken:}")
    private String accessToken;

    private String appname;

    private String ip;

    private int port;
}