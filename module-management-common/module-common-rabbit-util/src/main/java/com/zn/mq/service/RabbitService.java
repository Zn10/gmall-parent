package com.zn.mq.service;

import com.alibaba.fastjson.JSON;
import com.zn.mq.po.GmallCorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @program: gmall-parent
 * @description: 封装消息发送
 * @author: Mr.Zhao
 * @create: 2024-05-13 22:05
 **/
@Service
public class RabbitService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 发送消息
     *
     * @param exchange   交换机
     * @param routingKey 路由键
     * @param msg        消息
     */
//  封装一个发送消息的方法
    public Boolean sendMessage(String exchange, String routingKey, Object msg) {
        //  将发送的消息 赋值到 自定义的实体类
        GmallCorrelationData gmallCorrelationData = new GmallCorrelationData();
        //  声明一个correlationId的变量
        String correlationId = UUID.randomUUID().toString().replaceAll("-", "");
        gmallCorrelationData.setId(correlationId);
        gmallCorrelationData.setExchange(exchange);
        gmallCorrelationData.setRoutingKey(routingKey);
        gmallCorrelationData.setMessage(msg);

        //  发送消息的时候，将这个gmallCorrelationData 对象放入缓存。
        redisTemplate.opsForValue().set(correlationId, JSON.toJSONString(gmallCorrelationData), 10, TimeUnit.MINUTES);
        //  调用发送消息方法
        this.rabbitTemplate.convertAndSend(exchange, routingKey, msg);
        this.rabbitTemplate.convertAndSend(exchange, routingKey, msg, gmallCorrelationData);
        //  默认返回true
        return true;
    }


}
