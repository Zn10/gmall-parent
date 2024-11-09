package com.zn.payment.client;

import com.zn.gmall.model.payment.PaymentInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Package: com.zn.payment.client
 * Description:
 * Created on 2024-11-10 01:46
 *
 * @author zhaonian
 */
@FeignClient(value = "service-payment", fallback = PaymentDegradeFeignClient.class)
public interface PaymentFeignClient {

    @GetMapping("api/payment/alipay/closePay/{orderId}")
    Boolean closePay(@PathVariable Long orderId);

    @GetMapping("api/payment/alipay/checkPayment/{orderId}")
    Boolean checkPayment(@PathVariable Long orderId);

    @GetMapping("api/payment/alipay/getPaymentInfo/{outTradeNo}")
    PaymentInfo getPaymentInfo(@PathVariable String outTradeNo);

}

