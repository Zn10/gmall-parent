package com.zn.gmall.product.client;

import com.alibaba.fastjson.JSONObject;
import com.zn.gmall.common.result.Result;
import com.zn.gmall.model.product.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class ProductDegradeFeignClient implements ProductFeignClient {

    @Override
    public Result<BaseTrademark> getTrademarkById(Long id) {
        Result<BaseTrademark> result = Result.fail();

        result.message("服务降级");

        return result;
    }

    @Override
    public Result<List<BaseAttrInfo>> getBaseAttrInfoBySkuId(Long skuId) {
        Result<List<BaseAttrInfo>> result = Result.fail();

        result.message("服务降级");

        return result;
    }

    @Override
    public Result<List<JSONObject>> getBaseCategoryList() {
        Result<List<JSONObject>> result = Result.fail();
        result.message("服务降级");
        return result;
    }

    @Override
    public Result<SkuInfo> getSkuInfo(Long skuId) {

        Result<SkuInfo> result = Result.fail();

        result.message("服务降级");

        return result;
    }

    @Override
    public Result<BaseCategoryView> getCategoryView(Long category3Id) {

        Result<BaseCategoryView> result = Result.fail();

        result.message("服务降级");

        return result;
    }

    @Override
    public Result<BigDecimal> getSkuPrice(Long skuId) {

        Result<BigDecimal> result = Result.fail();

        result.message("服务降级");

        return result;
    }

    @Override
    public Result<List<SpuSaleAttr>> getSpuSaleAttrListCheckBySku(Long skuId, Long spuId) {

        Result<List<SpuSaleAttr>> result = Result.fail();

        result.message("服务降级");

        return result;
    }

    @Override
    public Result<Map<String, Object>> getSkuValueIdsMap(Long spuId) {

        Result<Map<String, Object>> result = Result.fail();

        result.message("服务降级");

        return result;
    }

    @Override
    public Result<List<SpuPoster>> getSpuPosterBySpuId(Long spuId) {
        Result<List<SpuPoster>> result = Result.fail();

        result.message("服务降级");

        return result;
    }

    @Override
    public Result<List<BaseAttrInfo>> getAttrList(Long skuId) {
        Result<List<BaseAttrInfo>> result = Result.fail();

        result.message("服务降级");

        return result;
    }
}
