package com.ysb.gp.logger;

import com.ysb.logger.ms.CompositeMsLogProcessor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 屏蔽{@link CompositeMsLogProcessor}注入配置类
 */
@Configuration
@ConditionalOnClass(CompositeMsLogProcessor.class)
public class CompositeMsLogProcessorConfiguration {
    public static final String NONE_COMPOSITE_MS_LOG_PROCESSOR_BEAN_NAME = "noneCompositeMsLogProcessor";

    @Bean(name = NONE_COMPOSITE_MS_LOG_PROCESSOR_BEAN_NAME)
    CompositeMsLogProcessor noneCompositeMsLogProcessor() {
        return new NoneCompositeMsLogProcessor();
    }

    @Bean("compositeMsLogProcessorReplacedProcessor")
    ReplaceBeanDefinitionRegistryPostProcessor compositeMsLogProcessorReplacedProcessor() {
        return new ReplaceBeanDefinitionRegistryPostProcessor(
                "compositeMsLogProcessor",
                NONE_COMPOSITE_MS_LOG_PROCESSOR_BEAN_NAME);
    }
}
