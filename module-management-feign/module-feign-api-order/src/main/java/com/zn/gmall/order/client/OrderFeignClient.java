package com.zn.gmall.order.client;

import com.zn.gmall.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@FeignClient(value = "service-cart",fallback = OrderDegradeFeignClient.class,path = "/api/cart")
public interface OrderFeignClient {

    @GetMapping("/api/order/auth/trade")
    Result<Map<String, Object>> trade();

}
