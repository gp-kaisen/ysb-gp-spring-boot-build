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
public class ReplaceBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private String originBeanName;
    private String replaceBeanName;

    public ReplaceBeanDefinitionRegistryPostProcessor(String originBeanName, String replaceBeanName) {
        this.originBeanName = originBeanName;
        this.replaceBeanName = replaceBeanName;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        try {
            if (registry.containsBeanDefinition(originBeanName)
                    && registry.containsBeanDefinition(replaceBeanName)) {
                BeanDefinition replaceBeanDefinition = registry.getBeanDefinition(replaceBeanName);
                registry.removeBeanDefinition(originBeanName);
                registry.registerBeanDefinition(originBeanName, replaceBeanDefinition);
                log.debug("替换Bean {} 为 {} 成功", originBeanName, replaceBeanName);
            }
        } catch (Exception e) {
            log.error("替换Bean {} 为 {} 异常", originBeanName, replaceBeanName, e);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {}

}
