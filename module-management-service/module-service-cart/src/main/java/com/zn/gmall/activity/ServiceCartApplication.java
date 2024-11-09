package com.zn.gmall.activity;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 购物车启动
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.zn.gmall")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.zn.gmall")
@Slf4j
@MapperScan("com.zn.gmall.*.mapper")
public class ServiceCartApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCartApplication.class, args);
        log.info("购物车服务启动成功");
    }
}

