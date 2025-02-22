package com.zn.gmall.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)//取消数据源自动配置)
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.zn.gmall")
@EnableFeignClients(basePackages= {"com.zn.gmall"})
@Slf4j
public class ServiceMqApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceMqApplication.class, args);
        log.info("消息队列服务启动成功");
    }
}
