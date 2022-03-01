package com.ysb.gp.logger;

import com.ysb.logger.CompositeLogProcessor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 日志自动配置类
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "ysb.gp.logger.enabled", havingValue = "true", matchIfMissing = true)
public class LoggerAutoConfiguration {
    public static final String SLF4J_COMPOSITE_LOG_PROCESSOR_BEAN_NAME = "slf4jCompositeLogProcessor";

    @Bean(name = SLF4J_COMPOSITE_LOG_PROCESSOR_BEAN_NAME)
    CompositeLogProcessor slf4jCompositeLogProcessor() {
        return new Slf4jCompositeLogProcessor();
    }

    @Bean("compositeLogProcessorReplacedProcessor")
    ReplaceBeanDefinitionRegistryPostProcessor compositeLogProcessorReplacedProcessor() {
        return new ReplaceBeanDefinitionRegistryPostProcessor(
                "compositeLogProcessor",
                SLF4J_COMPOSITE_LOG_PROCESSOR_BEAN_NAME);
    }
}
