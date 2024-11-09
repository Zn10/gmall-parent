package com.zn.gmall.payment.service.api;

/**
 * Package: com.zn.gmall.payment.service.api
 * Description:
 * Created on 2024-11-10 00:56
 *
 * @author zhaonian
 */
public interface AlipayService {
    String createaliPay(Long orderId);
    boolean refund(Long orderId);
}
