package com.zn.payment.client;

import com.zn.gmall.common.result.Result;
import com.zn.gmall.model.order.OrderInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(contextId = "OrderFeignClient", value = "service-cart", fallback = OrderDegradeFeignClient.class)
public interface OrderFeignClient {

    @GetMapping("/api/order/auth/trade")
    Result<Map<String, Object>> trade();

    /**
     * 获取订单
     *
     * @param orderId
     * @return
     */
    @GetMapping("/api/order/inner/getOrderInfo/{orderId}")
    Result<OrderInfo> getOrderInfo(@PathVariable(value = "orderId") Long orderId);


}
