package com.zn.gmall.order.client;

import com.zn.gmall.common.result.Result;
import com.zn.gmall.model.order.OrderInfo;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OrderDegradeFeignClient implements OrderFeignClient {

    @Override
    public Long submitOrder(OrderInfo orderInfo) {
        return null;
    }

    @Override
    public Result<Map<String, Object>> trade() {
        Result<Map<String, Object>> result = Result.fail();
        result.message("服务降级了");
        return result;
    }

    @Override
    public Result<OrderInfo> getOrderInfo(Long orderId) {
        Result<OrderInfo> result = Result.fail();
        result.message("服务降级了");
        return result;
    }
}
