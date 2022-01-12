package com.ysb.gp.apollo;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class Log4j2ConfigTest {

    @Autowired
    Environment env;

    @Value("${application.a}")
    private String applicationA;

    @Test
    public void loggingConfigAutoUpdate() throws InterruptedException {
        Config config = ConfigService.getConfig("test-sub-cluster-test");
        for (String name : config.getPropertyNames()) {
            System.out.println(name);
        }

        for (int i = 0; i < 5; i++) {
            Thread.sleep(5000);
            log.info("log test");
        }
    }

    @Test
    public void valueAutoUpdate() throws InterruptedException {
        Config config = ConfigService.getConfig("test-sub-cluster-test");
        for (String name : config.getPropertyNames()) {
            System.out.println(name);
        }

        for (int i = 0; i < 5; i++) {
            Thread.sleep(5000);
            log.info("application.a value: {}", applicationA);
        }
    }

    @Test
    public void cluster() throws InterruptedException {
        Config config = ConfigService.getAppConfig();
        for (String name : config.getPropertyNames()) {
            System.out.println(name);
        }
    }

    @Test
    public void namespaceOrder() throws InterruptedException {
        log.info(env.getProperty("test-sub.order-test"));
    }

    @SpringBootApplication
    static class TestConfiguration {}
}
