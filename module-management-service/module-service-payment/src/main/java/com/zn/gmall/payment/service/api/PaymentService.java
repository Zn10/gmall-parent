package com.zn.gmall.payment.service.api;

import com.zn.gmall.model.order.OrderInfo;
import com.zn.gmall.model.payment.PaymentInfo;

import java.util.Map;

/**
 * Package: com.zn.gmall.payment.service.api
 * Description:
 * Created on 2024-11-10 00:53
 *
 * @author zhaonian
 */
public interface PaymentService {

    /**
     * 获取paymentInfo 对象
     *
     * @param outTradeNo
     * @param name
     * @return
     */
    PaymentInfo getPaymentInfo(String outTradeNo, String name);

    /**
     * 支付成功更新交易记录方法
     *
     * @param outTradeNo
     * @param name
     * @param paramMap
     */
    void paySuccess(String outTradeNo, String name, Map<String, String> paramMap);

    /**
     * 根据outTradeNo 支付方式name 更新数据
     *
     * @param outTradeNo
     * @param name
     * @param paymentInfo
     */
    void updatePaymentInfo(String outTradeNo, String name, PaymentInfo paymentInfo);

    /**
     * 保存交易记录
     *
     * @param orderInfo   订单信息
     * @param paymentType 支付类型（1：微信 2：支付宝）
     */
    void savePaymentInfo(OrderInfo orderInfo, String paymentType);

    /**
     * 根据订单号，支付方法name 更新数据
     *
     * @param outTradeNo
     * @param paymentInfo
     */
    void updatePaymentInfo(String outTradeNo, PaymentInfo paymentInfo);
}
