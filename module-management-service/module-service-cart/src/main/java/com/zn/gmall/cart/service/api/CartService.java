package com.zn.gmall.cart.service.api;

import com.zn.gmall.model.cart.CartInfo;

import java.util.List;

/**
 * 购物车服务
 */
public interface CartService {

    /**
     * 查询购物车数据时，考虑两种 id 是否存在的情况。
     * 上层 Controller 方法调用的就是这个方法
     * @param userId
     * @param userTempId
     * @return
     */
    List<CartInfo> getCartList(String userId, String userTempId);

    /**
     * 根据 userId 到缓存查询购物车集合，如果缓存没有再查询数据库
     * 查询数据库调用 getCartListFromDBToCache() 方法
     * @param userId
     * @return
     */
    List<CartInfo> getCartListFromCacheFirst(String userId);

    /**
     * 根据 userId 到数据库查询购物车集合，查到数据还存入 Redis 缓存
     * @param userId 可能是正式 id，也可能是临时 id
     * @return
     */
    List<CartInfo> getCartListFromDBToCache(String userId);

    /**
     * 执行购物车添加操作
     * @param skuId
     * @param userId
     * @param skuNum
     */
    void addToCart(Long skuId, String userId, Integer skuNum);

    /**
     * 执行购物车中某一个购物项修改选中状态
     * @param userId
     *      用户正式 id 或临时 id，定位到某一个购物车
     * @param skuId
     *      在一个购物车中定位到一个具体购物项
     * @param isChecked
     *      1：勾选
     *      0：取消勾选
     */
    void modifyCartCheckStatus(String userId, Long skuId, Integer isChecked);

    /**
     * 删除某一个购物项
     * @param userId
     * @param skuId
     */
    void removeCartItem(String userId, Long skuId);

    /**
     * 订单保存成功后，清空购物车
     * @param userId
     */
    void clearCheckedCartItem(String userId);
}

