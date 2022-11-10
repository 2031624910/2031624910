package com.extra.light.record.controller;

import com.extra.light.record.model.ExcelMethodInvokeModel;
import com.extra.light.record.model.bo.ExportBo;
import com.extra.light.record.util.SpringUtil;
import com.extra.light.record.util.StringUtil;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author 林树毅
 */
@AllArgsConstructor
@RestController
@RequestMapping("excel")
public class ExcelController {
    private final Map<String, ExcelMethodInvokeModel> excelMethodInvokeMap;

    @ApiOperation("export")
    @PostMapping("export")
    public String export(@RequestBody ExportBo bo) {
        ExcelMethodInvokeModel invokeModel = excelMethodInvokeMap.get(bo.getMethodName());
        if (StringUtil.isEmpty(invokeModel)){
            return "";
        }
        Object bean = SpringUtil.getBean(invokeModel.getClassType());

        return "";
    }

}
