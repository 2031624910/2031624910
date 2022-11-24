package com.extra.light.identity;

import com.extra.light.common.config.SystemPropertyConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author 林树毅
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableAspectJAutoProxy
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.extra.light.common", "com.extra.light.identity"})
public class IdentityApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(IdentityApplication.class);

        SystemPropertyConfig.setSystemPropertyConfig("identity", "identity");
        //这里中间配置点东西
        application.run(args);
    }
}
