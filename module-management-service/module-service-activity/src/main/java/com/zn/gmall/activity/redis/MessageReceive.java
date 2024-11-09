package com.zn.gmall.activity.redis;

import com.zn.gmall.activity.util.CacheHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Package: com.zn.gmall.activity.redis
 * Description:
 * Created on 2024-11-10 02:16
 *
 * @author zhaonian
 */
@Component
@Slf4j
public class MessageReceive {

    /**
     * 接收消息的方法
     */
    public void receiveMessage(String message) {
        log.info("----------收到消息了message：{}", message);
        if (!StringUtils.isEmpty(message)) {
            /*
             消息格式
                skuId:0 表示没有商品
                skuId:1 表示有商品
             */
            // 因为传递过来的数据为 ""6:1""
            message = message.replaceAll("\"", "");
            String[] split = StringUtils.split(message, ":");
            if (split == null || split.length == 2) {
                CacheHelper.put(split[0], split[1]);
            }
        }
    }
}

