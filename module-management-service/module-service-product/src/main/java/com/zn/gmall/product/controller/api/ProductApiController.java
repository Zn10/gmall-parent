package com.zn.gmall.product.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.zn.gmall.common.result.Result;
import com.zn.gmall.model.product.*;
import com.zn.gmall.product.service.api.BaseTrademarkService;
import com.zn.gmall.product.service.api.ManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product/inner/")
@Slf4j
public class ProductApiController {

    @Autowired
    private ManageService manageService;
    @Autowired
    private BaseTrademarkService baseTrademarkService;

    /**
     * 通过品牌Id 集合来查询数据
     *
     * @param tmId 品牌ID
     * @return BaseTrademark
     */
    @GetMapping("getTrademark/{tmId}")
    public Result<BaseTrademark> getTrademark(@PathVariable("tmId") Long tmId) {
        log.info("通过品牌Id 集合来查询数据,{}", tmId);
        if (tmId == null) {
            return Result.<BaseTrademark>fail().message("品牌Id不能为空");
        }
        BaseTrademark baseTrademark = baseTrademarkService.getById(tmId);
        return Result.ok(baseTrademark);
    }

    /**
     * 获取全部分类信息
     */
    @GetMapping("getBaseCategoryList")
    public Result<List<JSONObject>> getBaseCategoryList() {
        List<JSONObject> list = manageService.getBaseCategoryList();
        return Result.ok(list);
    }


    /**
     * 通过skuId 集合来查询数据
     *
     * @param skuId 商品SKUID
     * @return List<BaseAttrInfo>
     */
    @GetMapping("getAttrList/{skuId}")
    public Result<List<BaseAttrInfo>> getAttrList(@PathVariable("skuId") Long skuId) {
        log.info("通过skuId 集合来查询数据,{}", skuId);
        if (skuId == null) {
            return Result.<List<BaseAttrInfo>>fail().message("skuId不能为空");
        }
        List<BaseAttrInfo> attrList = manageService.getAttrList(skuId);
        return Result.ok(attrList);
    }

    /**
     * 根据spuId 获取海报数据
     *
     * @param spuId 商品SPUID
     * @return List<SpuPoster>
     */
    @GetMapping("findSpuPosterBySpuId/{spuId}")
    public Result<List<SpuPoster>> findSpuPosterBySpuId(@PathVariable Long spuId) {
        log.info("根据spuId 获取海报数据,{}", spuId);
        if (spuId == null) {
            return Result.<List<SpuPoster>>fail().message("spuId不能为空");
        }
        List<SpuPoster> spuPosterBySpuId = manageService.findSpuPosterBySpuId(spuId);
        return Result.ok(spuPosterBySpuId);
    }

    @GetMapping("getPrice/{skuId}")
    public Result<BigDecimal> getSkuPrice(@PathVariable Long skuId) {
        log.info("根据skuId 获取价格,{}", skuId);
        if (skuId == null) {
            return Result.<BigDecimal>fail().message("skuId不能为空");
        }
        BigDecimal price = manageService.getSkuPrice(skuId);
        return Result.ok(price);
    }

    /**
     * 根据spuId 查询map 集合属性
     *
     * @param spuId 商品SPUID
     */
    @GetMapping("getSkuValueIdsMap/{spuId}")
    public Result<Map<Object, Object>> getSkuValueIdsMap(@PathVariable("spuId") Long spuId) {
        log.info("根据spuId 查询map 集合属性,{}", spuId);
        if (spuId == null) {
            return Result.<Map<Object, Object>>fail().message("spuId不能为空");
        }
        Map<Object, Object> skuValueIdsMap = manageService.getSkuValueIdsMap(spuId);
        return Result.ok(skuValueIdsMap);
    }

    /**
     * 根据spuId，skuId 查询销售属性集合
     *
     * @param skuId 商品SKUID
     * @param spuId 商品SPUID
     * @return List<SpuSaleAttr>
     */
    @GetMapping("getSpuSaleAttrListCheckBySku/{skuId}/{spuId}")
    public Result<List<SpuSaleAttr>> getSpuSaleAttrListCheckBySku(@PathVariable("skuId") Long skuId, @PathVariable("spuId") Long spuId) {
        log.info("根据spuId，skuId 查询销售属性集合,{},{}", skuId, spuId);
        if (skuId == null || spuId == null) {
            return Result.<List<SpuSaleAttr>>fail().message("skuId或spuId不能为空");
        }
        List<SpuSaleAttr> spuSaleAttrListCheckBySku = manageService.getSpuSaleAttrListCheckBySku(skuId, spuId);
        return Result.ok(spuSaleAttrListCheckBySku);
    }

    /**
     * 根据skuId获取sku信息
     *
     * @param skuId 商品SKUID
     * @return SkuInfo
     */
    @GetMapping("getSkuInfo/{skuId}")
    public Result<SkuInfo> getAttrValueList(@PathVariable("skuId") Long skuId) {
        log.info("根据skuId获取sku信息,{}", skuId);
        if (skuId == null) {
            return Result.<SkuInfo>fail().message("skuId不能为空");
        }
        SkuInfo skuInfo = manageService.getSkuInfo(skuId);
        return Result.ok(skuInfo);
    }

    /**
     * 通过三级分类id查询分类信息
     *
     * @param category3Id 三级分类id
     * @return BaseCategoryView
     */
    @GetMapping("getCategoryView/{category3Id}")
    public Result<BaseCategoryView> getCategoryView(@PathVariable("category3Id") Long category3Id) {
        log.info("通过三级分类id查询分类信息,{}", category3Id);
        if (category3Id == null) {
            return Result.<BaseCategoryView>fail().message("category3Id不能为空");
        }
        BaseCategoryView baseCategoryView = manageService.getCategoryViewByCategory3Id(category3Id);
        return Result.ok(baseCategoryView);
    }
}

