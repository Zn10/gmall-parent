package com.zn.mq.common.config;

import com.alibaba.fastjson.JSON;
import com.zn.mq.po.GmallCorrelationData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @program: gmall-parent
 * @description: 消息发送确认
 * @author: Mr.Zhao
 * @create: 2024-05-13 22:04
 **/
@Component
@Slf4j
public class MQProducerAckConfig implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    // 修饰一个非静态的void（）方法,在服务器加载Servlet的时候运行，并且只会被服务器执行一次在构造函数之后执行，init（）方法之前执行。
    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);            //指定 ConfirmCallback
        rabbitTemplate.setReturnCallback(this);             //指定 ReturnCallback
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            log.info("消息发送成功：{}", JSON.toJSONString(correlationData));
        } else {
            log.info("消息发送失败：{} 数据：{}", cause, JSON.toJSONString(correlationData));
        }
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        // 反序列化对象输出
        log.info("消息主体: {}", new String(message.getBody()));
        log.info("应答码: {}", replyCode);
        log.info("描述：{}", replyText);
        log.info("消息使用的交换器 exchange : {}", exchange);
        log.info("消息使用的路由键 routing : {}", routingKey);
        //  获取这个CorrelationData对象的Id  spring_returned_message_correlation
        String correlationDataId = (String) message.getMessageProperties().getHeaders().get("spring_returned_message_correlation");
        //  因为在发送消息的时候，已经将数据存储到缓存，通过 correlationDataId 来获取缓存的数据
        String strJson = (String) this.redisTemplate.opsForValue().get(correlationDataId);
        //  消息没有到队列的时候，则会调用重试发送方法
        GmallCorrelationData gmallCorrelationData = JSON.parseObject(strJson, GmallCorrelationData.class);
        //  调用方法  gmallCorrelationData 这对象中，至少的有，交换机，路由键，消息等内容.
        this.retrySendMsg(gmallCorrelationData);
    }

    /**
     * 重试发送方法
     *
     * @param correlationData 父类对象  它下面还有个子类对象 GmallCorrelationData
     */
    private void retrySendMsg(CorrelationData correlationData) {
        //  数据类型转换  统一转换为子类处理
        GmallCorrelationData gmallCorrelationData = (GmallCorrelationData) correlationData;
        //  获取到重试次数 初始值 0
        int retryCount = gmallCorrelationData.getRetryCount();
        //  判断
        if (retryCount >= 3) {
            //  不需要重试了
            log.error("重试次数已到，发送消息失败:{}", JSON.toJSONString(gmallCorrelationData));
        } else {
            //  变量更新
            retryCount += 1;
            //  重新赋值重试次数 第一次重试 0->1 1->2 2->3
            gmallCorrelationData.setRetryCount(retryCount);
            log.error("重试次数：\t{}", retryCount);

            //  更新缓存中的数据
            this.redisTemplate.opsForValue().set(gmallCorrelationData.getId(), JSON.toJSONString(gmallCorrelationData), 10, TimeUnit.MINUTES);
            if (gmallCorrelationData.isDelay()) {
                //  属于延迟消息
                this.rabbitTemplate.convertAndSend(gmallCorrelationData.getExchange(), gmallCorrelationData.getRoutingKey(), gmallCorrelationData.getMessage(), message -> {
                    //  设置延迟时间
                    message.getMessageProperties().setDelay(gmallCorrelationData.getDelayTime() * 1000);
                    return message;
                }, gmallCorrelationData);
            } else {
                //  调用发送消息方法 表示发送普通消息  发送消息的时候，不能调用 new RabbitService().sendMsg() 这个方法
                this.rabbitTemplate.convertAndSend(gmallCorrelationData.getExchange(), gmallCorrelationData.getRoutingKey(), gmallCorrelationData.getMessage(), gmallCorrelationData);
            }
        }
    }
}
