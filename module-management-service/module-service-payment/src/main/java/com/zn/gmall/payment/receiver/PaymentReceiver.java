package com.zn.gmall.payment.receiver;

import com.rabbitmq.client.Channel;
import com.zn.gmall.mq.constant.MqConst;
import com.zn.gmall.payment.service.api.PaymentService;
import lombok.SneakyThrows;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Package: com.zn.gmall.payment.receiver
 * Description:
 * Created on 2024-11-10 01:40
 *
 * @author zhaonian
 */
@SuppressWarnings("all")
@Component
public class PaymentReceiver {

    @Resource
    private PaymentService paymentService;

    @SneakyThrows
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_PAYMENT_CLOSE, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_PAYMENT_CLOSE),
            key = {MqConst.ROUTING_PAYMENT_CLOSE}
    ))
    public void closePayment(Long orderId, Message message, Channel channel) {
        if (null != orderId) {
            // 关闭交易
            paymentService.closePayment(orderId);
        }
        // 手动ack
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}

