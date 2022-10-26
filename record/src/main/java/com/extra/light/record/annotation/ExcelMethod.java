package com.extra.light.record.annotation;

import java.lang.annotation.*;

/**
 * @author 林树毅
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ExcelMethod {
    /**
     * 用于显示名字相关
     *
     * @return
     */
    String value() default "none";

    /**
     * 默认为空，俩者哪个有值，用哪个，都有值用自己,都美有值，都是none
     *
     * @return
     */
    String fileName() default "none";

    /**
     * 默认为fileName,有值则使用值
     *
     * @return
     */
    String sheetName() default "";

    /**
     * 必填项, 无默认值，必须写入一个，其值是用于到处的数据结构的类型
     *
     * @return
     */
    Class<?> clazz();
}
