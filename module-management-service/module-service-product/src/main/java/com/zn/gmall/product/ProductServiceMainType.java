package com.zn.gmall.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(value = "com.zn.gmall")
@SpringBootApplication
@EnableDiscoveryClient
public class ProductServiceMainType {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceMainType.class, args);
    }

}
