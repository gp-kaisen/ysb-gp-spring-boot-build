package com.ysb.gp.logger;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.ysb.logger.CompositeLogProcessor;
import com.ysb.logger.LogProcessor;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "ysb.gp.logger.enabled", havingValue = "true", matchIfMissing = true)
public class LoggerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    CompositeLogProcessor compositeLogProcessor() {
        return new Slf4jCompositeLogProcessor();
    }
}
