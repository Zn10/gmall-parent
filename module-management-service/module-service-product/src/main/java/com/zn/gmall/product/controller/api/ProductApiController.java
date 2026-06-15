package com.zn.gmall.product.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.zn.gmall.common.result.Result;
import com.zn.gmall.model.product.*;
import com.zn.gmall.product.service.api.BaseTrademarkService;
import com.zn.gmall.product.service.api.ManageService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product")
@Slf4j
@SuppressWarnings("all")
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
    @ApiOperation("通过tmId，查询品牌数据")
    @GetMapping("/getTrademark/{tmId}")
    public Result<BaseTrademark> getTrademarkById(@PathVariable("tmId") @NotNull(message = "品牌id不能为空") Long tmId) {
        log.info("通过品牌Id 集合来查询数据,{}", tmId);
        BaseTrademark baseTrademark = baseTrademarkService.getById(tmId);
        return Result.ok(baseTrademark);
    }

    /**
     * 获取全部分类信息
     */
    @ApiOperation("获取全部分类信息")
    @GetMapping("/getBaseCategoryList")
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
    @ApiOperation("通过skuId 集合来查询数据")
    @GetMapping("/inner/getAttrList/{skuId}")
    public Result<List<BaseAttrInfo>> getAttrList(@PathVariable("skuId") @NotNull(message = "SKUID不能为空") Long skuId) {
        log.info("通过skuId 集合来查询数据,{}", skuId);
        List<BaseAttrInfo> attrList = manageService.getAttrList(skuId);
        return Result.ok(attrList);
    }

    /**
     * 根据spuId 获取海报数据
     *
     * @param spuId 商品SPUID
     * @return List<SpuPoster>
     */
    @ApiOperation("根据spuId 获取海报数据")
    @GetMapping("/inner/findSpuPosterBySpuId/{spuId}")
    public Result<List<SpuPoster>> findSpuPosterBySpuId(@PathVariable @NotNull(message = "SPUID不能为空") Long spuId) {
        log.info("根据spuId 获取海报数据,{}", spuId);
        List<SpuPoster> spuPosterBySpuId = manageService.findSpuPosterBySpuId(spuId);
        return Result.ok(spuPosterBySpuId);
    }

    /**
     * 根据skuId 查询价格
     *
     * @param skuId 商品SKUID
     * @return BigDecimal
     */
    @ApiOperation("根据skuId 查询价格")
    @GetMapping("/inner/getPrice/{skuId}")
    public Result<BigDecimal> getSkuPrice(@PathVariable @NotNull(message = "SKUID不能为空") Long skuId) {
        log.info("根据skuId 获取价格,{}", skuId);
        BigDecimal price = manageService.getSkuPrice(skuId);
        return Result.ok(price);
    }

    /**
     * 根据spuId 查询map 集合属性
     *
     * @param spuId 商品SPUID
     */
    @ApiOperation("根据spuId 查询map 集合属性")
    @GetMapping("/inner/getSkuValueIdsMap/{spuId}")
    public Result<Map<Object, Object>> getSkuValueIdsMap(@PathVariable("spuId") @NotNull(message = "SPUID不能为空") Long spuId) {
        log.info("根据spuId 查询map 集合属性,{}", spuId);
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
    @ApiOperation("根据spuId，skuId 查询销售属性集合")
    @GetMapping("/inner/getSpuSaleAttrListCheckBySku/{skuId}/{spuId}")
    public Result<List<SpuSaleAttr>> getSpuSaleAttrListCheckBySku(@PathVariable("skuId") @NotNull(message = "SkUID不能为空") Long skuId, @PathVariable("spuId") @NotNull(message = "SPUID不能为空") Long spuId) {
        log.info("根据spuId，skuId 查询销售属性集合,{},{}", skuId, spuId);
        List<SpuSaleAttr> spuSaleAttrListCheckBySku = manageService.getSpuSaleAttrListCheckBySku(skuId, spuId);
        return Result.ok(spuSaleAttrListCheckBySku);
    }

    /**
     * 根据skuId获取sku信息
     *
     * @param skuId 商品SKUID
     * @return SkuInfo
     */
    @ApiOperation("根据skuId获取sku信息")
    @GetMapping("/inner/getSkuInfo/{skuId}")
    public Result<SkuInfo> getAttrValueList(@PathVariable("skuId") @NotNull(message = "SKUID不能为空") Long skuId) {
        log.info("根据skuId获取sku信息,{}", skuId);
        SkuInfo skuInfo = manageService.getSkuInfo(skuId);
        return Result.ok(skuInfo);
    }

    /**
     * 通过三级分类id查询分类信息
     *
     * @param category3Id 三级分类id
     * @return BaseCategoryView
     */
    @ApiOperation("通过三级分类id查询分类信息")
    @GetMapping("/inner/getCategoryView/{category3Id}")
    public Result<BaseCategoryView> getCategoryView(@PathVariable("category3Id") @NotNull(message = "三级分类id不能为空") Long category3Id) {
        log.info("通过三级分类id查询分类信息,{}", category3Id);
        BaseCategoryView baseCategoryView = manageService.getCategoryViewByCategory3Id(category3Id);
        return Result.ok(baseCategoryView);
    }
}

