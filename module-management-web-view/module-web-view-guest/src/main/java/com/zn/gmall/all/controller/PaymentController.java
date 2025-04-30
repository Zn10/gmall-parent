package com.zn.gmall.all.controller;

import com.zn.gmall.common.result.Result;
import com.zn.gmall.model.order.OrderInfo;
import com.zn.gmall.order.client.OrderFeignClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Package: com.zn.gmall.all.controller
 * Description:
 * Created on 2024-11-10 00:49
 *
 * @author zhaonian
 */
@Controller
public class PaymentController {

    @Resource
    private OrderFeignClient orderFeignClient;

    /**
     * 支付成功页
     * @return
     */
    @GetMapping("pay/success.html")
    public String success() {
        return "payment/success";
    }


    /**
     * 支付页
     *
     * @param request
     * @return
     */
    @GetMapping("pay.html")
    public String success(HttpServletRequest request, Model model) {
        String orderId = request.getParameter("orderId");
        Result<OrderInfo> orderInfoResult = orderFeignClient.getOrderInfo(Long.parseLong(orderId));
        OrderInfo orderInfo = orderInfoResult.getData();
        model.addAttribute("orderInfo", orderInfo);
        return "payment/pay";
    }
}

