package com.zn.gmall.activity.receiver;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbitmq.client.Channel;
import com.zn.gmall.activity.mapper.SeckillGoodsMapper;
import com.zn.gmall.activity.service.api.SeckillGoodsService;
import com.zn.gmall.common.constant.RedisConst;
import com.zn.gmall.common.util.DateUtil;
import com.zn.gmall.model.activity.SeckillGoods;
import com.zn.gmall.model.activity.UserRecode;
import com.zn.gmall.mq.constant.MqConst;
import lombok.SneakyThrows;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
@SuppressWarnings("all")
@Component
public class SeckillReceiver {
    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private SeckillGoodsMapper seckillGoodsMapper;
    @Resource
    private SeckillGoodsService seckillGoodsService;

    //  监听删除消息！
    @SneakyThrows
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_TASK_18, durable = "true", autoDelete = "false"),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_TASK),
            key = {MqConst.ROUTING_TASK_18}
    ))
    public void deleteRedisData(Message message, Channel channel) {
        try {
            //  查询哪些商品是秒杀结束的！end_time , status = 1
            //  select * from seckill_goods where status = 1 and end_time < new Date();
            QueryWrapper<SeckillGoods> seckillGoodsQueryWrapper = new QueryWrapper<>();
            seckillGoodsQueryWrapper.eq("status", 1);
            seckillGoodsQueryWrapper.le("end_time", new Date());
            List<SeckillGoods> seckillGoodsList = seckillGoodsMapper.selectList(seckillGoodsQueryWrapper);

            //  对应将秒杀结束缓存中的数据删除！
            for (SeckillGoods seckillGoods : seckillGoodsList) {
                //  seckill:stock:46 删除库存对应key
                redisTemplate.delete(RedisConst.SECKILL_STOCK_PREFIX + seckillGoods.getSkuId());
                //  如果有多个秒杀商品的时候，
                //  redisTemplate.boundHashOps(RedisConst.SECKILL_GOODS).delete(seckillGoods.getSkuId());
            }
            //  删除预热等数据！ 主要针对于预热数据删除！ 我们项目只针对一个商品的秒杀！ 如果是多个秒杀商品，则不能这样直接删除预热秒杀商品的key！
            //  46 : 10:00 -- 10:30 | 47 : 18:10 -- 18:30
            redisTemplate.delete(RedisConst.SECKILL_GOODS);
            //  预下单
            redisTemplate.delete(RedisConst.SECKILL_ORDERS);
            //  删除真正下单数据
            redisTemplate.delete(RedisConst.SECKILL_ORDERS_USERS);

            //  修改数据库秒杀对象的状态！
            SeckillGoods seckillGoods = new SeckillGoods();
            //  1:表示审核通过 ，2：表示秒杀结束
            seckillGoods.setStatus("2");
            seckillGoodsMapper.update(seckillGoods, seckillGoodsQueryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //  手动确认消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }


    //  监听用户与商品的消息！
    @SneakyThrows
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_SECKILL_USER, durable = "true", autoDelete = "false"),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_SECKILL_USER),
            key = {MqConst.ROUTING_SECKILL_USER}
    ))
    public void seckillUser(UserRecode userRecode, Message message, Channel channel) {
        try {
            //  判断接收过来的数据
            if (userRecode != null) {
                //  预下单处理！
                seckillGoodsService.seckillOrder(userRecode.getSkuId(), userRecode.getUserId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //  手动确认
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }


    @SneakyThrows
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

