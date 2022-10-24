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
    /**
     * 默认的H2内存数据库，在没有安装系统之前使用该数据库
     * @param druidProperty     druid配置属性
     * @return                  DruidDataSource
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid.h2")
    public DataSource h2DataSource(DruidProperty druidProperty){
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return druidProperty.druidDataSource(dataSource);
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid.s1")
    public DataSource s1DataSource(DruidProperty druidProperty){
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return druidProperty.druidDataSource(dataSource);
    }

    @Bean
    @Primary
    public DynamicDataSource master(DataSource h2DataSource, DataSource s1DataSource){
        Map<Object,Object> targetDataSource = new HashMap<>(2);
        targetDataSource.put(DateBaseType.H2.name(),h2DataSource);
        targetDataSource.put(DateBaseType.SQL_SERVER_ONE.name(),s1DataSource);
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
