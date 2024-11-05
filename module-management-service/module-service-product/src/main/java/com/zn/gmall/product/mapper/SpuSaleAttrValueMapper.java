package com.zn.gmall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zn.gmall.model.product.SpuSaleAttrValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * SPU销售属性值数据交互
 */
@Mapper
public interface SpuSaleAttrValueMapper extends BaseMapper<SpuSaleAttrValue> {
    /**
     * 批量插入SPU销售属性值
     *
     * @param spuSaleAttrValueList SPU销售属性值
     */
    void batchInsert(@Param("spuSaleAttrValueList") List<SpuSaleAttrValue> spuSaleAttrValueList);
}
