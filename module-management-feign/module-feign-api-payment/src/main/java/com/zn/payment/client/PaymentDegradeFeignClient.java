package com.zn.payment.client;

import com.zn.gmall.model.payment.PaymentInfo;
import org.springframework.stereotype.Component;

/**
 * Package: com.zn.payment.client
 * Description:
 * Created on 2024-11-10 01:46
 *
 * @author zhaonian
 */
@Component
public class PaymentDegradeFeignClient implements PaymentFeignClient {
    @Override
    public Boolean closePay(Long orderId) {
        return null;
    }

    @Override
    public Boolean checkPayment(Long orderId) {
        return null;
    }

    @Override
    public PaymentInfo getPaymentInfo(String outTradeNo) {
        return null;
    }

}

