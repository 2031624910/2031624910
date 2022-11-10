package com.extra.light.record.config;

import lombok.Data;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author 林树毅
 */
public class ResultConfig {
    public static ResponseEntity<?> custom(int code, String msg, Object date, HttpStatus status) {
        ConfigNote configNote = new ConfigNote();
        configNote.setCode(code);
        configNote.setDate(date);
        configNote.setMsg(msg);
        return new ResponseEntity<>(configNote, status);
    }

    public static ResponseEntity<?> success(String msg, Object date) {
        return custom(200, msg, date, HttpStatus.OK);
    }

    public static ResponseEntity<?> failure(String msg, Object date) {
        return custom(500, msg, date, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Data
    private static class ConfigNote {
        private int code;
        private String msg;
        private Object date;
    }
}
