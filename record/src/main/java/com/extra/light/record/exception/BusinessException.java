package com.extra.light.record.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 林树毅
 */
@AllArgsConstructor
public class BusinessException extends RuntimeException{
    @Getter
    @Setter
    private String msg;
    @Getter
    @Setter
    private int code;
    public BusinessException(String msg){
        this.msg = msg;
        this.code = 500;
    }
}
