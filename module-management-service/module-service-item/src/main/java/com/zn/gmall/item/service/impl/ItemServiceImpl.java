package com.zn.gmall.item.service.impl;

import com.alibaba.fastjson.JSON;
import com.zn.gmall.common.cache.GmallCache;
import com.zn.gmall.common.constant.RedisConst;
import com.zn.gmall.item.service.api.ItemService;
import com.zn.gmall.list.client.ListFeignClient;
import com.zn.gmall.model.product.BaseCategoryView;
import com.zn.gmall.model.product.SkuInfo;
import com.zn.gmall.model.product.SpuSaleAttr;
import com.zn.gmall.product.client.ProductFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    @Qualifier("productDegradeFeignClient")
    @Autowired
    private ProductFeignClient productFeignClient;
    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;
    @Qualifier("listDegradeFeignClient")
    @Autowired
    private ListFeignClient listFeignClient;

    /**
     * 获取sku详情信息
     *
     * @param skuId 商品SKUID
     */
    @Override
    @GmallCache(prefix = RedisConst.SKUKEY_PREFIX)
    public Map<String, Object> getBySkuId(Long skuId) {

        CompletableFuture<SkuInfo> futureSkuInfo = CompletableFuture.supplyAsync(() -> {
            // 第一步：根据 skuId 查询 SKU 基本信息和图片集合
            return productFeignClient.getSkuInfo(skuId).getData();
        }, threadPoolExecutor);

        CompletableFuture<BaseCategoryView> futureCategoryView = futureSkuInfo.thenApply((SkuInfo skuInfo) -> {
            // 第二步：根据 category3Id 查询面包屑功能所需要的分类信息
            Long category3Id = skuInfo.getCategory3Id();
            return productFeignClient.getCategoryView(category3Id).getData();
        });

        CompletableFuture<BigDecimal> futurePrice = CompletableFuture.supplyAsync(() -> {
            // 第三步：根据 skuId 查询价格
            return productFeignClient.getSkuPrice(skuId).getData();
        }, threadPoolExecutor);

        CompletableFuture<List<SpuSaleAttr>> futureSaleAttrList = futureSkuInfo.thenApply((SkuInfo skuInfo) -> {
            // 第四步：根据 skuId 和 spuId 查询 SPU 下的销售属性（经过 SKU 标记的）
            Long spuId = skuInfo.getSpuId();
            return productFeignClient.getSpuSaleAttrListCheckBySku(skuId, spuId).getData();
        });

        CompletableFuture<String> futureValueIdsJSON = futureSkuInfo.thenApply((SkuInfo skuInfo) -> {
            // 第五步：根据 spuId 查询映射关系（从销售属性组合到 skuId）
            Map<String, Object> valueIdsMap = productFeignClient.getSkuValueIdsMap(skuInfo.getSpuId()).getData();
            // ※由于前端程序的要求，valueIdsMap 必须转换为 JSON 字符串
            return JSON.toJSONString(valueIdsMap);
        });

        // ※附加功能：在异步任务中给 service-list 发送请求执行商品热度值的累加
        CompletableFuture.runAsync(() -> listFeignClient.incrGoodsHotScore(skuId), threadPoolExecutor);
        Map<String, Object> finalDataMap = null;

        try {
            // 第六步：组装数据，把上面查询到的数据存入 Map 集合
            // 这里的每一个属性都必须和页面上引用属性名的地方一致
            finalDataMap = new HashMap<>();

            SkuInfo skuInfo = futureSkuInfo.get();
            finalDataMap.put("skuInfo", skuInfo);

            BaseCategoryView categoryView = futureCategoryView.get();
            finalDataMap.put("categoryView", categoryView);

            BigDecimal price = futurePrice.get();
            finalDataMap.put("price", price);

            List<SpuSaleAttr> saleAttrList = futureSaleAttrList.get();
            finalDataMap.put("spuSaleAttrList", saleAttrList);

            String valuesIdsJSON = futureValueIdsJSON.get();
            finalDataMap.put("valuesSkuJson", valuesIdsJSON);
        } catch (Exception e) {
            log.error("exception message", e);
        }
        return finalDataMap;
    }
}