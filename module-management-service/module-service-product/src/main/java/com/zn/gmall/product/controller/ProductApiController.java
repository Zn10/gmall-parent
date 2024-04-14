package com.zn.gmall.product.controller;

import com.alibaba.fastjson.JSONObject;
import com.zn.gmall.common.result.Result;
import com.zn.gmall.model.product.*;
import com.zn.gmall.product.service.api.ManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/product")
public class ProductApiController {

    @Autowired
    private ManageService manageService;

    /**
     * 通过品牌Id 集合来查询数据
     *
     * @param tmId 品牌ID
     * @return BaseTrademark
     */
    @GetMapping("inner/getTrademark/{tmId}")
    public Result<BaseTrademark> getTrademark(@PathVariable("tmId") Long tmId) {
        BaseTrademark baseTrademark = manageService.getTrademarkByTmId(tmId);
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
    @GetMapping("inner/getAttrList/{skuId}")
    public Result<List<BaseAttrInfo>> getAttrList(@PathVariable("skuId") Long skuId) {
        List<BaseAttrInfo> attrList = manageService.getAttrList(skuId);
        return Result.ok(attrList);
    }

    /**
     * 根据spuId 获取海报数据
     *
     * @param spuId 商品SPUID
     * @return List<SpuPoster>
     */
    @GetMapping("inner/findSpuPosterBySpuId/{spuId}")
    public Result<List<SpuPoster>> findSpuPosterBySpuId(@PathVariable Long spuId) {
        List<SpuPoster> spuPosterBySpuId = manageService.findSpuPosterBySpuId(spuId);
        return Result.ok(spuPosterBySpuId);
    }

    /**
     * 根据spuId 查询map 集合属性
     *
     * @param spuId 商品SPUID
     */
    @GetMapping("inner/getSkuValueIdsMap/{spuId}")
    public Map getSkuValueIdsMap(@PathVariable("spuId") Long spuId) {
        return manageService.getSkuValueIdsMap(spuId);
    }

    /**
     * 根据spuId，skuId 查询销售属性集合
     *
     * @param skuId 商品SKUID
     * @param spuId 商品SPUID
     * @return List<SpuSaleAttr>
     */
    @GetMapping("inner/getSpuSaleAttrListCheckBySku/{skuId}/{spuId}")
    public Result<List<SpuSaleAttr>> getSpuSaleAttrListCheckBySku(@PathVariable("skuId") Long skuId, @PathVariable("spuId") Long spuId) {
        List<SpuSaleAttr> spuSaleAttrListCheckBySku = manageService.getSpuSaleAttrListCheckBySku(skuId, spuId);
        return Result.ok(spuSaleAttrListCheckBySku);
    }

    /**
     * 根据skuId获取sku信息
     *
     * @param skuId 商品SKUID
     * @return SkuInfo
     */
    @GetMapping("inner/getSkuInfo/{skuId}")
    public Result<SkuInfo> getAttrValueList(@PathVariable("skuId") Long skuId) {
        SkuInfo skuInfo = manageService.getSkuInfo(skuId);
        return Result.ok(skuInfo);
    }

    /**
     * 通过三级分类id查询分类信息
     *
     * @param category3Id 三级分类id
     * @return BaseCategoryView
     */
    @GetMapping("inner/getCategoryView/{category3Id}")
    public Result<BaseCategoryView> getCategoryView(@PathVariable("category3Id") Long category3Id) {
        BaseCategoryView baseCategoryView = manageService.getCategoryViewByCategory3Id(category3Id);
        return Result.ok(baseCategoryView);
    }
}

