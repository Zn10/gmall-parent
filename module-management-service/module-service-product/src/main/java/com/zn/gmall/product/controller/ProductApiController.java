package com.zn.gmall.product.controller;

import com.alibaba.fastjson.JSONObject;
import com.zn.gmall.common.result.Result;
import com.zn.gmall.model.product.*;
import com.zn.gmall.product.service.api.ManageService;
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

    @Resource
    private ManageService manageService;

    /**
     * 通过品牌Id 集合来查询数据
     *
     * @param tmId 品牌ID
     * @return BaseTrademark
     */
    @GetMapping("inner/getTrademark/{tmId}")
    public BaseTrademark getTrademark(@PathVariable("tmId") Long tmId) {
        return manageService.getTrademarkByTmId(tmId);
    }

    /**
     * 获取全部分类信息
     */
    @GetMapping("getBaseCategoryList")
    public Result getBaseCategoryList() {
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
    public List<BaseAttrInfo> getAttrList(@PathVariable("skuId") Long skuId) {
        return manageService.getAttrList(skuId);
    }

    /**
     * 根据spuId 获取海报数据
     *
     * @param spuId 商品SPUID
     * @return List<SpuPoster>
     */
    @GetMapping("inner/findSpuPosterBySpuId/{spuId}")
    public List<SpuPoster> findSpuPosterBySpuId(@PathVariable Long spuId) {
        return manageService.findSpuPosterBySpuId(spuId);
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
    public List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(@PathVariable("skuId") Long skuId, @PathVariable("spuId") Long spuId) {
        return manageService.getSpuSaleAttrListCheckBySku(skuId, spuId);
    }

    /**
     * 根据skuId获取sku信息
     *
     * @param skuId 商品SKUID
     * @return SkuInfo
     */
    @GetMapping("inner/getSkuInfo/{skuId}")
    public SkuInfo getAttrValueList(@PathVariable("skuId") Long skuId) {
        SkuInfo skuInfo = manageService.getSkuInfo(skuId);
        return skuInfo;
    }

    /**
     * 通过三级分类id查询分类信息
     *
     * @param category3Id 三级分类id
     * @return BaseCategoryView
     */
    @GetMapping("inner/getCategoryView/{category3Id}")
    public BaseCategoryView getCategoryView(@PathVariable("category3Id") Long category3Id) {
        return manageService.getCategoryViewByCategory3Id(category3Id);
    }
}

