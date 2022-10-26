package com.extra.light.record.model;

import lombok.Data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author 林树毅
 */
@Data
public class AnnotationModel {
    private Class<?> clazz;
    private Method method;
    private Annotation annotation;
}
