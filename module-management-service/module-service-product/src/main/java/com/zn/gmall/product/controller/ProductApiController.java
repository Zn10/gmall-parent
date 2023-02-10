package com.zn.gmall.product.controller;

import com.zn.gmall.model.product.BaseCategoryView;
import com.zn.gmall.model.product.SkuInfo;
import com.zn.gmall.product.service.api.ManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("api/product")
public class ProductApiController {

    @Resource
    private ManageService manageService;

    /**
     * 根据skuId获取sku信息
     *
     * @param skuId
     * @return SkuInfo
     */
    @GetMapping("inner/getSkuInfo/{skuId}")
    public SkuInfo getAttrValueList(@PathVariable("skuId") Long skuId) {
        SkuInfo skuInfo = manageService.getSkuInfo(skuId);
        return skuInfo;
    }

    /**
     * 通过三级分类id查询分类信息
     *
     * @param category3Id
     * @return BaseCategoryView
     */
    @GetMapping("inner/getCategoryView/{category3Id}")
    public BaseCategoryView getCategoryView(@PathVariable("category3Id") Long category3Id) {
        return manageService.getCategoryViewByCategory3Id(category3Id);
    }
}

