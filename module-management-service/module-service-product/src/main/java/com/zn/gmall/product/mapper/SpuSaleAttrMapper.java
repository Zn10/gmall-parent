package com.zn.gmall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zn.gmall.model.product.SpuSaleAttr;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * SPU销售属性数据交互
 */
@Mapper
public interface SpuSaleAttrMapper extends BaseMapper<SpuSaleAttr> {
    /**
     * 根据spuId 查询销售属性集合
     *
     * @param spuId SPU销售属性
     * @return List<SpuSaleAttr>
     */
    List<SpuSaleAttr> selectSpuSaleAttrList(@Param("spuId") Long spuId);

    /**
     * 根据spuId，skuId 查询销售属性集合
     *
     * @param skuId SKU销售属性id
     * @param spuId SPU销售属性id
     * @return List<SpuSaleAttr>
     */
    List<SpuSaleAttr> selectSpuSaleAttrListCheckBySku(@Param("skuId") Long skuId, @Param("spuId") Long spuId);

    /**
     *  批量插入spu销售属性值
     * @param spuSaleAttrList spu销售属性值
     */
    void batchInsert(List<SpuSaleAttr> spuSaleAttrList);
}

