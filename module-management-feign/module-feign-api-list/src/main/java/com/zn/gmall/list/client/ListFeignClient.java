package com.zn.gmall.list.client;

import com.zn.gmall.common.result.Result;
import com.zn.gmall.model.list.SearchParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author: 赵念
 * @create-date: 2023/2/10/19:51
 */
@FeignClient(contextId = "ListFeignClient", value = "service-list", fallback = ListDegradeFeignClient.class)
public interface ListFeignClient {

    @RequestMapping("/api/list/do/search")
    Result list(@RequestBody SearchParam searchParam) throws Throwable;

    @RequestMapping("/api/list/inner/import/sku/to/elastic/search/{skuId}")
    Result<Void> importSkuToElasticSearch(@PathVariable("skuId") Long skuId);

    @RequestMapping("/api/list/remove/goods/from/elastic/search/{skuId}")
    Result<Void> removeGoodsFromElasticSearch(@PathVariable("skuId") Long skuId);

    @RequestMapping("/inner/incr/goods/hot/score/{skuId}")
    Result<Void> incrGoodsHotScore(@PathVariable("skuId") Long skuId);

}
