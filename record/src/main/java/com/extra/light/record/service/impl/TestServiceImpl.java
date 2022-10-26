package com.extra.light.record.service.impl;

import com.extra.light.record.service.TestService;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {
    @Override
    public String annotationTest(String a) {
        return null;
    }

    @Override
    public String annotationTest1(String b, Object d) {
        return null;
    }

    @Override
    public String annotationTest2(String c, String d) {
        return null;
    }
}
