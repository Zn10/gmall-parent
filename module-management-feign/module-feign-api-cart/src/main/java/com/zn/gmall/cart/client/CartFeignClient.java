package com.zn.gmall.cart.client;

import com.zn.gmall.common.result.Result;
import com.zn.gmall.model.cart.CartInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(contextId = "CartFeignClient", value = "service-cart", fallback = CartDegradeFeignClient.class)
public interface CartFeignClient {
    //  获取选中购物车列表！
    @GetMapping("/getCartCheckedList/{userId}")
    Result<List<CartInfo>> getCartCheckedList(@PathVariable String userId);

    @PostMapping("/addToCart/{skuId}/{skuNum}")
    Result<Void> addToCart(
            @PathVariable("skuId") Long skuId,
            @PathVariable("skuNum") Long skuNum);

    @DeleteMapping("/delete/all/checked/cart")
    Result<Void> deleteAllCheckedCart();
}
