package com.extra.light.record.model;

import lombok.Data;

/**
 * @author 林树毅
 */
@Data
public class ExcelMethodInvokeModel {
    /**
     * 方法名
     */
    private String methodName;
    private String fileName;
    private String[] sheetName;
    private int page;
    private int size;
    private Class<?>[] args;
    private Class<?> resultType;
    private Class<?> classType;
}
