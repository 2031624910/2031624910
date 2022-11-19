package com.extra.light.common.annotation;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.*;

/**
 * @author 林树毅
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ExcelMethod {
    /**
     * 用于显示名字相关, 默认none的话
     * 文件名相同的俩个方法会被标明为一个导出接口, 名字遵循该原则
     * 如果名字不为none，则相同的value值，会被标记为一个导出列表，
     *
     * @return
     */
    String value() default "none";

    /**
     * 默认为空，俩者哪个有值，用哪个，都有值用自己,都没有值，则都是none
     *
     * @return
     */
    String fileName();

    /**
     * 默认为fileName,有值则使用值
     *
     * @return
     */
    String sheetName() default "none";

    /**
     * 必填项, 无默认值，必须写入一个，其值是用于到处的数据结构的类型
     * 导出结构类型
     *
     * @return
     */
    Class<?> clazz();

    /**
     * 方法的参数类型列表
     * 必填, 需要填入参数的类型列表
     * 例如：{Integer.class, Integer.class, String.class, HttpServletRequest.class}
     *
     * @return
     */
    Class[] args() default {int.class, int.class, String.class, HttpServletRequest.class};

    /**
     * page 的位置
     * 默认 1
     *
     * @return
     */
    int page() default 1;

    /**
     * size 的位置
     * 默认 2
     *
     * @return
     */
    int size() default 2;
}
