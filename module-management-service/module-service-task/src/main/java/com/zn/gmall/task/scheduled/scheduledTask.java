package com.zn.gmall.task.scheduled;

import com.zn.mq.constant.MqConst;
import com.zn.mq.service.RabbitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Package: com.zn.gmall.task.scheduled
 * Description:
 * Created on 2024-11-10 02:09
 *
 * @author zhaonian
 */
@Component
@EnableScheduling
@Slf4j
public class scheduledTask {
    @Autowired
    private RabbitService rabbitService;

    /**
     * 每天凌晨1点执行
     */
    //@Scheduled(cron = "0/30 * * * * ?")
    @Scheduled(cron = "0 0 1 * * ?")
    public void task1() {
        rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_TASK, MqConst.ROUTING_TASK_1, "");
    }

}
