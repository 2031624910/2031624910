package com.extra.light.record.controller;

import com.extra.light.record.annotation.ExcelMethod;
import com.extra.light.record.dao.mapper.TestMapper;
import com.extra.light.record.model.AnnotationModel;
import com.extra.light.record.service.TestService;
import com.extra.light.record.util.AnnotationUtil;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author 林树毅
 */
@AllArgsConstructor
@RestController
@RequestMapping("test")
public class TestController {
    private final TestMapper testMapper;
    private final TestService testService;
    private final AnnotationUtil annotationUtil;

    @ApiOperation("测试代码")
    @GetMapping("hello")
    public String hello() {
        return "hello";
    }

    @ApiOperation("数据库1请求")
    @GetMapping("testGetSql1")
    public String testGetSql1(){
        return testMapper.getUserId();
    }

    @ApiOperation("数据库2请求")
    @GetMapping("testGetSql2")
    public String testGetSql2(){
        return testMapper.getWriterUserId();
    }


    @ApiOperation("注解工具类测试")
    @GetMapping("annotationTest")
    public String annotationTest() throws Exception {
        String classPath = "classpath*:com/extra/light/record/service/*.class";
        List<AnnotationModel> allAddTagAnnotationUrl = annotationUtil.getAllAddTagAnnotationUrl(classPath, ExcelMethod.class);
        System.out.println(allAddTagAnnotationUrl);
        return "SUCCESS";
    }
}
