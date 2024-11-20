package com.zn.gmall.activity.service.impl;

import com.zn.gmall.activity.mapper.CartInfoMapper;
import com.zn.gmall.activity.service.api.CartAsyncService;
import com.zn.gmall.model.cart.CartInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@SuppressWarnings("all")
@Slf4j
@Service
public class CartAsyncServiceImpl implements CartAsyncService {

    @Resource
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

