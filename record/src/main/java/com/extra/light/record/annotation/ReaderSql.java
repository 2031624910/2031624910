package com.extra.light.record.annotation;

import java.lang.annotation.*;

/**
 * @author 林树毅
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ReaderSql {
}
