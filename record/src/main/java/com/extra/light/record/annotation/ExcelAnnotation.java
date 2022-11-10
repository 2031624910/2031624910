package com.extra.light.record.annotation;

import java.lang.annotation.*;

/**
 * @author 林树毅
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface ExcelAnnotation {
    /**
     * 默认值为空字符串
     * 在isValue为true 的时候会使用该值为表的输出值
     * default extension name
     */
    String value() default "";

    /**
     * 是否使用value值代替ApiModelProperty字段值
     * @return
     */
    boolean isValue() default false;

    /**
     * 是否是是用于合计的字段，是，将对该字段进行合计
     * String类型会转换成BigDecimal进行计算
     * int long short byte 会进行加法计算
     * @return
     */
    boolean isTotal() default false;

    /**
     * 默认位数 String类型的BigDecimal位数
     * @return
     */
    int totalSize() default 2;
}