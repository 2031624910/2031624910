package com.extra.light.record;

import com.extra.light.common.config.SystemPropertyConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 创建于2022-10-19
 * 用于测试相关服务与项目相关的连接。以及新知识的实际应用测试
 * 必须要扫描common文件夹，不然无法加载common中的配置
 *
 * @author 林树毅
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@MapperScan("com.extra.light.record.dao.mapper")
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"com.extra.light.common", "com.extra.light.record"})
public class RecordApplication {
    public static void main(String[] args) {
        SystemPropertyConfig.setSystemPropertyConfig("record", "record");
        SpringApplication.run(RecordApplication.class, args);
    }
}
