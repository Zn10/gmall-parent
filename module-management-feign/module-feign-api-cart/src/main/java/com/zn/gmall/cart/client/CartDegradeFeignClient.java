package com.zn.gmall.cart.client;

import com.zn.gmall.common.result.Result;
import com.zn.gmall.model.cart.CartInfo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartDegradeFeignClient implements CartFeignClient {

    @Override
    public Result<List<CartInfo>> getCartCheckedList(String userId) {
        Result<List<CartInfo>> result = Result.fail();
        result.message("服务降级了");
        return result;
    }

    @Override
    public Result<Void> addToCart(Long skuId, Long skuNum) {
        Result<Void> result = Result.fail();
        result.message("服务降级了");
        return result;
    }

    @Override
    public Result<Void> deleteAllCheckedCart() {
        Result<Void> result = Result.fail();
        result.message("服务降级了");
        return result;
    }
}
