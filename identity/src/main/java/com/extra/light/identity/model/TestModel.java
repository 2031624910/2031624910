package com.extra.light.identity.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.extra.light.common.annotation.ExcelEnum;
import com.extra.light.common.converter.CodeToStringConverter;
import com.extra.light.common.enums.IdentityTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 林树毅
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestModel {
    @ExcelProperty("进出")
    private int inOut;
    @ExcelProperty("名字")
    private String name;
    @ExcelProperty(value = "类型" ,converter = CodeToStringConverter.class)
    @ExcelEnum(enums = IdentityTypeEnum.class)
    private String type;
    @ExcelProperty("模式")
    private String model;
}
