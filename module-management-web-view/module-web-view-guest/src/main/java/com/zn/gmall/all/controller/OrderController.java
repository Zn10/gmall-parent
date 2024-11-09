package com.zn.gmall.all.controller;

import com.zn.gmall.common.result.Result;
import com.zn.payment.client.OrderFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

/**
 * @program: gmall-parent
 * @description: 订单控制器
 * @author: Mr.Zhao
 * @create: 2024-05-13 20:47
 **/
@Controller
public class OrderController {

    @Qualifier("orderDegradeFeignClient")
    @Autowired
    private OrderFeignClient orderFeignClient;

    /**
     * 确认订单
     *
     * @param model
     * @return
     */
    @GetMapping("trade.html")
    public String trade(Model model) {
        Result<Map<String, Object>> result = orderFeignClient.trade();

        model.addAllAttributes(result.getData());
        return "order/trade";
    }

    /**
     * 我的订单
     *
     * @return
     */
    @GetMapping("myOrder.html")
    public String myOrder() {
        return "order/myOrder";
    }

}

