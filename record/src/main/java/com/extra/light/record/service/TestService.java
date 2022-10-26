package com.extra.light.record.service;

import com.extra.light.record.annotation.ExcelMethod;
import org.springframework.stereotype.Service;

/**
 * @author 林树毅
 */
public interface TestService {
    /**
     * 测试注解类
     *
     * @param a
     * @return
     */
    @ExcelMethod(value = "annotationTest", clazz = String.class)
    String annotationTest(String a);

    /**
     * 测试注解类1
     *
     * @param b
     * @param d
     * @return
     */
    @ExcelMethod(value = "annotationTest", clazz = String.class)
    String annotationTest1(String b, Object d);

    /**
     * 测试注解类2
     * @param c
     * @param d
     * @return
     */
    String annotationTest2(String c, String d);
}
