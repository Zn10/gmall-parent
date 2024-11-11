package com.zn.gmall.mq.receiver;

import com.zn.gmall.mq.config.DelayedMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Package: com.zn.gmall.mq.receiver
 * Description:
 * Created on 2024-11-10 00:27
 *
 * @author zhaonian
 */
@Component
@Slf4j
@SuppressWarnings("all")
public class DelayReceiver {

    @RabbitListener(queues = DelayedMqConfig.queue_delay_1)
    public void get(String msg) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("Receive queue_delay_1: {} Delay rece.{}", sdf.format(new Date()), msg);
    }

}

