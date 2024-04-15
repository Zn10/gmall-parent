package com.zn.gmall.product.controller;

import com.zn.gmall.common.result.Result;
import com.zn.gmall.model.product.BaseTrademark;
import com.zn.gmall.model.product.vo.CategoryTrademarkVo;
import com.zn.gmall.product.service.api.BaseCategoryTrademarkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 分类品牌列表控制
 */
@Api("分类品牌列表接口")
@RestController
@RequestMapping("admin/product/baseCategoryTrademark")
public class BaseCategoryTrademarkController {

    @Autowired
    private BaseCategoryTrademarkService baseCategoryTrademarkService;

    /**
     * 保存分类与品牌关联
     *
     * @param categoryTrademarkVo 封装分类与品牌VO
     */
    @ApiOperation("保存分类与品牌关联")
    @PostMapping("save")
    public Result<Void> save(@RequestBody CategoryTrademarkVo categoryTrademarkVo) {
        //  保存方法
        baseCategoryTrademarkService.save(categoryTrademarkVo);
        return Result.ok();
    }

    /**
     * 删除关联
     *
     * @param category3Id 三级分类id
     * @param trademarkId 关联id
     */
    @ApiOperation("删除关联")
    @DeleteMapping("remove/{category3Id}/{trademarkId}")
    public Result<Void> remove(@PathVariable Long category3Id, @PathVariable Long trademarkId) {
        //  调用服务层方法
        baseCategoryTrademarkService.remove(category3Id, trademarkId);
        return Result.ok();
    }

    /**
     * 根据三级分类获取品牌
     *
     * @param category3Id 三级分类id
     * @return List<BaseTrademark>
     */
    @ApiOperation("根据三级分类获取品牌")
    @GetMapping("findTrademarkList/{category3Id}")
    public Result<List<BaseTrademark>> findTrademarkList(@PathVariable Long category3Id) {
        //  select * from base_trademark
        List<BaseTrademark> list = baseCategoryTrademarkService.findTrademarkList(category3Id);
        //  返回
        return Result.ok(list);
    }

    /**
     * 获取当前未被三级分类关联的所有品牌
     *
     * @param category3Id 三级分类id
     * @return List<BaseTrademark>
     */
    @ApiOperation("获取当前未被三级分类关联的所有品牌")
    @GetMapping("findCurrentTrademarkList/{category3Id}")
    public Result<List<BaseTrademark>> findCurrentTrademarkList(@PathVariable Long category3Id) {
        List<BaseTrademark> list = baseCategoryTrademarkService.findCurrentTrademarkList(category3Id);
        //  返回
        return Result.ok(list);
    }
}

