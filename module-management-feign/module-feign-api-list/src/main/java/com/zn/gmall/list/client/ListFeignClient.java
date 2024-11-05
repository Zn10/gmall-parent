package com.zn.gmall.list.client;

import com.zn.gmall.common.result.Result;
import com.zn.gmall.model.list.SearchParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author: 赵念
 * @create-date: 2023/2/10/19:51
 */
@FeignClient(contextId = "ListFeignClient", value = "service-list", fallback = ListDegradeFeignClient.class)
public interface ListFeignClient {

    @PostMapping("/api/list/do/search")
    Result list(@RequestBody SearchParam searchParam) throws Throwable;

    @GetMapping("/api/list/inner/import/sku/to/elastic/search/{skuId}")
    Result<Void> importSkuToElasticSearch(@PathVariable("skuId") Long skuId);

    @GetMapping("/api/list/remove/goods/from/elastic/search/{skuId}")
    Result<Void> removeGoodsFromElasticSearch(@PathVariable("skuId") Long skuId);

    @GetMapping("/inner/incr/goods/hot/score/{skuId}")
    Result<Void> incrGoodsHotScore(@PathVariable("skuId") Long skuId);

}
