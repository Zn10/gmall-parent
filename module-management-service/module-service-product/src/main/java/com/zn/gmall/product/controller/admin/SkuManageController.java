package com.zn.gmall.product.controller.admin;

import com.zn.gmall.common.result.Result;
import com.zn.gmall.model.product.SkuInfo;
import com.zn.gmall.model.product.SpuImage;
import com.zn.gmall.model.product.SpuSaleAttr;
import com.zn.gmall.product.service.api.ManageService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商品SKU控制
 * <p/>
 */
@Api(tags = "商品SKU接口")
@RestController
@RequestMapping("admin/product")
public class SkuManageController {

    @Autowired
    private ManageService manageService;

    /**
     * 保存sku
     *
     * @param skuInfo SKU实例
     */
    @PostMapping("saveSkuInfo")
    public Result<Void> saveSkuInfo(@RequestBody SkuInfo skuInfo) {
        // 调用服务层
        manageService.saveSkuInfo(skuInfo);
        return Result.ok();
    }

    /**
     * 根据spuId 查询销售属性集合
     *
     * @param spuId 商品SPUID
     * @return List<SpuSaleAttr>
     */
    @GetMapping("spuSaleAttrList/{spuId}")
    public Result<List<SpuSaleAttr>> getSpuSaleAttrList(@PathVariable("spuId") Long spuId) {
        List<SpuSaleAttr> spuSaleAttrList = manageService.getSpuSaleAttrList(spuId);
        return Result.ok(spuSaleAttrList);
    }

    /**
     * 根据spuId 查询spuImageList
     *
     * @param spuId 商品SPUID
     * @return List<SpuImage>
     */
    @GetMapping("spuImageList/{spuId}")
    public Result<List<SpuImage>> getSpuImageList(@PathVariable("spuId") Long spuId) {
        List<SpuImage> spuImageList = manageService.getSpuImageList(spuId);
        return Result.ok(spuImageList);
    }
}

