package com.zn.gmall.item.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: gmall-parent
 * @description: 线程池配置类
 * @author: Mr.Zhao
 * @create: 2024-05-13 21:31
 **/
@Configuration
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolExecutor getThreadPoolExecutor() {

        return new ThreadPoolExecutor(50, 500, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10000));
    }

}
