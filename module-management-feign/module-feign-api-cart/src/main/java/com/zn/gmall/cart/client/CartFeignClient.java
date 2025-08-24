package com.zn.gmall.cart.client;

import com.zn.gmall.common.result.Result;
import com.zn.gmall.model.cart.CartInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(contextId = "CartFeignClient", value = "service-cart", fallback = CartDegradeFeignClient.class)
public interface CartFeignClient {
    //  获取选中购物车列表！
    @RequestMapping("/getCartCheckedList/{userId}")
    Result<List<CartInfo>> getCartCheckedList(@PathVariable String userId);

    @RequestMapping("/addToCart/{skuId}/{skuNum}")
    Result<Void> addToCart(
            @PathVariable("skuId") Long skuId,
            @PathVariable("skuNum") Long skuNum);

    @RequestMapping("/delete/all/checked/cart")
    Result<Void> deleteAllCheckedCart();
}
