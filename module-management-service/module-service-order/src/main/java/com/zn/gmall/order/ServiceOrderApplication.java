package com.zn.gmall.order;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: 赵念
 * @create-date: 2023/2/11/14:06
 */
@SpringBootApplication
@ComponentScan({"com.zn.gmall"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.zn.gmall")
@Slf4j
public class ServiceOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceOrderApplication.class, args);
        log.info("订单服务启动成功");
    }
}

