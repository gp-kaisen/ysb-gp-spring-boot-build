package com.ysb.gp.logger;

import com.ysb.logger.CompositeLogProcessor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "ysb.gp.logger.enabled", havingValue = "true", matchIfMissing = true)
public class LoggerAutoConfiguration {
    public static final String REPLACE_BEAN_NAME = "slf4jCompositeLogProcessor";

    @Bean(name = REPLACE_BEAN_NAME)
    CompositeLogProcessor slf4jCompositeLogProcessor() {
        return new Slf4jCompositeLogProcessor();
    }

    @Bean
    @ConditionalOnMissingBean
    CompositeLogProcessorReplacedProcessor compositeLogProcessorReplacedProcessor() {
        return new CompositeLogProcessorReplacedProcessor(REPLACE_BEAN_NAME);
    }

}
