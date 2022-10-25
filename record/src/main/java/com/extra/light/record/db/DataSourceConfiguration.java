package com.extra.light.record.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
import com.extra.light.record.enums.DateBaseType;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 林树毅
 */
@Configuration
public class DataSourceConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid.reader")
    public DataSource s1DataSource(DruidProperty druidProperty){
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return druidProperty.druidDataSource(dataSource);
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid.writer")
    public DataSource s2DataSource(DruidProperty druidProperty){
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return druidProperty.druidDataSource(dataSource);
    }

    @Bean
    @Primary
    public DynamicDataSource master(DataSource s1DataSource,DataSource s2DataSource){
        Map<Object,Object> targetDataSource = new HashMap<>(2);
        targetDataSource.put(DateBaseType.READER.name(),s1DataSource);
        targetDataSource.put(DateBaseType.WRITER.name(),s2DataSource);
        return new DynamicDataSource(s1DataSource,targetDataSource);
    }

    @Bean
    public DruidStatInterceptor druidStatInterceptor(){
        return new DruidStatInterceptor();
    }

    @Bean
    @Scope("prototype")
    public JdkRegexpMethodPointcut jdkRegexpMethodPointcut(){
        JdkRegexpMethodPointcut pointcut = new JdkRegexpMethodPointcut();
        pointcut.setPatterns("com.ramostear.blogdemo.*");
        return pointcut;
    }

    @Bean
    public DefaultPointcutAdvisor defaultPointcutAdvisor(DruidStatInterceptor druidStatInterceptor, JdkRegexpMethodPointcut pointcut){
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(pointcut);
        advisor.setAdvice(druidStatInterceptor);
        return advisor;
    }
}
