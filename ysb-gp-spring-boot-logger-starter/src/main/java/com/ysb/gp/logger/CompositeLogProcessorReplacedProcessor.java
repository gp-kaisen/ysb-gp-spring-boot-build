package com.ysb.gp.logger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

/**
 * 替换{@link com.ysb.logger.CompositeLogProcessor}为{@link Slf4jCompositeLogProcessor}
 */
@Slf4j
public class CompositeLogProcessorReplacedProcessor implements BeanDefinitionRegistryPostProcessor {
    private static final String ORIGIN_BEAN_NAME = "compositeLogProcessor";

    private String replaceBeanName;

    public CompositeLogProcessorReplacedProcessor(String replaceBeanName) {
        this.replaceBeanName = replaceBeanName;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        try {
            if (registry.containsBeanDefinition(ORIGIN_BEAN_NAME)
                    && registry.containsBeanDefinition(replaceBeanName)) {
                BeanDefinition replaceBeanDefinition = registry.getBeanDefinition(replaceBeanName);
                registry.removeBeanDefinition(ORIGIN_BEAN_NAME);
                registry.registerBeanDefinition(ORIGIN_BEAN_NAME, replaceBeanDefinition);
                log.debug("替换Bean %s 为 %s 成功", ORIGIN_BEAN_NAME, replaceBeanName);
            }
        } catch (Exception e) {
            log.error(String.format("替换Bean %s 为 %s 异常", ORIGIN_BEAN_NAME, replaceBeanName), e);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {}

}
