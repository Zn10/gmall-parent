package com.zn.gmall.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zn.gmall.model.order.OrderInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: gmall-parent
 * @description: 订单信息
 * @author: Mr.Zhao
 * @create: 2024-05-13 20:50
 **/
@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    IPage<OrderInfo> selectPageByUserId(Page<OrderInfo> pageParam, String userId);
}

