package com.extra.light.identity.service.impl;

import com.extra.light.common.model.UserInfoModel;
import com.extra.light.identity.model.TestModel;
import com.extra.light.identity.service.TestService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author 林树毅
 */
@Service
public class TestServiceImpl implements TestService {
    @Override
    public List<TestModel> test(int page, int size, UserInfoModel userInfo) {
        return Collections.singletonList(TestModel.builder().inOut(1).model("未知").name("名字").type("normal").build());
    }
}
