package com.extra.light.common.util;

import com.extra.light.common.annotation.EnumChange;
import com.extra.light.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author 林树毅
 */
@Slf4j
@Component
public class EnumsUtil {

    public static Object getCodeByValue(Class<?> clazz, Object... objects) throws Exception {
        //通过注解获取，通过名字获取
        Method method = getEnumMethod(clazz, EnumChange.IN);
        if (StringUtil.isEmpty(method)) {
            throw new BusinessException("方法调用失败");
        }
        Class<?>[] parameterTypes = method.getParameterTypes();
        argVerify(parameterTypes, objects);
        return method.invoke(null, objects);
    }


    public static Object getValueByCode(Class<?> clazz, Object... objects) throws Exception {
        Method method = getEnumMethod(clazz, EnumChange.OUT);
        if (StringUtil.isEmpty(method)) {
            throw new BusinessException("方法调用失败");
        }
        Class<?>[] parameterTypes = method.getParameterTypes();
        argVerify(parameterTypes, objects);
        return method.invoke(null, objects);
    }

    private static void argVerify(Class<?>[] parameterTypes, Object[] objects) {
        if (parameterTypes == null || objects == null) {
            throw new BusinessException("无参数");
        }
        int length = parameterTypes.length;
        int ol = objects.length;
        if (length != ol) {
            throw new BusinessException("参数异常");
        }
    }


    private static Method getEnumMethod(Class<?> clazz, int type) {
        //根据方法注解找，没有根据方法名字找
        Method[] declaredMethods = clazz.getMethods();
        if (StringUtil.isNotEmpty(declaredMethods)) {
            for (Method method : declaredMethods) {
                EnumChange annotation = method.getAnnotation(EnumChange.class);
                int modifiers = method.getModifiers();
                boolean aStatic = Modifier.isStatic(modifiers);
                //如果是静态方法则才进入
                if (aStatic) {
                    if (StringUtil.isNotEmpty(annotation)) {
                        boolean reversal = annotation.isReversal();
                        if (type == EnumChange.IN && !reversal) {
                            return method;
                        } else if (type == EnumChange.OUT && reversal) {
                            return method;
                        }
                    }
                }
            }
        }
        return null;
    }
}
