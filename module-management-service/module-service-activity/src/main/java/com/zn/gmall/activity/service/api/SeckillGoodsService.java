package com.zn.gmall.activity.service.api;

import com.zn.gmall.common.result.Result;
import com.zn.gmall.model.activity.SeckillGoods;

import java.util.List;

/**
 * Package: com.zn.gmall.activity.service.api
 * Description:
 * Created on 2024-11-10 02:20
 *
 * @author zhaonian
 */
@SuppressWarnings("all")
public interface SeckillGoodsService {
    /**
     * 返回全部列表
     * @return
     */
    List<SeckillGoods> findAll();

    /**
     * 根据ID获取实体
     * @param id
     * @return
     */
    SeckillGoods getSeckillGoods(Long id);

    /**
     * 根据用户和商品ID实现秒杀下单
     * @param skuId
     * @param userId
     */

    void seckillOrder(Long skuId, String userId);

    void updateStockCount(Long skuId);

    /***
     * 根据商品id与用户ID查看订单信息
     * @param skuId
     * @param userId
     * @return
     */
    Result checkOrder(Long skuId, String userId);

}
