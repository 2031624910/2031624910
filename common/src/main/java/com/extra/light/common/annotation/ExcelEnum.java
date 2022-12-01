package com.extra.light.common.annotation;

import com.extra.light.common.enums.TypeEnum;

import java.lang.annotation.*;

/**
 * @author 林树毅
 * 标记文件所用的枚举类是哪个枚举类
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ExcelEnum {
    /**
     * 哪个枚举类
     *
     * @return
     */
    Class<? extends TypeEnum<?, ?>> enums();

    /**
     * 是否反过来查询, 默认正序，通过code获取value接口
     *
     * @return
     */
    boolean isReversal() default false;
}
