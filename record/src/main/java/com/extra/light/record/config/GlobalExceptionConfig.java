package com.extra.light.record.config;

import com.extra.light.record.exception.BusinessException;
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

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> businessException(BusinessException e) {
        return ResultConfig.failure(e.getCode(), "业务异常", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exception(Exception e) {
        return ResultConfig.failure("系统异常", e.getMessage());
    }


}
