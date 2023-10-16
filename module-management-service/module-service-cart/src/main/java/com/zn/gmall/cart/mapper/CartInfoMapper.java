package com.zn.gmall.cart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zn.gmall.model.cart.CartInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartInfoMapper extends BaseMapper<CartInfo> {
    /**
     * 针对已经合并好的已登录集合执行批量保存
     * @param cartInfoList_login
     */
    void batchInsert(@Param("cartInfoListLogin") List<CartInfo> cartInfoList_login);

    /**
     * 执行选中状态的修改
     * @param userId
     * @param skuId
     * @param isChecked
     */
    void updateCartStatus(
            @Param("userId") String userId,
            @Param("skuId") Long skuId,
            @Param("isChecked") Integer isChecked);
}

