package com.extra.light.common.util;

import com.extra.light.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 林树毅
 */
@Slf4j
public class ClassUtil {
    /**
     * 获取类里的set方法
     *
     * @return
     */
    public static <T> List<Method> getSetMethodList(Class<T> tClass) {
        Method[] declaredMethods = tClass.getMethods();
        List<Method> list = new ArrayList<>();
        if (StringUtil.isNotEmpty(declaredMethods)) {
            for (Method method : declaredMethods) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (StringUtil.isNotEmpty(parameterTypes) && parameterTypes.length == 1) {
                    //自动生成的set方法一般只有一个参数
                    String name = method.getName();
                    //set方法名字前缀为set
                    if (name.indexOf("set") == 0) {
                        list.add(method);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 通过mao和类型获取对象
     *
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T mapToClass(Class<T> tClass, Map<String, Object> map) {
        try {
            List<Method> setMethodList = getSetMethodList(tClass);
            if (StringUtil.isEmpty(setMethodList)) {
                throw new BusinessException("没有set方法");
            }
            //set方法循环，
            T t = tClass.newInstance();
            for (Method method : setMethodList) {
                String name = method.getName();
                String paramName = getSetParamName(name);
                if (StringUtil.isEmpty(paramName)) {
                    continue;
                }
                //获取方法类型, set方法里只有一个参数的为准
                Class<?>[] parameterTypes = method.getParameterTypes();
                //不为1则直接跳过
                if (parameterTypes.length != 1) {
                    continue;
                }
                Class<?> parameterType = parameterTypes[0];
                //获取map里的数据
                Object o = map.get(paramName);
                Class<?> aClass = o.getClass();
                System.out.println(aClass);
                if (parameterType.equals(aClass)) {
                    method.invoke(t, o);
                } else if (isMap(aClass) && isNotBasicType(parameterType) &&
                        isNotList(parameterType)) {
                    //如果不是基本类型，并且传入的类型是map类型
                    Map<String, Object> stringMap = getStringMap((Map) o);
                    if (StringUtil.isNotEmpty(stringMap)) {
                        Object o1 = mapToClass(parameterType, stringMap);
                        if (StringUtil.isNotEmpty(o1)) {
                            method.invoke(t, o1);
                        }
                    }
                }
            }
            return t;
        } catch (Exception e) {
            log.error("反射异常" + e.getMessage());
        }
        return null;
    }

    public static Map<String, Object> getStringMap(Map o) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isNotEmpty(o)) {
            for (Object o1 : o.keySet()) {
                if (o1 instanceof String) {
                    map.put((String) o1, o.get(o1));
                }
            }
        }
        return map;
    }

    public static boolean isNotMap(Class<?> type) {
        return !isMap(type);
    }

    public static boolean isMap(Class<?> type) {
        return type.equals(Map.class) || Map.class.isAssignableFrom(type);
    }

    public static boolean isNotList(Class<?> type) {
        return !isList(type);
    }

    public static boolean isList(Class<?> type) {
        return type.equals(List.class) || List.class.isAssignableFrom(type);
    }

    /**
     * 是否不是基本类型
     *
     * @param type
     * @return
     */
    public static boolean isNotBasicType(Class<?> type) {
        return !isBasicType(type);
    }

    /**
     * 是否是基本类型
     *
     * @param type
     * @return
     */
    public static boolean isBasicType(Class<?> type) {
        return type.equals(int.class) || type.equals(Integer.class) ||
                type.equals(float.class) || type.equals(Float.class) ||
                type.equals(double.class) || type.equals(Double.class) ||
                type.equals(long.class) || type.equals(Long.class) ||
                type.equals(byte.class) || type.equals(Byte.class) ||
                type.equals(short.class) || type.equals(Short.class) ||
                type.equals(boolean.class) || type.equals(Boolean.class) ||
                type.equals(char.class) || type.equals(Character.class) ||
                type.equals(String.class);
    }

    /**
     * 根据方法名获取参数名
     *
     * @param name
     * @return
     */
    public static String getSetParamName(String name) {
        if (StringUtil.isNotEmpty(name) && name.length() > 3) {
            //大于三的方法名
            String substring = name.substring(3);
            char c = substring.charAt(0);
            char c1 = StringUtil.toLowerCaseChar(c);
            if (c != c1) {
                substring = substring.replace(c, c1);
            }
            return substring;
        }
        return null;
    }
}
