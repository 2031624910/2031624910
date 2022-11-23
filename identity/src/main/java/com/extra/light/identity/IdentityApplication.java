package com.extra.light.identity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author 林树毅
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"com.extra.light.common", "com.extra.light.identity"})
public class IdentityApplication {
    public static void main(String[] args) {
        System.setProperty("serverName", "identity");
        SpringApplication application = new SpringApplication(IdentityApplication.class);
        //这里中间配置点东西
        application.run(args);
    }
}
