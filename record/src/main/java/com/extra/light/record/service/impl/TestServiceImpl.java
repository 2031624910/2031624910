package com.extra.light.record.service.impl;

import com.extra.light.record.model.TestExcelModel;
import com.extra.light.record.service.TestService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * @author 林树毅
 */
@Service
public class TestServiceImpl implements TestService {

    @Override
    public List<TestExcelModel> resultList(int page, int size, String params, HttpServletRequest request) {
        return Collections.singletonList(TestExcelModel.builder().money(BigDecimal.ONE).time(7).wait("等待" + params).build());
    }

    @Override
    public List<TestExcelModel> resultList1(int page, int size, String params, HttpServletRequest request) {
        return Collections.singletonList(TestExcelModel.builder().money(BigDecimal.TEN).time(9).wait("不等了" + params).build());
    }
}