package com.extra.light.record.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author 林树毅
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
@Component
public @interface ReaderSql {
}
