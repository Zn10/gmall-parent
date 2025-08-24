package com.zn.gmall.product.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zn.gmall.common.result.Result;
import com.zn.gmall.model.product.BaseTrademark;
import com.zn.gmall.product.service.api.BaseTrademarkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 品牌管理控制
 */
@Api(tags = "品牌管理接口")
@RestController
@RequestMapping("admin/product/baseTrademark")
@Slf4j
@SuppressWarnings("all")
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
    @ResponseBody
    public Result<List<BaseTrademark>> getTrademarkList() {
        List<BaseTrademark> baseTrademarkList = baseTrademarkService.list();
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
    @ResponseBody
    public Result<IPage<BaseTrademark>> index(@PathVariable Long page,
                                              @PathVariable Long limit) {
        log.info("品牌分页列表:page: {}, limit: {}", page, limit);
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
    @ResponseBody
    public Result<BaseTrademark> get(@PathVariable String id) {
        log.info("根据品牌id查询该品牌: id: {}", id);
        if (id == null) {
            return Result.<BaseTrademark>fail().message("品牌id不能为空");
        }
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
    @ResponseBody
    public Result<Void> save(@RequestBody BaseTrademark banner) {
        log.info("添加品牌: BaseTrademark: {}", banner);
        if (banner == null) {
            return Result.<Void>fail().message("参数不能为空");
        }
        baseTrademarkService.save(banner);
        return Result.ok();
    }

    /**
     * 更新品牌
     *
     * @param banner 品牌实例
     */
    @ApiOperation(value = "更新品牌")
    @PostMapping("update")
    @ResponseBody
    public Result<Void> updateById(@RequestBody BaseTrademark banner) {
        log.info("更新品牌: BaseTrademark: {}", banner);
        if (banner.getId() == null) {
            return Result.<Void>fail().message("品牌id不能为空");
        }
        baseTrademarkService.updateById(banner);
        return Result.ok();
    }

    /**
     * 删除品牌
     *
     * @param id 品牌id
     */
    @ApiOperation(value = "根据品牌id删除品牌")
    @GetMapping("remove/{id}")
    @ResponseBody
    public Result<Void> remove(@PathVariable Long id) {
        log.info("根据品牌id删除品牌: id: {}", id);
        if (id == null) {
            return Result.<Void>fail().message("品牌id不能为空");
        }
        baseTrademarkService.removeById(id);
        return Result.ok();
    }
}