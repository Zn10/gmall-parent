package com.zn.gmall.list;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: 赵念
 * @create-date: 2023/2/10/19:31
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan({"com.zn.gmall"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages= {"com.zn.gmall"})
public class ListServiceMainType {
    public static void main(String[] args) {

        SpringApplication.run(ListServiceMainType.class,args);
    }
}
