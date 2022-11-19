package com.extra.light.common.model;

import lombok.Data;

/**
 * @author 林树毅
 */
@Data
public class ExcelMethodModel {
    private String fileName;
    private String sheetName;
    private String excelClassName;
    private String className;
    private String methodName;
    private String[] parameterTypes;
}
