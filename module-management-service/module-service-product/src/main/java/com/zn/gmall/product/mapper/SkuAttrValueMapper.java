package com.zn.gmall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zn.gmall.model.product.SkuAttrValue;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SkuAttrValueMapper extends BaseMapper<SkuAttrValue> {
    void batchInsert(List<SkuAttrValue> skuAttrValueList);
}

