package com.extra.light.common.util;

import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 林树毅
 */
@Component
public class FieldUtil {

    /**
     * 获取有相应注解的字段
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T, Y extends Annotation> List<Field> getFieldListByClazz(Class<T> clazz, Class<Y> annotation) {
        Field[] fields = clazz.getDeclaredFields();
        List<Field> fieldList = new ArrayList<>();
        for (Field field : fields) {
            Y[] annotationsByType = field.getAnnotationsByType(annotation);
            if (StringUtil.isNotEmpty(annotationsByType) && annotationsByType.length != 0) {
                fieldList.add(field);
            }
        }
        return fieldList;
    }


}
