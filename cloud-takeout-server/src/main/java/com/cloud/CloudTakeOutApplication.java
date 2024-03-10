package com.cloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement //开启注解方式的事务管理
@EnableCaching//开启注解方式的数据缓存
@EnableScheduling//开启注解方式的定时任务
@Slf4j
public class CloudTakeOutApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudTakeOutApplication.class, args);
        log.info("service was started...");
    }
}
