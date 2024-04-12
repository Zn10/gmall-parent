package com.zn.gmall.product.service.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zn.gmall.model.product.BaseCategoryTrademark;
import com.zn.gmall.model.product.BaseTrademark;
import com.zn.gmall.model.product.vo.CategoryTrademarkVo;

import java.util.List;

/**
 * 分类品牌接口
 */
public interface BaseCategoryTrademarkService extends IService<BaseCategoryTrademark> {

    /**
     * 根据三级分类获取品牌
     *
     * @param category3Id 三级分类id
     * @return List<BaseTrademark>
     */
    List<BaseTrademark> findTrademarkList(Long category3Id);

    /**
     * 保存分类与品牌关联
     *
     * @param categoryTrademarkVo 封装分类与品牌VO
     */
    void save(CategoryTrademarkVo categoryTrademarkVo);

    /**
     * 获取当前未被三级分类关联的所有品牌
     *
     * @param category3Id 三级分类id
     * @return List<BaseTrademark>
     */
    List<BaseTrademark> findCurrentTrademarkList(Long category3Id);

    /**
     * 删除关联
     *
     * @param category3Id 三级分类id
     * @param trademarkId 关联id
     */
    void remove(Long category3Id, Long trademarkId);
}

