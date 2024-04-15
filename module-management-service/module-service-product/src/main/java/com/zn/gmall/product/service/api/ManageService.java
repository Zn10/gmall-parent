package com.zn.gmall.product.service.api;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zn.gmall.model.product.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 商品汇总
 */
public interface ManageService {

    /**
     * 通过品牌Id 来查询数据
     *
     * @param tmId 品牌id
     * @return BaseTrademark
     */
    BaseTrademark getTrademarkByTmId(Long tmId);


    /**
     * 获取全部分类信息
     *
     * @return List<JSONObject>
     */
    List<JSONObject> getBaseCategoryList();


    /**
     * 通过skuId 集合来查询数据
     *
     * @param skuId 商品SKUid
     * @return List<BaseAttrInfo>
     */
    List<BaseAttrInfo> getAttrList(Long skuId);


    /**
     * 根据spuid获取商品海报
     *
     * @param spuId 商品SPUid
     * @return List<SpuPoster>
     */
    List<SpuPoster> findSpuPosterBySpuId(Long spuId);


    /**
     * 根据spuId 查询map 集合属性
     *
     * @param spuId 商品SPUid
     */
    Map<Object,Object> getSkuValueIdsMap(Long spuId);


    /**
     * 根据spuId，skuId 查询销售属性集合
     *
     * @param skuId 商品SKUid
     * @param spuId 商品SPUid
     * @return List<SpuSaleAttr>
     */
    List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(Long skuId, Long spuId);


    /**
     * 获取sku价格
     *
     * @param skuId 商品SKUid
     * @return BigDecimal
     */
    BigDecimal getSkuPrice(Long skuId);


    /**
     * 通过三级分类id查询分类信息
     *
     * @param category3Id 三级分类id
     * @return BaseCategoryView
     */
    BaseCategoryView getCategoryViewByCategory3Id(Long category3Id);


    /**
     * 根据skuId 查询skuInfo
     *
     * @param skuId 商品SKUID
     */
    SkuInfo getSkuInfo(Long skuId);


    /**
     * SKU分页列表
     *
     * @param pageParam 页码参数
     * @return IPage<SkuInfo>
     */
    IPage<SkuInfo> getPage(Page<SkuInfo> pageParam);

    /**
     * 商品上架
     *
     * @param skuId 商品SKUID
     */
    void onSale(Long skuId);

    /**
     * 商品下架
     *
     * @param skuId 商品SKUID
     */
    void cancelSale(Long skuId);


    /**
     * 保存数据
     *
     * @param skuInfo SKU实例
     */
    void saveSkuInfo(SkuInfo skuInfo);


    /**
     * 根据spuId 查询销售属性集合
     *
     * @param spuId 商品SPUID
     * @return List<SpuSaleAttr>
     */
    List<SpuSaleAttr> getSpuSaleAttrList(Long spuId);


    /**
     * 根据spuId 查询spuImageList
     *
     * @param spuId 商品SPUID
     * @return List<SpuImage>
     */
    List<SpuImage> getSpuImageList(Long spuId);


    /**
     * 保存商品数据
     *
     * @param spuInfo 商品SPU实例
     */
    void saveSpuInfo(SpuInfo spuInfo);


    /**
     * 查询所有的销售属性数据
     *
     * @return List<BaseSaleAttr>
     */
    List<BaseSaleAttr> getBaseSaleAttrList();


    /**
     * spu分页查询
     *
     * @param pageParam 页码参数
     * @param spuInfo   商品列表参数
     * @return IPage<SpuInfo>
     */
    IPage<SpuInfo> getSpuInfoPage(Page<SpuInfo> pageParam, SpuInfo spuInfo);


    /**
     * 根据attrId 查询平台属性对象
     *
     * @param attrId 属性id
     * @return BaseAttrInfo
     */
    BaseAttrInfo getAttrInfo(Long attrId);


    /**
     * 保存平台属性方法
     *
     * @param baseAttrInfo 平台属性实例
     */
    void saveAttrInfo(BaseAttrInfo baseAttrInfo);


    /**
     * 查询所有的一级分类信息
     *
     * @return List<BaseCategory1>
     */
    List<BaseCategory1> getCategory1();

    /**
     * 根据一级分类Id 查询二级分类数据
     *
     * @param category1Id 一级分类Id
     * @return List<BaseCategory2>
     */
    List<BaseCategory2> getCategory2(Long category1Id);

    /**
     * 根据二级分类Id 查询三级分类数据
     *
     * @param category2Id 二级分类Id
     * @return List<BaseCategory3>
     */
    List<BaseCategory3> getCategory3(Long category2Id);


    /**
     * 根据分类Id 获取平台属性数据
     * 接口说明：
     * 1，平台属性可以挂在一级分类、二级分类和三级分类
     * 2，查询一级分类下面的平台属性，传：category1Id，0，0；   取出该分类的平台属性
     * 3，查询二级分类下面的平台属性，传：category1Id，category2Id，0；
     * 取出对应一级分类下面的平台属性与二级分类对应的平台属性
     * 4，查询三级分类下面的平台属性，传：category1Id，category2Id，category3Id；
     * 取出对应一级分类、二级分类与三级分类对应的平台属性
     *
     * @param category1Id 一级分类Id
     * @param category2Id 二级分类Id
     * @param category3Id 三级分类Id
     * @return List<BaseAttrInfo>
     */
    List<BaseAttrInfo> getAttrInfoList(Long category1Id, Long category2Id, Long category3Id);
}

