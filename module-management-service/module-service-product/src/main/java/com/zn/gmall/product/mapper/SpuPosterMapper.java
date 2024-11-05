package com.zn.gmall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zn.gmall.model.product.SpuPoster;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品海报数据交互
 */
@Mapper
public interface SpuPosterMapper extends BaseMapper<SpuPoster> {
    void batchInsert(@Param("spuPosterList") List<SpuPoster> spuPosterList);
}

