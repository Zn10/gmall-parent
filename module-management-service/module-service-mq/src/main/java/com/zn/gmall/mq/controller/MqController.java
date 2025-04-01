package com.zn.gmall.mq.controller;

import com.zn.gmall.common.result.Result;
import com.zn.gmall.mq.config.DeadLetterMqConfig;
import com.zn.gmall.mq.config.DelayedMqConfig;
import com.zn.gmall.mq.service.RabbitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: gmall-parent
 * @description: 消息发送端
 * @author: Mr.Zhao
 * @create: 2024-05-13 22:06
 **/
@RestController
@RequestMapping("/mq")
@Slf4j
@SuppressWarnings("all")
public class MqController {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private RabbitService rabbitService;

    //  基于延迟插件的延迟消息
    @RequestMapping("sendDelay")
    public Result sendDelay() {
        this.rabbitService.sendDelayMessage(DelayedMqConfig.exchange_delay, DelayedMqConfig.routing_delay, "iuok", 3);
        return Result.ok();
    }


    /**
     * 消息发送
     */
    @RequestMapping("sendDeadLettle")
    public Result sendDeadLettle() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.rabbitTemplate.convertAndSend(DeadLetterMqConfig.exchange_dead, DeadLetterMqConfig.routing_dead_1, "ok");
        log.info("消息发送成功,时间:{}", sdf.format(new Date()));
        return Result.ok();
    }
}
