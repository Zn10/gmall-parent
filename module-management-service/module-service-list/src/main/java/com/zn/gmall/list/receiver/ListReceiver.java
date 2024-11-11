package com.zn.gmall.list.receiver;

import com.rabbitmq.client.Channel;
import com.zn.gmall.list.service.api.SearchService;
import com.zn.mq.constant.MqConst;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Package: com.zn.gmall.list.receiver
 * Description:
 * Created on 2024-11-09 22:31
 *
 * @author zhaonian
 */
@Slf4j
@SuppressWarnings("all")
@Component
public class ListReceiver {
    @Autowired
    private SearchService searchService;

    //  开启消息监听 监听商品上架！
    @SneakyThrows
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_GOODS_UPPER, durable = "true", autoDelete = "false"),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_GOODS),
            key = {MqConst.ROUTING_GOODS_UPPER}
    ))
    public void upperGoodsToEs(Long skuId, Message message, Channel channel) {
        log.info("监听到商品上架消息，skuId:{}", skuId);
        //  获取到skuId,并判断
        try {
            if (skuId != null) {
                //  则调用商品上架的方法！
                searchService.upperGoods(skuId);
            }
        } catch (Exception e) {
            //  写入日志或将这条消息写入数据库，短信接口
            e.printStackTrace();
        }
        //  确认消费者消费消息！
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    //  编写商品下架代码
    @SneakyThrows
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_GOODS_LOWER, durable = "true", autoDelete = "false"),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_GOODS),
            key = {MqConst.ROUTING_GOODS_LOWER}
    ))
    public void lowerGoodsToEs(Long skuId, Message message, Channel channel) {
        log.info("监听到商品下架消息，skuId:{}", skuId);
        try {
            //  判断skuId
            if (skuId != null) {
                //  调用商品下架方法
                searchService.lowerGoods(skuId);
            }
        } catch (Exception e) {
            //  写入日志或将这条消息写入数据库，短信接口
            e.printStackTrace();
        }
        //  消息确认
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
