package com.zn.gmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages= {"com.zn.gmall"})
public class WebViewGuestMainType {

    public static void main(String[] args) {
        SpringApplication.run(WebViewGuestMainType.class, args);
    }

}
