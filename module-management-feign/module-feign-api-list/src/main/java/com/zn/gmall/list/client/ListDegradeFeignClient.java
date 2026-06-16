package com.zn.gmall.list.client;

import com.zn.gmall.common.result.Result;
import com.zn.gmall.model.list.SearchParam;
import com.zn.gmall.model.list.vo.SearchResponseVo;
import org.springframework.stereotype.Component;

/**
 * @author: 赵念
 * @create-date: 2023/2/10/19:51
 */
@Component
public class ListDegradeFeignClient implements ListFeignClient {
    @Override
    public Result<SearchResponseVo> list(SearchParam searchParam) throws Throwable {
        Result result = Result.fail();
        result.message("服务降级了");
        return result;
    }

    @Override
    public Result<Void> importSkuToElasticSearch(Long skuId) {
        Result<Void> result = Result.fail();
        result.message("服务降级了");
        return result;
    }

    @Override
    public Result<Void> removeGoodsFromElasticSearch(Long skuId) {
        Result<Void> result = Result.fail();
        result.message("服务降级了");
        return result;
    }

    @Override
    public Result<Void> incrGoodsHotScore(Long skuId) {
        Result<Void> result = Result.fail();
        result.message("服务降级了");
        return result;
    }
}
