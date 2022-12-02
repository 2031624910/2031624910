package com.extra.light.common.util;

import com.extra.light.common.model.AnnotationModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author 林树毅
 */
@Component
@AllArgsConstructor
@Slf4j
public class AnnotationUtil {
    private final ResourceLoader resourceLoader;

    /**
     * 获取指定包下所有添加了执行注解的方法信息
     *
     * @param classPath          包名
     * @param tagAnnotationClass 指定注解类型
     * @param <T>                注解实例
     * @return 内容
     * @throws Exception 没啥东西
     */
    public <T extends Annotation> List<AnnotationModel<T>> getAllAddTagAnnotationUrl(String classPath, Class<T> tagAnnotationClass) {
        try {
            List<AnnotationModel<T>> resList = new ArrayList<>();
            ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
            MetadataReaderFactory metaReader = new CachingMetadataReaderFactory(resourceLoader);
            Resource[] resources = resolver.getResources(classPath);
            for (Resource r : resources) {
                try {
                    MetadataReader reader = metaReader.getMetadataReader(r);
                    resList = resolveClass(reader, resList, tagAnnotationClass);
                } catch (Exception e) {
                    log.error("根据注解查询所有方法错误 >> " + e.getMessage());
                }
            }
            return resList;
        } catch (Exception e) {
            log.error("根据注解查询所有方法异常 >> " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * 根据结构返回列表
     *
     * @param reader
     * @param resList
     * @param tagAnnotationClass
     * @param <T>
     * @return
     * @throws Exception
     */
    private <T extends Annotation> List<AnnotationModel<T>> resolveClass(
            MetadataReader reader, List<AnnotationModel<T>> resList, Class<T> tagAnnotationClass)
            throws Exception {
        String tagAnnotationClassCanonicalName = tagAnnotationClass.getCanonicalName();
        //获取注解元数据
        AnnotationMetadata annotationMetadata = reader.getAnnotationMetadata();
        Set<MethodMetadata> annotatedMethods = annotationMetadata.getAnnotatedMethods(tagAnnotationClassCanonicalName);
        if (StringUtil.isEmpty(annotatedMethods)) {
            return resList;
        }
        for (MethodMetadata next : annotatedMethods) {
            //根据类，保存各种内容
            String className = next.getDeclaringClassName();
            Class<?> aClass = Class.forName(className);
            Method[] methods = aClass.getMethods();
            String methodName = next.getMethodName();
            if (StringUtil.isNotEmpty(methods)) {
                for (Method method : methods) {
                    String name = method.getName();
                    if (methodName.equals(name)) {
                        //确认这个为有用的类
                        T annotation = method.getAnnotation(tagAnnotationClass);
                        if (StringUtil.isNotEmpty(annotation)) {
                            AnnotationModel<T> model = new AnnotationModel<>();
                            model.setAnnotation(annotation);
                            model.setClazz(aClass);
                            model.setMethod(method);
                            resList.add(model);
                            break;
                        }
                    }
                }
            }
        }
        return resList;
    }
}
