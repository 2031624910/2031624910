package com.extra.light.record.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 *
 * @author 林树毅
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionConfig {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exception(Exception e) {
        return ResultConfig.failure("系统异常", e.getMessage());
    }
}
