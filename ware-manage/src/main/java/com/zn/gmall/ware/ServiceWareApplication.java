package com.zn.gmall.ware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.zn.gmall"})
@EnableDiscoveryClient
public class ServiceWareApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceWareApplication.class, args);
	}

}
