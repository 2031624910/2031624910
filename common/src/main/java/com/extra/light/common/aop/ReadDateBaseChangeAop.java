package com.extra.light.common.aop;

import com.extra.light.common.db.DynamicDataSource;
import com.extra.light.common.enums.DateBaseType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author 林树毅
 */
@Component
@Aspect
public class ReadDateBaseChangeAop {
    /**
     * 读切入点
     */
    @Pointcut("@annotation(com.extra.light.common.annotation.ReaderSql)")
    public void reader() {
    }

    @Pointcut("@annotation(com.extra.light.common.annotation.WriterSql)")
    public void writer() {
    }

    @Before("reader()")
    public void before(JoinPoint joinPoint) {
        DynamicDataSource.setDateBaseType(DateBaseType.READER.name());
    }

    @Before("writer()")
    public void writerBefore(JoinPoint joinPoint) {
        DynamicDataSource.setDateBaseType(DateBaseType.WRITER.name());
    }

}
