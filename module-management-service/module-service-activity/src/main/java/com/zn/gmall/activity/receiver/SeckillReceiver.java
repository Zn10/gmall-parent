package com.zn.gmall.activity.receiver;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbitmq.client.Channel;
import com.zn.gmall.activity.mapper.SeckillGoodsMapper;
import com.zn.gmall.common.constant.RedisConst;
import com.zn.gmall.common.util.DateUtil;
import com.zn.gmall.model.activity.SeckillGoods;
import com.zn.mq.constant.MqConst;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Package: com.zn.gmall.activity.receiver
 * Description:
 * Created on 2024-11-10 02:12
 *
 * @author zhaonian
 */
@Component
public class SeckillReceiver {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_TASK_1, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_TASK, type = ExchangeTypes.DIRECT, durable = "true"),
            key = {MqConst.ROUTING_TASK_1}
    ))
    public void importItemToRedis(Message message, Channel channel) throws IOException {
        //Log.info("importItemToRedis:");

        QueryWrapper<SeckillGoods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        queryWrapper.gt("stock_count", 0);
        //当天的秒杀商品导入缓存
        queryWrapper.eq("DATE_FORMAT(start_time,'%Y-%m-%d')", DateUtil.formatDate(new Date()));

        List<SeckillGoods> list = seckillGoodsMapper.selectList(queryWrapper);

        //把数据放在redis中
        for (SeckillGoods seckillGoods : list) {
            if (redisTemplate.boundHashOps(RedisConst.SECKILL_GOODS).hasKey(seckillGoods.getSkuId().toString()))
                continue;

            redisTemplate.boundHashOps(RedisConst.SECKILL_GOODS).put(seckillGoods.getSkuId().toString(), seckillGoods);

            //根据每一个商品的数量把商品按队列的形式放进redis中
            for (int i = 0; i < seckillGoods.getStockCount(); i++) {
                redisTemplate.boundListOps(RedisConst.SECKILL_STOCK_PREFIX + seckillGoods.getSkuId()).leftPush(seckillGoods.getSkuId().toString());
            }

            //通知添加与更新状态位，更新为开启
            redisTemplate.convertAndSend("seckillpush", seckillGoods.getSkuId() + ":1");
        }

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

}

