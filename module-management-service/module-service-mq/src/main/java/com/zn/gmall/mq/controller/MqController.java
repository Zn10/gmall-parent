package com.zn.gmall.mq.controller;

import com.zn.gmall.common.result.Result;
import com.zn.mq.service.RabbitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: gmall-parent
 * @description: 消息发送端
 * @author: Mr.Zhao
 * @create: 2024-05-13 22:06
 **/
@RestController
@RequestMapping("/mq")
public class MqController {
    @Autowired
    private RabbitService rabbitService;


    /**
     * 消息发送
     */
    //http://localhost:8282/mq/sendConfirm
    @GetMapping("sendConfirm")
    public Result sendConfirm() {
        rabbitService.sendMessage("exchange.confirm", "routing.confirm", "来人了，开始接客吧！");
        return Result.ok();
    }

}
