package com.aliveseven.adminmanage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching//开启缓存功能
public class AdminManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminManageApplication.class, args);
    }

}
