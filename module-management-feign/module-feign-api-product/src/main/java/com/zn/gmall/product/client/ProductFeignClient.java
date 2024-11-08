package com.zn.gmall.product.client;

import com.alibaba.fastjson.JSONObject;
import com.zn.gmall.common.result.Result;
import com.zn.gmall.model.product.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@FeignClient(contextId = "ProductFeignClient", value = "service-product", fallback = ProductDegradeFeignClient.class)
public interface ProductFeignClient {

    /**
     * 根据品牌id查询品牌
     *
     * @param tmId 品牌id
     * @return BaseTrademark
     */
    @GetMapping("/api/product/getTrademark/{tmId}")
    Result<BaseTrademark> getTrademarkById(@PathVariable("tmId") Long tmId);

    /**
     * 根据skuId查询平台属性集合
     *
     * @param skuId 商品SKUID
     * @return List<BaseAttrInfo>
     */
    @GetMapping("/api/product/inner/getAttrList/{skuId}")
    Result<List<BaseAttrInfo>> getBaseAttrInfoBySkuId(@PathVariable("skuId") Long skuId);

    /**
     * 获取全部分类信息
     *
     * @return List<JSONObject>
     */
    @GetMapping("/api/product/getBaseCategoryList")
    Result<List<JSONObject>> getBaseCategoryList();


    /**
     * 根据skuId获取sku信息
     *
     * @param skuId 商品SKUID
     * @return SkuInfo
     */
    @GetMapping("/api/product/inner/getSkuInfo/{skuId}")
    Result<SkuInfo> getSkuInfo(@PathVariable("skuId") Long skuId);


    /**
     * 通过三级分类id查询分类信息
     *
     * @param category3Id 三级分类id
     * @return BaseCategoryView
     */
    @GetMapping("/api/product/inner/getCategoryView/{category3Id}")
    Result<BaseCategoryView> getCategoryView(@PathVariable("category3Id") Long category3Id);

    /**
     * 获取sku最新价格
     *
     * @param skuId 商品SKUID
     * @return BigDecimal
     */
    @GetMapping("/api/product/inner/getSkuPrice/{skuId}")
    Result<BigDecimal> getSkuPrice(@PathVariable(value = "skuId") Long skuId);

    /**
     * 根据spuId，skuId 查询销售属性集合
     *
     * @param skuId 商品SKUID
     * @param spuId 商品SPUID
     * @return List<SpuSaleAttr>
     */
    @GetMapping("/api/product/inner/getSpuSaleAttrListCheckBySku/{skuId}/{spuId}")
    Result<List<SpuSaleAttr>> getSpuSaleAttrListCheckBySku(@PathVariable("skuId") Long skuId, @PathVariable("spuId") Long spuId);

    /**
     * 根据spuId 查询map 集合属性
     *
     * @param spuId 商品SPUID
     * @return Map<String, Object>
     */
    @GetMapping("/api/product/inner/getSkuValueIdsMap/{spuId}")
    Result<Map<String, Object>> getSkuValueIdsMap(@PathVariable("spuId") Long spuId);

    /**
     * 根据spuId 获取海报数据
     *
     * @param spuId 商品SPUID
     * @return List<SpuPoster>
     */
    @GetMapping("/api/product/inner/findSpuPosterBySpuId/{spuId}")
    Result<List<SpuPoster>> getSpuPosterBySpuId(@PathVariable("spuId") Long spuId);

    /**
     * 通过skuId 集合来查询数据
     *
     * @param skuId 商品SKUID
     * @return List<BaseAttrInfo>
     */
    @GetMapping("/api/product/inner/getAttrList/{skuId}")
    Result<List<BaseAttrInfo>> getAttrList(@PathVariable("skuId") Long skuId);
}
