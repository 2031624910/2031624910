package com.extra.light.record.service;

import com.extra.light.record.annotation.ExcelMethod;
import com.extra.light.record.model.TestExcelModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 林树毅
 */
public interface TestService {
    /**
     * 用于接口导出测试
     *
     * @param page
     * @param size
     * @param params
     * @param request
     * @return
     */
    @ExcelMethod(clazz = TestExcelModel.class, fileName = "导出测试")
    List<TestExcelModel> resultList(int page, int size, String params, HttpServletRequest request);
}