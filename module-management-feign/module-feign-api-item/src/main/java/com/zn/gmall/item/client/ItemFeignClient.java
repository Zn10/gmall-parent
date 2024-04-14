package com.zn.gmall.item.client;

/**
 * @author: 赵念
 * @create-date: 2023/2/10/14:23
 */

import com.zn.gmall.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(value = "service-item", fallback = ItemDegradeFeignClient.class)
public interface ItemFeignClient {

    /**
     * @param skuId
     * @return
     */
    @GetMapping("/api/item/{skuId}")
    Result<Map<String, Object>> getItem(@PathVariable("skuId") Long skuId);

}