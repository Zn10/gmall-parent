package com.zn.gmall.cart.service.impl;

import com.zn.gmall.cart.mapper.CartInfoMapper;
import com.zn.gmall.cart.service.api.CartAsyncService;
import com.zn.gmall.model.cart.CartInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class CartAsyncServiceImpl implements CartAsyncService {

    @Autowired
    private CartInfoMapper cartInfoMapper;

    @Async
    @Override
    public void updateCartInfo(CartInfo cartInfo) {
        cartInfoMapper.updateById(cartInfo);
    }

    @Async
    @Override
    public void saveCartInfo(CartInfo cartInfo) {
        cartInfoMapper.insert(cartInfo);
    }
}

