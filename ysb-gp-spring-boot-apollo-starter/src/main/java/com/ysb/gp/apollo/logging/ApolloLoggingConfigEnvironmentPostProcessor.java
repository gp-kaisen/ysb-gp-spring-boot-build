package com.ysb.gp.apollo.logging;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

import com.ctrip.framework.apollo.ConfigFile;
import com.ctrip.framework.apollo.ConfigFileChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.build.ApolloInjector;
import com.ctrip.framework.apollo.core.enums.ConfigFileFormat;
import com.ctrip.framework.apollo.model.ConfigFileChangeEvent;
import com.ctrip.framework.apollo.spring.boot.ApolloApplicationContextInitializer;
import com.ctrip.framework.apollo.spring.config.PropertySourcesConstants;
import com.ctrip.framework.apollo.util.ConfigUtil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 扩展Apollo，支持spring logging system {logging.config} 配置
 * 把apollo.bootstrap.logging.config指定的配置命名文件内容写入本地缓存文件，然后增加logging.config配置指向本地文件
 */
@Slf4j
public class ApolloLoggingConfigEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {
    public static final String APOLLO_LOGGING_CONFIG_PROPERTY_NAME = "apollo.bootstrap.logging.config";

    public static final String SPRING_BOOT_LOGGING_CONFIG_PROPERTY_NAME = "logging.config";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment configurableEnvironment,
            SpringApplication springApplication) {

        String namespace = configurableEnvironment.getProperty(APOLLO_LOGGING_CONFIG_PROPERTY_NAME);
        if (!StringUtils.hasText(namespace)) {
            return;
        }

        if (!configurableEnvironment.getPropertySources()
                .contains(PropertySourcesConstants.APOLLO_BOOTSTRAP_PROPERTY_SOURCE_NAME)) {
            log.warn("Apollo logging config file init failed, apollo is not initialized");
            return;
        }

        try {
            ConfigFile file = getConfigFile(namespace);
            if (!file.hasContent()) {
                log.warn("Apollo logging config file init failed, {} namespace is empty", namespace);
                return;
            }

            // 注意使用file.getNamespace获取命名空间全称
            Path path = getConfigFileLocalPath(file.getNamespace());
            Files.write(path, file.getContent().getBytes(), StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

            log.debug("Apollo logging config file inited, file path: {}", path);

            file.addChangeListener(new ApolloLoggingConfigFileChangeListener());

            Map<String, String> propertyMap = new HashMap<>();
            propertyMap.put("logging.config", path.toString());
            ApolloLoggingConfigPropertySource propertySource = new ApolloLoggingConfigPropertySource(
                    "apolloLoggingConfigProperties", propertyMap);
            configurableEnvironment.getPropertySources().addLast(propertySource);

        } catch (Exception ex) {
            log.warn("Apollo logging config file init failed", ex);
        }
    }

    /**
     * 获取指定命名空间配置文件
     * 
     * @param namespace 命名空间
     * @return 配置文件
     */
    private ConfigFile getConfigFile(String namespace) {
        // 判断日志命名空间配置类型，未指定表示为.properties
        ConfigFileFormat configFileFormat = ConfigFileFormat.Properties;
        int index = namespace.lastIndexOf(".");
        if (index >= 0) {
            String format = namespace.substring(index + 1, namespace.length());
            if (ConfigFileFormat.isValidFormat(format)) {
                namespace = namespace.substring(0, index);
                configFileFormat = ConfigFileFormat.fromString(format);
            }
        }
        ConfigFile file = ConfigService.getConfigFile(namespace, configFileFormat);
        return file;

    }

    private static Path getConfigFileLocalPath(String namespace) {
        String dir = ApolloInjector.getInstance(ConfigUtil.class).getDefaultLocalCacheDir();
        // 变更情况，Namespace名称会
        return Paths.get(dir, namespace);
    }

    @Override
    public int getOrder() {
        return ApolloApplicationContextInitializer.DEFAULT_ORDER + 1;
    }

    static class ApolloLoggingConfigFileChangeListener implements ConfigFileChangeListener {

        @Override
        public void onChange(ConfigFileChangeEvent changeEvent) {
            String namespace = changeEvent.getNamespace();
            try {
                Path path = getConfigFileLocalPath(namespace);
                Files.write(path, changeEvent.getNewValue().getBytes(), StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING);
                log.info("Apollo logging config refresh success, {}", namespace);
            } catch (Exception e) {
                log.warn("Apollo logging config refresh failed, {}", namespace, e);
            }
        }

    }

}