package com.extra.light.identity;

import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
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
@NacosPropertySource(dataId = "identity", groupId = "light", type = ConfigType.YAML,
        properties = @NacosProperties(serverAddr = "192.168.3.140:8848",namespace = "light"))
public class IdentityApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(IdentityApplication.class);
        //这里中间配置点东西
        application.run(args);
    }
}
