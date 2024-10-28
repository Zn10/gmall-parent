package com.zn.gmall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zn.gmall.model.product.SpuImage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 库存单元图片数据交互
 */
@Mapper
public interface SpuImageMapper extends BaseMapper<SpuImage> {
    /**
     *  批量保存SpuImage
     * @param spuImageList 商品图片表
     */
    void batchInsert(List<SpuImage> spuImageList);
}

