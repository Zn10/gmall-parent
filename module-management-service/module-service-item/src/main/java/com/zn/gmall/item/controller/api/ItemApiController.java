package com.zn.gmall.item.controller.api;

import com.zn.gmall.common.result.Result;
import com.zn.gmall.item.service.api.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/item")
@Slf4j
public class ItemApiController {
    @Autowired
    private ItemService itemService;

    /**
     * 获取sku详情信息
     *
     * @param skuId 商品SKUID
     */
    @GetMapping("{skuId}")
    public Result<Map<String, Object>> getItem(@PathVariable Long skuId) {
        log.info("获取商品详情信息，skuId:{}", skuId);
        if (skuId == null) {
            return Result.<Map<String, Object>>fail().message("skuid为空");
        }
        Map<String, Object> result = itemService.getBySkuId(skuId);
        return Result.ok(result);
    }
}

