package com.extra.light.identity.controller;

import com.extra.light.common.util.SpringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author 林树毅
 */
@Slf4j
@RestController
@Api(tags = "身份管理")
@RequestMapping("/identity")
public class IdentityController {

    @GetMapping("test")
    @ApiOperation("测试")
    public String test(@RequestParam("dataId") String dataId) {
        Object enumsList = SpringUtil.getBean("enumsList");
        return String.valueOf(enumsList);
    }

}
