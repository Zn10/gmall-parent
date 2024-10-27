package com.zn.gmall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zn.gmall.model.product.BaseAttrValue;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 属性值数据交互
 */
@Mapper
public interface BaseAttrValueMapper extends BaseMapper<BaseAttrValue> {
    void batchInsert(List<BaseAttrValue> attrValueList);
}

