package com.zn.gmall.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 购物车启动
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = "com.zn.gmall")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.zn.gmall")
public class ServiceCartApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCartApplication.class,args);
    }
}

