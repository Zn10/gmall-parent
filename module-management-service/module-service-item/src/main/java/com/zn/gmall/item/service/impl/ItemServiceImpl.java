package com.zn.gmall.item.service.impl;

import com.alibaba.fastjson.JSON;
import com.zn.gmall.common.result.Result;
import com.zn.gmall.item.service.api.ItemService;
import com.zn.gmall.list.client.ListFeignClient;
import com.zn.gmall.model.product.BaseCategoryView;
import com.zn.gmall.model.product.SkuInfo;
import com.zn.gmall.model.product.SpuSaleAttr;
import com.zn.gmall.product.client.ProductFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class ItemServiceImpl implements ItemService {

    @Resource
    private ProductFeignClient productFeignClient;


    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    private ListFeignClient listFeignClient;

    @Override
    public Map<String, Object> getBySkuId(Long skuId) {

        CompletableFuture<SkuInfo> futureSkuInfo = CompletableFuture.supplyAsync(() -> {
            // 第一步：根据 skuId 查询 SKU 基本信息和图片集合
            SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId).getData();

            return skuInfo;
        }, threadPoolExecutor);

        CompletableFuture<BaseCategoryView> futureCategoryView = futureSkuInfo.thenApply((SkuInfo skuInfo) -> {
            // 第二步：根据 category3Id 查询面包屑功能所需要的分类信息
            Long category3Id = skuInfo.getCategory3Id();
            BaseCategoryView categoryView = productFeignClient.getCategoryView(category3Id).getData();

            return categoryView;
        });

        CompletableFuture<List<SpuSaleAttr>> futureSaleAttrList = futureSkuInfo.thenApply((SkuInfo skuInfo) -> {
            // 第四步：根据 skuId 和 spuId 查询 SPU 下的销售属性（经过 SKU 标记的）
            Long spuId = skuInfo.getSpuId();
            List<SpuSaleAttr> saleAttrList = productFeignClient.getSpuSaleAttrListCheckBySku(skuId, spuId).getData();

            return saleAttrList;
        });

        CompletableFuture<BigDecimal> futurePrice = CompletableFuture.supplyAsync(() -> {
            // 第三步：根据 skuId 查询价格
            BigDecimal price = productFeignClient.getSkuPrice(skuId).getData();

            return price;
        }, threadPoolExecutor);

        CompletableFuture<String> futureValueIdsJSON = futureSkuInfo.thenApply((SkuInfo skuInfo) -> {
            // 第五步：根据 spuId 查询映射关系（从销售属性组合到 skuId）
            Map<String, Object> valueIdsMap = productFeignClient.getSkuValueIdsMap(skuInfo.getSpuId()).getData();

            // ※由于前端程序的要求，valueIdsMap 必须转换为 JSON 字符串
            String valuesIdsJSON = JSON.toJSONString(valueIdsMap);

            return valuesIdsJSON;
        });

        // ※附加功能：在异步任务中给 service-list 发送请求执行商品热度值的累加
        CompletableFuture.runAsync(()->{

            listFeignClient.incrGoodsHotScore(skuId);

        }, threadPoolExecutor);

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
            e.printStackTrace();
        }

        return finalDataMap;
    }
}