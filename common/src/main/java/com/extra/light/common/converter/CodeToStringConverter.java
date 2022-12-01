package com.extra.light.common.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.extra.light.common.annotation.ExcelEnum;
import com.extra.light.common.enums.TypeEnum;
import com.extra.light.common.util.EnumsUtil;
import com.extra.light.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

/**
 * @author 林树毅
 */
@Slf4j
public class CodeToStringConverter implements Converter<Object> {
    @Override
    public Class<?> supportJavaTypeKey() {
        return String.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Object convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        Field field = contentProperty.getField();
        //如果有注解标记这个字段是用哪个枚举类进行处理的
        if (StringUtil.isNotEmpty(field)) {
            //获取枚举类
            try {
                ExcelEnum annotation = field.getAnnotation(ExcelEnum.class);
                Class<? extends TypeEnum<?, ?>> enums = annotation.enums();
                if (!annotation.isReversal()) {
                    return EnumsUtil.getValueByCode(enums, cellData.getData());
                } else {
                    return EnumsUtil.getCodeByValue(enums, cellData.getData());
                }
            } catch (Exception e) {
                log.error("excel表解析异常 >> " + e.getMessage());
            }
        }
        return cellData.getData();
    }

    @Override
    public WriteCellData<?> convertToExcelData(Object value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        WriteCellData<String> stringCellData = new WriteCellData<>();
        stringCellData.setData(String.valueOf(value));
        Field field = contentProperty.getField();
        //如果有注解标记这个字段是用哪个枚举类进行处理的
        if (StringUtil.isNotEmpty(field)) {
            //获取枚举类
            try {
                ExcelEnum annotation = field.getAnnotation(ExcelEnum.class);
                Class<? extends TypeEnum<?, ?>> enums = annotation.enums();
                Object o = null;
                if (annotation.isReversal()) {
                    o = EnumsUtil.getValueByCode(enums, value);
                } else {
                    o = EnumsUtil.getCodeByValue(enums, value);
                }
                stringCellData.setData(String.valueOf(o));
            } catch (Exception e) {
                log.error("excel表解析异常 >> " + e.getMessage());
            }
        }
        return stringCellData;
    }

}
