package com.zn.gmall.cart.controller;

import com.zn.gmall.cart.service.api.CartService;
import com.zn.gmall.common.result.Result;
import com.zn.gmall.common.util.AuthContextHolder;
import com.zn.gmall.model.cart.CartInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/cartList")
    public Result<List<CartInfo>> getCartInfoList(HttpServletRequest request) {

        String userId = AuthContextHolder.getUserId(request);
        String userTempId = AuthContextHolder.getUserTempId(request);

        List<CartInfo> cartList = cartService.getCartList(userId, userTempId);

        return Result.ok(cartList);
    }

    @PostMapping("/addToCart/{skuId}/{skuNum}")
    public Result<Void> addToCart(
            @PathVariable("skuId") Long skuId,
            @PathVariable("skuNum") Integer skuNum,
            HttpServletRequest request) {

        // 1、获取 userId
        // [1]先尝试获取正式登录后的用户 id
        String userId = AuthContextHolder.getUserId(request);

        // [2]如果用户没有登录，则获取临时登录的 id
        if (StringUtils.isEmpty(userId)) {
            userId = AuthContextHolder.getUserTempId(request);
        }

        // 2、执行添加购物车
        cartService.addToCart(skuId, userId, skuNum);

        // 3、返回结果
        return Result.ok();
    }

    @GetMapping("/checkCart/{skuId}/{isChecked}")
    public Result<Void> modifyCartCheckStatus(
            @PathVariable("skuId") Long skuId,
            @PathVariable("isChecked") Integer isChecked,
            HttpServletRequest request) {

        // 1、获取用户正式登录的 id
        String userId = AuthContextHolder.getUserId(request);
        if (!StringUtils.isEmpty(userId)) {
            cartService.modifyCartCheckStatus(userId, skuId, isChecked);
        }

        // 2、获取用户未登录的临时 id
        String userTempId = AuthContextHolder.getUserTempId(request);
        if (!StringUtils.isEmpty(userTempId)) {
            cartService.modifyCartCheckStatus(userTempId, skuId, isChecked);
        }

        return Result.ok();
    }

    @DeleteMapping("/deleteCart/{skuId}")
    public Result<Void> removeCartItem(
            @PathVariable("skuId") Long skuId,
            HttpServletRequest request) {

        // 1、获取用户登录的正式 id
        String userId = AuthContextHolder.getUserId(request);
        if (!StringUtils.isEmpty(userId)) {
            cartService.removeCartItem(userId, skuId);
        }

        // 2、获取用户登录的临时 id
        String userTempId = AuthContextHolder.getUserTempId(request);
        if (!StringUtils.isEmpty(userTempId)) {
            cartService.removeCartItem(userTempId, skuId);
        }

        return Result.ok();
    }

    @GetMapping("/inner/get/cart/checked/{userId}")
    public Result<List<CartInfo>> getCheckedCartList(@PathVariable("userId") String userId) {

        // 根据 userId 查询 Redis，得到当前购物车中全部数据
        // ※说明：为什么查的不是数据库？
        // 因为 cart_info 这个表也未必是最新的准确数据，将来验证库存、价格也不是查这个表；
        // 而且 cart_info 这个表中没有 sku_price 字段值
        List<CartInfo> cartListFromCache = cartService.getCartListFromCacheFirst(userId);

        // 使用 StreamAPI 进行数据筛选
        List<CartInfo> checkedCartList =
                cartListFromCache
                        .stream()
                        .filter(
                                (CartInfo cartInfo) -> cartInfo.getIsChecked() == 1).collect(Collectors.toList()
                        );

        return Result.ok(checkedCartList);
    }

    @GetMapping("/inner/get/cart/list/from/db/to/cache/{userId}")
    public Result<Void> getCartListFromDBToCache(@PathVariable("userId") String userId) {

        cartService.getCartListFromDBToCache(userId);

        return Result.ok();
    }

    @GetMapping("/inner/clear/checked/cart/{userId}")
    public Result<Void> clearCheckedCartItem(@PathVariable("userId") String userId) {

        cartService.clearCheckedCartItem(userId);

        return Result.ok();
    }
}

