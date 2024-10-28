package com.zn.gmall.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zn.gmall.common.result.Result;
import com.zn.gmall.model.product.BaseCategory1;
import com.zn.gmall.model.product.BaseTrademark;
import com.zn.gmall.product.service.api.BaseTrademarkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 品牌管理控制
 */
@Api("品牌管理接口")
@RestController
@RequestMapping("admin/product/baseTrademark")
public class BaseTrademarkController {

    @Autowired
    private BaseTrademarkService baseTrademarkService;

    /**
     * 查询所有的品牌信息
     *
     * @return List<BaseTrademark>
     */
    @ApiOperation("查询所有的品牌信息")
    @GetMapping("getTrademarkList")
    public Result<List<BaseTrademark>> getTrademarkList() {
        List<BaseTrademark> baseTrademarkList = baseTrademarkService.getTrademarkList();
        return Result.ok(baseTrademarkList);
    }

    /**
     * Banner分页列表
     *
     * @param page  页码参数
     * @param limit 页码参数
     * @return IPage<BaseTrademark>
     */
    @ApiOperation(value = "品牌分页列表")
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
    @ApiOperation(value = "根据品牌id查询该品牌")
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
    @ApiOperation(value = "添加品牌")
    @PostMapping("save")
    public Result<Void> save(@RequestBody BaseTrademark banner) {
        baseTrademarkService.save(banner);
        return Result.ok();
    }

    /**
     * 更新品牌
     *
     * @param banner 品牌实例
     */
    @ApiOperation(value = "更新品牌")
    @PutMapping("update")
    public Result<Void> updateById(@RequestBody BaseTrademark banner) {
        baseTrademarkService.updateById(banner);
        return Result.ok();
    }

    /**
     * 删除品牌
     *
     * @param id 品牌id
     */
    @ApiOperation(value = "根据品牌id删除品牌")
    @DeleteMapping("remove/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        baseTrademarkService.removeById(id);
        return Result.ok();
    }
}