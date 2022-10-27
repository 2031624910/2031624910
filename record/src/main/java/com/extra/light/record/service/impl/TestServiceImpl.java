package com.extra.light.record.service.impl;

import com.extra.light.record.service.TestService;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {
    @Override
    public String annotationTest(String a) {
        System.out.println("我特么来了");
        return null;
    }

    @Override
    public String annotationTest1(String b, Object d) {
        System.out.println("我走拉");
        return null;
    }

    @Override
    public String annotationTest2(String c, String d) {
        System.out.println("我又来了");
        return null;
    }
}
