package com.extra.light.identity.service;

import com.extra.light.common.annotation.ExcelMethod;
import com.extra.light.common.model.UserInfoModel;
import com.extra.light.identity.model.TestModel;

import java.util.List;

/**
 * @author 林树毅
 */
public interface TestService {

    /**
     * 测试用
     *
     * @param page
     * @param size
     * @param userInfo
     * @return
     */
    @ExcelMethod(clazz = TestModel.class, fileName = "test")
    List<TestModel> test(int page, int size, UserInfoModel userInfo);
}
