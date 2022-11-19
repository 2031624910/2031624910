package com.extra.light.identity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 林树毅
 */
@SpringBootApplication
public class IdentityApplication {
    public static void main(String[] args) {
        new SpringApplication(IdentityApplication.class).run(args);
    }
}
