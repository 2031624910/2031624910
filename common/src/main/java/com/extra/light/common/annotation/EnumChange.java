package com.extra.light.common.annotation;

import java.lang.annotation.*;

/**
 * @author 林树毅
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface EnumChange {
    int IN = 0;
    int OUT = 1;
    /**
     * 是否反过来查询, 默认正序，通过code获取value接口
     *
     * @return
     */
    boolean isReversal() default true;
}
