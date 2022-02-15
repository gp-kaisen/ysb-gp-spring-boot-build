package com.ysb.gp.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author qiwenliang
 * @date 2022/2/11
 */
@Data
@Component
@ConfigurationProperties(prefix = "ysb.gp.web")
public class WebProperties {

    private List<String> ignoreException = new ArrayList<>();

}
