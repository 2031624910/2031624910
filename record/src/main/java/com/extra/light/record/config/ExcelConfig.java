package com.extra.light.record.config;

import com.extra.light.record.annotation.ExcelMethod;
import com.extra.light.record.model.AnnotationModel;
import com.extra.light.record.model.ExcelMethodInvokeModel;
import com.extra.light.record.util.AnnotationUtil;
import com.extra.light.record.util.SpringUtil;
import com.extra.light.record.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author 林树毅
 */
@Configuration
@Slf4j
public class ExcelConfig {

    @Bean
    public Map<String, ExcelMethodInvokeModel> excelMethodInvokeMap(AnnotationUtil annotationUtil) {
        String classPath = "classpath*:com/extra/light/record/service/*.class";
        log.info("开始查询ExcelMethod列表");
        List<AnnotationModel<ExcelMethod>> list = annotationUtil.getAllAddTagAnnotationUrl(classPath, ExcelMethod.class);
        log.info("查询结束, 拥有注解的方法个数为" + (StringUtil.isNotEmpty(list) ? list.size() : 0));
        //获取用了该注解的方法和类,以及对应的注解信息
        Map<String, ExcelMethodInvokeModel> map = new HashMap<>();
        if (StringUtil.isNotEmpty(list)) {
            for (AnnotationModel<ExcelMethod> model : list) {
                Class<?> clazz = model.getClazz();
                Method method = model.getMethod();
                ExcelMethod annotation = model.getAnnotation();
                String methodName = method.getName();
                ExcelMethodInvokeModel invokeModel = new ExcelMethodInvokeModel();
                //填入对应的值
                invokeModel.setClassType(clazz);
                invokeModel.setResultType(annotation.clazz());
                invokeModel.setArgs(annotation.args());
                invokeModel.setPage(annotation.page());
                invokeModel.setSize(annotation.size());
                invokeModel.setMethodName(methodName);
                invokeModel.setFileName(annotation.fileName());
                String s = annotation.sheetName();
                String[] sheetName = new String[]{s};
                invokeModel.setSheetName(sheetName);
                map.put(methodName, invokeModel);
            }
        }
        return map;
    }
}
