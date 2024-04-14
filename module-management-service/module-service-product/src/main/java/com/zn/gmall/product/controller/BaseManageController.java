package com.zn.gmall.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zn.gmall.common.result.Result;
import com.zn.gmall.model.product.*;
import com.zn.gmall.product.service.api.ManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 针对平台属性控制
 */
@Api(tags = "商品基础属性接口")
@RestController
@RequestMapping("admin/product")
public class BaseManageController {

    @Resource
    private ManageService manageService;

    /**
     * SKU分页列表
     *
     * @param page  页码页数
     * @param limit 页码条数
     */
    @GetMapping("/list/{page}/{limit}")
    public Result<IPage<SkuInfo>> index(
            @PathVariable Long page,
            @PathVariable Long limit) {

        Page<SkuInfo> pageParam = new Page<>(page, limit);
        IPage<SkuInfo> pageModel = manageService.getPage(pageParam);
        return Result.ok(pageModel);
    }

    /**
     * 商品上架
     *
     * @param skuId 商品SKUID
     */
    @GetMapping("onSale/{skuId}")
    public Result<String> onSale(@PathVariable("skuId") Long skuId) {
        manageService.onSale(skuId);
        return Result.ok();
    }

    /**
     * 商品下架
     *
     * @param skuId 商品SKUID
     */
    @GetMapping("cancelSale/{skuId}")
    public Result<String> cancelSale(@PathVariable("skuId") Long skuId) {
        manageService.cancelSale(skuId);
        return Result.ok();
    }

    /**
     * 根据attrId 查询平台属性对象
     *
     * @param attrId 属性id
     * @return BaseAttrInfo
     */
    @ApiOperation("根据attrId 查询平台属性对象")
    @GetMapping("getAttrValueList/{attrId}")
    public Result<List<BaseAttrValue>> getAttrValueList(@PathVariable("attrId") Long attrId) {
        BaseAttrInfo baseAttrInfo = manageService.getAttrInfo(attrId);
        List<BaseAttrValue> baseAttrValueList = baseAttrInfo.getAttrValueList();
        return Result.ok(baseAttrValueList);
    }

    /**
     * 保存平台属性方法
     *
     * @param baseAttrInfo 平台属性实例
     */
    @ApiOperation("保存平台属性")
    @PostMapping("saveAttrInfo")
    public Result<String> saveAttrInfo(@RequestBody BaseAttrInfo baseAttrInfo) {
        // 前台数据都被封装到该对象中baseAttrInfo
        manageService.saveAttrInfo(baseAttrInfo);
        return Result.ok();
    }


    /**
     * 查询所有的一级分类信息
     *
     * @return List<BaseCategory1>
     */
    @ApiOperation("查询所有的一级分类信息")
    @GetMapping("getCategory1")
    public Result<List<BaseCategory1>> getCategory1() {
        List<BaseCategory1> baseCategory1List = manageService.getCategory1();
        return Result.ok(baseCategory1List);
    }

    /**
     * 根据一级分类Id 查询二级分类数据
     *
     * @param category1Id 一级分类Id
     * @return List<BaseCategory2>
     */
    @ApiOperation("根据一级分类Id 查询二级分类数据")
    @GetMapping("getCategory2/{category1Id}")
    public Result<List<BaseCategory2>> getCategory2(@PathVariable("category1Id") Long category1Id) {
        List<BaseCategory2> baseCategory2List = manageService.getCategory2(category1Id);
        return Result.ok(baseCategory2List);
    }

    /**
     * 根据二级分类Id 查询三级分类数据
     *
     * @param category2Id 二级分类Id
     * @return List<BaseCategory3>
     */
    @ApiOperation("根据二级分类Id 查询三级分类数据")
    @GetMapping("getCategory3/{category2Id}")
    public Result<List<BaseCategory3>> getCategory3(@PathVariable("category2Id") Long category2Id) {
        List<BaseCategory3> baseCategory3List = manageService.getCategory3(category2Id);
        return Result.ok(baseCategory3List);
    }

    /**
     * 根据分类Id 获取平台属性数据
     *
     * @param category1Id 一级分类Id
     * @param category2Id 二级分类Id
     * @param category3Id 三级分类Id
     * @return List<BaseAttrInfo>
     */
    @ApiOperation("根据分类Id 获取平台属性数据")
    @GetMapping("attrInfoList/{category1Id}/{category2Id}/{category3Id}")
    public Result<List<BaseAttrInfo>> attrInfoList(@PathVariable("category1Id") Long category1Id,
                                                   @PathVariable("category2Id") Long category2Id,
                                                   @PathVariable("category3Id") Long category3Id) {
        List<BaseAttrInfo> baseAttrInfoList = manageService.getAttrInfoList(category1Id, category2Id, category3Id);
        return Result.ok(baseAttrInfoList);
    }
}