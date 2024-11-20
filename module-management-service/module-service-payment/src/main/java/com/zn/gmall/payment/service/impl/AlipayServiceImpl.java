package com.zn.gmall.payment.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.zn.gmall.common.result.Result;
import com.zn.gmall.model.enums.PaymentStatus;
import com.zn.gmall.model.enums.PaymentType;
import com.zn.gmall.model.order.OrderInfo;
import com.zn.gmall.model.payment.PaymentInfo;
import com.zn.gmall.order.OrderFeignClient;
import com.zn.gmall.payment.config.AlipayConfig;
import com.zn.gmall.payment.service.api.AlipayService;
import com.zn.gmall.payment.service.api.PaymentService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * Package: com.zn.gmall.payment.service.impl
 * Description:
 * Created on 2024-11-10 00:57
 *
 * @author zhaonian
 */
@Service
@Slf4j
@SuppressWarnings("all")
public class AlipayServiceImpl implements AlipayService {

    @Resource
    private AlipayClient alipayClient;

    @Resource
    private OrderFeignClient orderFeignClient;

    @Resource
    private PaymentService paymentService;

    @SneakyThrows
    @Override
    public Boolean checkPayment(Long orderId) {
        // 根据订单Id 查询订单信息
        Result<OrderInfo> orderInfoResult = orderFeignClient.getOrderInfo(orderId);
        OrderInfo orderInfo = orderInfoResult.getData();
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        HashMap<String, Object> map = new HashMap<>();
        map.put("out_trade_no", orderInfo.getOutTradeNo());
        // 根据out_trade_no 查询交易记录
        request.setBizContent(JSON.toJSONString(map));
        AlipayTradeQueryResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public String createaliPay(Long orderId) {
        //  根据订单Id 获取orderInfo
        Result<OrderInfo> orderInfoResult = orderFeignClient.getOrderInfo(orderId);
        OrderInfo orderInfo = orderInfoResult.getData();
        if ("PAID".equals(orderInfo.getOrderStatus()) || "CLOSED".equals(orderInfo.getOrderStatus())) {
            return "该订单已经完成或已经关闭!";
        }
        //  调用保存交易记录方法！
        paymentService.savePaymentInfo(orderInfo, PaymentType.ALIPAY.name());

        String form = "";
        //  创建 AlipayClient 对象！ AlipayClient 这个对象注入到 spring 容器中！
        //  AlipayClient alipayClient =  new DefaultAlipayClient( "https://openapi.alipay.com/gateway.do" , APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);  //获得初始化的AlipayClient
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest(); //创建API对应的request
        //  同步回调 http://api.gmall.com/api/payment/alipay/callback/return
        alipayRequest.setReturnUrl(AlipayConfig.return_payment_url);
        //  异步回调
        alipayRequest.setNotifyUrl("http://domain.com/CallBack/notify_url.jsp"); //在公共参数中设置回跳和通知地址
        //  封装业务参数
        HashMap<String, Object> map = new HashMap<>();
        //  第三方业务编号！
        map.put("out_trade_no", orderInfo.getOutTradeNo());
        map.put("product_code", "FAST_INSTANT_TRADE_PAY");
        map.put("total_amount", "0.01");
        map.put("subject", orderInfo.getTradeBody());
        //  设置二维码过期时间
        map.put("timeout_express", "10m");
        alipayRequest.setBizContent(JSON.toJSONString(map));
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody();  //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return form;
    }

    @Override
    public boolean refund(Long orderId) {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();

        Result<OrderInfo> orderInfoResult = orderFeignClient.getOrderInfo(orderId);
        OrderInfo orderInfo = orderInfoResult.getData();
        HashMap<String, Object> map = new HashMap<>();
        map.put("out_trade_no", orderInfo.getOutTradeNo());
        map.put("refund_amount", orderInfo.getTotalAmount());
        map.put("refund_reason", "颜色浅了点");
        // out_request_no

        request.setBizContent(JSON.toJSONString(map));
        AlipayTradeRefundResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (response.isSuccess()) {
            // 更新交易记录 ： 关闭
            PaymentInfo paymentInfo = new PaymentInfo();
            paymentInfo.setPaymentStatus(PaymentStatus.CLOSED.name());
            paymentService.updatePaymentInfo(orderInfo.getOutTradeNo(), paymentInfo);
            return true;
        } else {
            return false;
        }
    }

    @SneakyThrows
    @Override
    public Boolean closePay(Long orderId) {
        Result<OrderInfo> orderInfoResult = orderFeignClient.getOrderInfo(orderId);
        OrderInfo orderInfo = orderInfoResult.getData();
        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
        HashMap<String, Object> map = new HashMap<>();
        // map.put("trade_no",paymentInfo.getTradeNo()); // 从paymentInfo 中获取！
        map.put("out_trade_no", orderInfo.getOutTradeNo());
        map.put("operator_id", "YX01");
        request.setBizContent(JSON.toJSONString(map));

        AlipayTradeCloseResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            System.out.println("调用成功");
            return true;
        } else {
            System.out.println("调用失败");
            return false;
        }
    }

}

