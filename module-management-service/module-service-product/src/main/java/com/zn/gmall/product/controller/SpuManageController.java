package com.zn.gmall.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zn.gmall.common.result.Result;
import com.zn.gmall.model.product.BaseSaleAttr;
import com.zn.gmall.model.product.SpuInfo;
import com.zn.gmall.product.service.api.ManageService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("spu商品接口")
@RestController // @ResponseBody + @Controller
@RequestMapping("admin/product")
public class SpuManageController {

    @Autowired
    private ManageService manageService;

    /**
     * 保存spu
     *
     * @param spuInfo
     */
    @PostMapping("saveSpuInfo")
    public Result saveSpuInfo(@RequestBody SpuInfo spuInfo) {
        // 调用服务层的保存方法
        manageService.saveSpuInfo(spuInfo);
        return Result.ok();
    }

    // 销售属性http://api.gmall.com/admin/product/baseSaleAttrList
    @GetMapping("baseSaleAttrList")
    public Result baseSaleAttrList() {
        // 查询所有的销售属性集合
        List<BaseSaleAttr> baseSaleAttrList = manageService.getBaseSaleAttrList();

        return Result.ok(baseSaleAttrList);
    }

    // 根据查询条件封装控制器
    // springMVC 的时候，有个叫对象属性传值 如果页面提交过来的参数与实体类的参数一致，
    // 则可以使用实体类来接收数据
    // http://api.gmall.com/admin/product/1/10?category3Id=61
    // @RequestBody 作用 将前台传递过来的json{"category3Id":"61"}  字符串变为java 对象。
    @GetMapping("{page}/{size}")
    public Result getSpuInfoPage(@PathVariable Long page,
                                 @PathVariable Long size,
                                 SpuInfo spuInfo) {
        // 创建一个Page 对象
        Page<SpuInfo> spuInfoPage = new Page<>(page, size);
        // 获取数据
        IPage<SpuInfo> spuInfoPageList = manageService.getSpuInfoPage(spuInfoPage, spuInfo);
        // 将获取到的数据返回即可！
        return Result.ok(spuInfoPageList);
    }
}


