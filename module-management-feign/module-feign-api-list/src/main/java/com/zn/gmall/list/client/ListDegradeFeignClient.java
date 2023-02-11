package com.zn.gmall.list.client;

import com.zn.gmall.common.result.Result;
import com.zn.gmall.model.list.SearchParam;
import org.springframework.stereotype.Component;

/**
 * @author: 赵念
 * @create-date: 2023/2/10/19:51
 */
@Component
public class ListDegradeFeignClient implements ListFeignClient {
    @Override
    public Result list(SearchParam searchParam) throws Throwable {
        return Result.<Void>fail().message("服务降级了！");
    }

    @Override
    public Result<Void> importSkuToElasticSearch(Long skuId) {
        return Result.<Void>fail().message("服务降级了！");
    }

    @Override
    public Result<Void> removeGoodsFromElasticSearch(Long skuId) {
        return Result.<Void>fail().message("服务降级了！");
    }

    @Override
    public Result<Void> incrGoodsHotScore(Long skuId) {
        return Result.<Void>fail().message("服务降级了！");
    }
}