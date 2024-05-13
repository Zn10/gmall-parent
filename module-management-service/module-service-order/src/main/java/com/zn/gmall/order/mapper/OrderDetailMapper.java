package com.zn.gmall.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zn.gmall.model.order.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: gmall-parent
 * @description: 订单详情
 * @author: Mr.Zhao
 * @create: 2024-05-13 20:50
 **/
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}

