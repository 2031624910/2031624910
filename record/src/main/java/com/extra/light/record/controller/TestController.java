package com.extra.light.record.controller;

import com.extra.light.record.dao.mapper.TestMapper;
import com.extra.light.record.db.DynamicDataSource;
import com.extra.light.record.enums.DateBaseType;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 林树毅
 */
@RestController
@RequestMapping("test")
public class TestController {
    @Autowired
    private TestMapper testMapper;

    @ApiOperation("测试代码")
    @GetMapping("hello")
    public String hello() {
        return "hello";
    }

    @ApiOperation("数据库1请求")
    @GetMapping("testGetSql1")
    public String testGetSql1(){
        DynamicDataSource.setDateBaseType(DateBaseType.H2.name());
        return testMapper.getUserId();
    }

    @ApiOperation("数据库2请求")
    @GetMapping("testGetSql2")
    public String testGetSql2(){
        DynamicDataSource.setDateBaseType(DateBaseType.SQL_SERVER_ONE.name());
        return testMapper.getUserId();
    }
}
