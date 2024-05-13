package com.zn.mq.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.amqp.rabbit.connection.CorrelationData;

/**
 * @program: gmall-parent
 * @description: 消息实体
 * @author: Mr.Zhao
 * @create: 2024-05-13 22:15
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class GmallCorrelationData extends CorrelationData {

    //  消息主体
    private Object message;
    //  交换机
    private String exchange;
    //  路由键
    private String routingKey;
    //  重试次数
    private int retryCount = 0;
    //  消息类型  是否是延迟消息
    private boolean isDelay = false;
    //  延迟时间
    private int delayTime = 10;
}

