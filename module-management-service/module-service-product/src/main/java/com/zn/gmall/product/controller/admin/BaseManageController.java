package com.zn.gmall.product.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zn.gmall.common.result.Result;
import com.zn.gmall.model.product.*;
import com.zn.gmall.product.service.api.ManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 针对平台属性控制
 */
@Api(tags = "商品基础属性接口")
@RestController
@RequestMapping("admin/product")
@Slf4j
@SuppressWarnings("all")
public class BaseManageController {

    @Resource
    private ManageService manageService;

    /**
     * SKU分页列表
     *
     * @param page  页码页数
     * @param limit 页码条数
     */
    @ApiOperation("SKU分页列表")
    @GetMapping("/list/{page}/{limit}")
    public Result<IPage<SkuInfo>> index(@PathVariable Long page, @PathVariable Long limit) {
        log.info("SKU分页列表:page: {}, limit: {}", page, limit);
        Page<SkuInfo> pageParam = new Page<>(page, limit);
        IPage<SkuInfo> pageModel = manageService.getPage(pageParam);
        return Result.ok(pageModel);
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
        log.info("根据属性id 查询平台属性对象,attrId:{}", attrId);
        if (attrId == null) {
            return Result.<List<BaseAttrValue>>fail().message("属性id不能为空");
        }
        BaseAttrInfo baseAttrInfo = manageService.getAttrInfo(attrId);
        List<BaseAttrValue> baseAttrValueList = null;
        if (baseAttrInfo != null) {
            baseAttrValueList = baseAttrInfo.getAttrValueList();
        }
        return Result.ok(baseAttrValueList);
    }

    /**
     * 根据attrId，删除属性和属性值
     *
     * @param attrId 属性id
     * @return
     */
    @ApiOperation("根据attrId，删除属性和属性值")
    @DeleteMapping("remove/{attrId}")
    public Result<Void> remove(@PathVariable Long attrId) {
        log.info("删除关联:attrId:{}", attrId);
        if (attrId == null) {
            return Result.<Void>fail().message("属性id不能为空");
        }
        //  调用服务层方法
        manageService.remove(attrId);
        return Result.ok();
    }

    /**
     * 保存平台属性方法
     *
     * @param baseAttrInfo 平台属性实例
     */
    @ApiOperation("保存平台属性")
    @PostMapping("saveAttrInfo")
    public Result<Void> saveAttrInfo(@RequestBody BaseAttrInfo baseAttrInfo) {
        log.info("保存平台属性,baseAttrInfo:{}", baseAttrInfo);
        if (baseAttrInfo == null) {
            return Result.<Void>fail().message("参数不能为空");
        }
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
        log.info("根据一级分类Id 查询二级分类数据,category1Id:{}", category1Id);
        if (category1Id == null) {
            return Result.<List<BaseCategory2>>fail().message("一级分类Id不能为空");
        }
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
        log.info("根据二级分类Id 查询三级分类数据,category2Id:{}", category2Id);
        if (category2Id == null) {
            return Result.<List<BaseCategory3>>fail().message("二级分类Id不能为空");
        }
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
    public Result<List<BaseAttrInfo>> attrInfoList(@PathVariable("category1Id") Long category1Id, @PathVariable("category2Id") Long category2Id, @PathVariable("category3Id") Long category3Id) {
        log.info("根据分类Id 获取平台属性数据,category1Id:{},category2Id:{},category3Id:{}", category1Id, category2Id, category3Id);
        if (category1Id == null || category2Id == null || category3Id == null) {
            return Result.<List<BaseAttrInfo>>fail().message("分类Id不能为空");
        }
        List<BaseAttrInfo> baseAttrInfoList = manageService.getAttrInfoList(category1Id, category2Id, category3Id);
        return Result.ok(baseAttrInfoList);
    }
}