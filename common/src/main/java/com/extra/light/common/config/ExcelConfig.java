package com.extra.light.common.config;

import com.extra.light.common.annotation.ExcelMethod;
import com.extra.light.common.model.AnnotationModel;
import com.extra.light.common.model.ExcelMethodInvokeModel;
import com.extra.light.common.util.AnnotationUtil;
import com.extra.light.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author 林树毅
 */
@Configuration
@Slf4j
public class ExcelConfig {
    private static final String NONE = "none";

    @Value("${excel.export.classPath}")
    private String classPath;


    @Bean
    public Map<String, ExcelMethodInvokeModel> excelMethodInvokeMap(AnnotationUtil annotationUtil) {
        Map<String, ExcelMethodInvokeModel> map = new HashMap<>();
        if (StringUtil.isNotEmpty(classPath)) {
            log.info("开始查询ExcelMethod列表");
            List<AnnotationModel<ExcelMethod>> list = annotationUtil.getAllAddTagAnnotationUrl(classPath, ExcelMethod.class);
            log.info("查询结束, 拥有注解的方法个数为" + (StringUtil.isNotEmpty(list) ? list.size() : 0));
            //获取用了该注解的方法和类,以及对应的注解信息
            if (StringUtil.isNotEmpty(list)) {
                for (AnnotationModel<ExcelMethod> model : list) {
                    Class<?> clazz = model.getClazz();
                    Method method = model.getMethod();
                    ExcelMethod annotation = model.getAnnotation();
                    String value = annotation.value();
                    String methodName = method.getName();
                    //获取列表标记
                    String excelTarget = getExcelTarget(methodName, value);
                    //根据列表标记，查询是否是同一个导出列表
                    ExcelMethodInvokeModel invokeModel = null;
                    if (map.containsKey(excelTarget)) {
                        //如果存在
                        invokeModel = map.get(excelTarget);
                    } else {
                        invokeModel = new ExcelMethodInvokeModel();
                        invokeModel.setFileName(annotation.fileName());
                        invokeModel.setExcelTarget(excelTarget);
                    }
                    String sheetName = getSheetName(annotation.fileName(), annotation.sheetName());
                    if (sheetName == null) {
                        continue;
                    }
                    invokeModel.addNote(methodName, sheetName, annotation.page(), annotation.size(), annotation.args(), annotation.clazz(), clazz);
                    //填入对应的值
                    map.put(excelTarget, invokeModel);
                }
            }
        }
        return map;
    }

    /**
     * 如果sheetName不设置，则选择用fileName作为sheetName
     *
     * @param fileName
     * @param sheetName
     * @return
     */
    private String getSheetName(String fileName, String sheetName) {
        if (sheetName != null) {
            if (NONE.equalsIgnoreCase(sheetName)) {
                return fileName;
            } else {
                return sheetName;
            }
        }
        return null;
    }

    private String getExcelTarget(String methodName, String value) {
        if (NONE.equalsIgnoreCase(value)) {
            return methodName;
        }
        return value;
    }
}
