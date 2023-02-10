package com.zn.gmall.item.controller;

import com.zn.gmall.common.result.Result;
import com.zn.gmall.item.service.api.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("api/item")
public class ItemApiController {
    @Resource
    private ItemService itemService;

    /**
     * 获取sku详情信息
     *
     * @param skuId
     */
    @GetMapping("{skuId}")
    public Result getItem(@PathVariable Long skuId) {
        Map<String, Object> result = itemService.getBySkuId(skuId);
        return Result.ok(result);
    }
}

