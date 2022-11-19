package com.extra.light.common.model;

import lombok.Data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author 林树毅
 */
@Data
public class AnnotationModel<T extends Annotation> {
    private Class<?> clazz;
    private Method method;
    private T annotation;
}
