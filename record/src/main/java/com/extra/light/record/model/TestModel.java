package com.extra.light.record.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 林树毅
 */
@Component
@ConfigurationProperties(prefix = "aa")
public class TestModel {
    private String bb;

    public String getBb() {
        return bb;
    }

    public void setBb(String bb) {
        this.bb = bb;
    }
}
