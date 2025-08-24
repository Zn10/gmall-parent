package com.zn.gmall.order.client;

import com.zn.gmall.common.result.Result;
import com.zn.gmall.model.order.OrderInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(contextId = "OrderFeignClient", value = "service-cart", fallback = OrderDegradeFeignClient.class)
public interface OrderFeignClient {
    /**
     * 提交秒杀订单
     * @param orderInfo
     * @return
     */
    @RequestMapping("/api/order/inner/seckill/submitOrder")
    Long submitOrder(@RequestBody OrderInfo orderInfo);


    @RequestMapping("/api/order/auth/trade")
    Result<Map<String, Object>> trade();

    /**
     * 获取订单
     *
     * @param orderId
     * @return
     */
    @RequestMapping("/api/order/inner/getOrderInfo/{orderId}")
    Result<OrderInfo> getOrderInfo(@PathVariable(value = "orderId") Long orderId);


}
