package com.zn.gmall.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zn.gmall.common.result.Result;
import com.zn.gmall.model.product.BaseTrademark;
import com.zn.gmall.product.service.api.BaseTrademarkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 品牌管理控制
 */
@Api("品牌管理接口")
@RestController
@RequestMapping("admin/product/baseTrademark")
public class BaseTrademarkController {

    @Resource
    private BaseTrademarkService baseTrademarkService;

    /**
     * Banner分页列表
     *
     * @param page  页码参数
     * @param limit 页码参数
     * @return IPage<BaseTrademark>
     */
    @ApiOperation(value = "分页列表")
    @GetMapping("{page}/{limit}")
    public Result<IPage<BaseTrademark>> index(@PathVariable Long page,
                        @PathVariable Long limit) {

        Page<BaseTrademark> pageParam = new Page<>(page, limit);
        IPage<BaseTrademark> pageModel = baseTrademarkService.getPage(pageParam);
        return Result.ok(pageModel);
    }

    /**
     * 根据品牌id查询该品牌
     *
     * @param id 品牌id
     * @return BaseTrademark
     */
    @ApiOperation(value = "获取BaseTrademark")
    @GetMapping("get/{id}")
    public Result<BaseTrademark> get(@PathVariable String id) {
        BaseTrademark baseTrademark = baseTrademarkService.getById(id);
        return Result.ok(baseTrademark);
    }

    /**
     * 添加品牌
     *
     * @param banner 品牌实例
     */
    @ApiOperation(value = "新增BaseTrademark")
    @PostMapping("save")
    public Result<String> save(@RequestBody BaseTrademark banner) {
        baseTrademarkService.save(banner);
        return Result.ok();
    }

    /**
     * 更新品牌
     *
     * @param banner 品牌实例
     */
    @ApiOperation(value = "修改BaseTrademark")
    @PutMapping("update")
    public Result<String> updateById(@RequestBody BaseTrademark banner) {
        baseTrademarkService.updateById(banner);
        return Result.ok();
    }

    /**
     * 删除品牌
     *
     * @param id 品牌id
     */
    @ApiOperation(value = "删除BaseTrademark")
    @DeleteMapping("remove/{id}")
    public Result<String> remove(@PathVariable Long id) {
        baseTrademarkService.removeById(id);
        return Result.ok();
    }
}