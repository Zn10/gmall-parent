package com.zn.gmall.task;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: 赵念
 * @create-date: 2023/2/11/14:06
 */
@SpringBootApplication
@ComponentScan({"com.zn.gmall"})
@EnableDiscoveryClient
@Slf4j
public class ServiceTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceTaskApplication.class, args);
        log.info("定时任务服务启动成功");
    }

}

