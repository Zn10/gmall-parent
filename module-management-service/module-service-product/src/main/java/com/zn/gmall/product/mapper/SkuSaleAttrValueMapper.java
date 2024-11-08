package com.zn.gmall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zn.gmall.model.product.SkuSaleAttrValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SkuSaleAttrValueMapper extends BaseMapper<SkuSaleAttrValue> {

    /**
     * 根据spuId 查询map 集合数据
     *
     * @param spuId 商品SPUID
     */
    List<Map<Object,Object>> selectSaleAttrValuesBySpu(@Param("spuId") Long spuId);

    void batchInsert(List<SkuSaleAttrValue> skuSaleAttrValueList);
}

