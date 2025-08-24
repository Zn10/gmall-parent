package com.zn.gmall;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;

//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
//@EnableDiscoveryClient
//@EnableFeignClients(basePackages = {"com.zn.gmall"})
@SpringCloudApplication
@RefreshScope
public class WebViewGuestMainType {

    public static void main(String[] args) {
        SpringApplication.run(WebViewGuestMainType.class, args);
    }

}
