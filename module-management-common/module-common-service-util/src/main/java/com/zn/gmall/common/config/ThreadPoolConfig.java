package com.zn.gmall.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @author: 赵念
 * @create-date: 2023/2/10/17:02
 */
@Configuration
public class ThreadPoolConfig {

    // 加入 IOC 容器
    // ※Spring 调用该方法前，会先检查 IOC 容器中是否有这个对象，
    // 如果有，那么并不会真的执行方法体，所以能够保证单例。
    // 除非我们在这里另外进行了设置。
    @Bean
    public ThreadPoolExecutor getThreadPoolExecutor() {

        // [1]线程池的核心线程数
        int corePoolSize = 3;

        // [2]线程池的最大线程数
        int maximumPoolSize = 5;

        // [3]设置最大过期时间的数量
        long keepAliveTime = 10;

        // [4]设置最大过期时间的单位
        TimeUnit timeUnit = TimeUnit.SECONDS;

        // [5]存放等待中任务的阻塞队列
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(10);

        // [6]创建线程工厂
        ThreadFactory threadFactory = Executors.defaultThreadFactory();

        // [7]创建拒绝策略对象
        ThreadPoolExecutor.AbortPolicy abortPolicy = new ThreadPoolExecutor.AbortPolicy();

        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                timeUnit,
                queue,
                threadFactory,
                abortPolicy);
    }

}
