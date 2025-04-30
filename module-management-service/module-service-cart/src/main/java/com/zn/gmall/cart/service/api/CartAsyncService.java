package com.zn.gmall.cart.service.api;

import com.zn.gmall.model.cart.CartInfo;

@SuppressWarnings("all")
public interface CartAsyncService {

    /**
     * 异步执行购物车信息的数据库更新
     * @param cartInfo
     */
    void updateCartInfo(CartInfo cartInfo);

    /**
     * 异步执行购物车信息的数据库保存
     * @param cartInfo
     */
    void saveCartInfo(CartInfo cartInfo);

}
