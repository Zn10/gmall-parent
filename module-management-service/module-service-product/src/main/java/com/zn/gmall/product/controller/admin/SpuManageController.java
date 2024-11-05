package com.zn.gmall.product.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zn.gmall.common.result.Result;
import com.zn.gmall.model.product.BaseSaleAttr;
import com.zn.gmall.model.product.SpuInfo;
import com.zn.gmall.product.service.api.ManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品列表控制
 */
@Api(tags ="商品SPU接口")
@RestController // @ResponseBody + @Controller
@RequestMapping("admin/product")
@Slf4j
public class SpuManageController {

    @Autowired
    private ManageService manageService;

    /**
     * 保存商品数据
     *
     * @param spuInfo 商品SPU实例
     */
    @ApiOperation("保存商品SPU数据")
    @PostMapping("saveSpuInfo")
    public Result<Void> saveSpuInfo(@RequestBody SpuInfo spuInfo) {
        log.info("保存商品SPU数据:{}", spuInfo);
        // 调用服务层的保存方法
        manageService.saveSpuInfo(spuInfo);
        return Result.ok();
    }

    /**
     * 查询所有的销售属性数据
     *
     * @return List<BaseSaleAttr>
     */
    @ApiOperation("查询所有的销售属性数据")
    @GetMapping("baseSaleAttrList")
    public Result<List<BaseSaleAttr>> baseSaleAttrList() {
        // 查询所有的销售属性集合
        List<BaseSaleAttr> baseSaleAttrList = manageService.getBaseSaleAttrList();
        return Result.ok(baseSaleAttrList);
    }

    /**
     * spu分页查询
     *
     * @param page    页码参数
     * @param size    页码参数
     * @param spuInfo 商品列表参数
     * @return IPage<SpuInfo>
     */
    @ApiOperation("spu分页查询")
    @GetMapping("{page}/{size}")
    public Result<IPage<SpuInfo>> getSpuInfoPage(@PathVariable Long page,
                                         @PathVariable Long size,
                                         SpuInfo spuInfo) {
        log.info("品牌分页列表:page: {}, size: {}", page, size);
        if (page == null || size == null) {
            return Result.<IPage<SpuInfo>>fail().message("页码或页数不能为空");
        }
        // 创建一个Page 对象
        Page<SpuInfo> spuInfoPage = new Page<>(page, size);
        // 获取数据
        IPage<SpuInfo> spuInfoPageList = manageService.getSpuInfoPage(spuInfoPage, spuInfo);
        // 将获取到的数据返回即可！
        return Result.ok(spuInfoPageList);
    }
}


