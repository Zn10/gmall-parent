package com.zn.gmall.order.service.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zn.gmall.model.order.OrderInfo;

/**
 * @program: gmall-parent
 * @description: 订单接口
 * @author: Mr.Zhao
 * @create: 2024-05-13 20:51
 **/
public interface OrderService extends IService<OrderInfo> {

    IPage<OrderInfo> getPage(Page<OrderInfo> pageParam, String userId);

    /**
     * 验证库存
     *
     * @param skuId
     * @param skuNum
     * @return
     */
    boolean checkStock(Long skuId, Integer skuNum);


    /**
     * 生产流水号
     *
     * @param userId
     * @return
     */
    String getTradeNo(String userId);

    /**
     * 比较流水号
     *
     * @param userId      获取缓存中的流水号
     * @param tradeCodeNo 页面传递过来的流水号
     * @return
     */
    boolean checkTradeCode(String userId, String tradeCodeNo);


    /**
     * 删除流水号
     *
     * @param userId
     */
    void deleteTradeNo(String userId);


    /**
     * 保存订单
     *
     * @param orderInfo
     * @return
     */
    Long saveOrderInfo(OrderInfo orderInfo);

    void execExpiredOrder(Long orderId);
}

