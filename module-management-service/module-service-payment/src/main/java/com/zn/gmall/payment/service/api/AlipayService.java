package com.zn.gmall.payment.service.api;

/**
 * Package: com.zn.gmall.payment.service.api
 * Description:
 * Created on 2024-11-10 00:56
 *
 * @author zhaonian
 */
@SuppressWarnings("all")
public interface AlipayService {
    String createaliPay(Long orderId);
    boolean refund(Long orderId);
    /***
     * 关闭交易
     * @param orderId
     * @return
     */
    Boolean closePay(Long orderId);
    /**
     * 根据订单查询是否支付成功！
     * @param orderId
     * @return
     */
    Boolean checkPayment(Long orderId);


}
