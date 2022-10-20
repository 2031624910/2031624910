package com.extra.light.record.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 林树毅
 */
@RestController
@RequestMapping("test")
public class TestController {

    @ApiOperation("hello")
    @GetMapping("hello")
    public String test() {
        return "hello";
    }
}
