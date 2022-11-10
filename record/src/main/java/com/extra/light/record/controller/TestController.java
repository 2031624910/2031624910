package com.extra.light.record.controller;

import com.extra.light.record.annotation.ExcelMethod;
import com.extra.light.record.config.RabbitMqConfig;
import com.extra.light.record.dao.mapper.TestMapper;
import com.extra.light.record.model.AnnotationModel;
import com.extra.light.record.service.TestService;
import com.extra.light.record.util.AnnotationUtil;
import com.extra.light.record.util.SpringUtil;
import com.extra.light.record.util.StringUtil;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.List;

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
    private final RabbitTemplate rabbitTemplate;

    @ApiOperation("测试代码")
    @GetMapping("hello")
    public String hello() {
        return "hello";
    }

    @ApiOperation("数据库1请求")
    @GetMapping("testGetSql1")
    public String testGetSql1() {
        return testMapper.getUserId();
    }

    @ApiOperation("数据库2请求")
    @GetMapping("testGetSql2")
    public String testGetSql2() {
        System.out.println(1);
        System.out.println(2);
        return testMapper.getWriterUserId();
    }


    @ApiOperation("注解工具类测试")
    @GetMapping("annotationTest")
    public String annotationTest() throws Exception {
        String classPath = "classpath*:com/extra/light/record/service/*.class";
        List<AnnotationModel<ExcelMethod>> allAddTagAnnotationUrl = annotationUtil.getAllAddTagAnnotationUrl(classPath, ExcelMethod.class);
        System.out.println(allAddTagAnnotationUrl);
        if (StringUtil.isNotEmpty(allAddTagAnnotationUrl)) {
            for (AnnotationModel<ExcelMethod> model : allAddTagAnnotationUrl) {
                Class<?> clazz = model.getClazz();
                Object bean = SpringUtil.getBean(clazz);
                Method method = model.getMethod();
                Class<?>[] parameterTypes = method.getParameterTypes();
                Object[] args = getArgs(parameterTypes);
                Object invoke = method.invoke(bean, args);
                System.out.println(invoke);
            }
        }
        return "SUCCESS";
    }

    @ApiOperation("往队列添加数据")
    @GetMapping("postMq")
    public String postMq() {
        //使用rabbitTemplate发送消息
        String message = "send email message to user";
        rabbitTemplate.convertAndSend(RabbitMqConfig.TEST_QUEUE, message);
        return "";
    }

    private Object[] getArgs(Class<?>[] parameterTypes) {
        if (StringUtil.isNotEmpty(parameterTypes)) {
            Object[] o = new Object[parameterTypes.length];
            int i = 0;
            for (Class<?> parameterType : parameterTypes) {
                if (parameterType.equals(String.class)) {
                    Object o1 = "呜呜呜";
                    o[i] = o1;
                } else {
                    Object o1 = new Object();
                    o[i] = o1;
                }
                i++;
            }
            return o;
        }
        return new Object[0];
    }


}
