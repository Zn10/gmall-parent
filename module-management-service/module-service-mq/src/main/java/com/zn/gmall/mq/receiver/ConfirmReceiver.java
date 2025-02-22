package com.zn.gmall.mq.receiver;

import com.rabbitmq.client.Channel;
import com.zn.gmall.mq.config.DeadLetterMqConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: gmall-parent
 * @description: 消息接收端
 * @author: Mr.Zhao
 * @create: 2024-05-13 22:22
 **/
@Configuration
@Component
@Slf4j
@SuppressWarnings("all")
public class ConfirmReceiver {

    @RabbitListener(queues = DeadLetterMqConfig.queue_dead_2)
    public void get(String msg) {
        log.info("Receive:{}", msg);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("Receive queue_dead_2: {} Delay rece.{}", sdf.format(new Date()), msg);
    }


    @SneakyThrows
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "queue.confirm", autoDelete = "false"),
            exchange = @Exchange(value = "exchange.confirm", autoDelete = "true"),
            key = {"routing.confirm"}))
    public void process(Message message, Channel channel) {
        log.info("RabbitListener:{}", new String(message.getBody()));
        // false 确认一个消息，true 批量确认
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
