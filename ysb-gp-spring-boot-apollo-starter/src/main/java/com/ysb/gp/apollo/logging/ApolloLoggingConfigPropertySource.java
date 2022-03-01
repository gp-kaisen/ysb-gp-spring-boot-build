package com.ysb.gp.apollo.logging;

import java.util.Map;

import org.springframework.core.env.EnumerablePropertySource;

/**
 * Apollo日志初始化相关配置的配置源
 */
public class ApolloLoggingConfigPropertySource extends EnumerablePropertySource<Map<String, String>> {

    public ApolloLoggingConfigPropertySource(String name, Map<String, String> source) {
        super(name, source);
    }

    @Override
    public String[] getPropertyNames() {
        return source.keySet().toArray(new String[source.size()]);
    }

    @Override
    public Object getProperty(String name) {
        return source.get(name);
    }
}