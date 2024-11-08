package com.zn.gmall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zn.gmall.model.product.SkuImage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SkuImageMapper extends BaseMapper<SkuImage> {
    void batchInsert(List<SkuImage> skuImageList);
}

