package com.zn.gmall.payment.controller.api;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.zn.gmall.common.result.Result;
import com.zn.gmall.model.enums.PaymentType;
import com.zn.gmall.model.payment.PaymentInfo;
import com.zn.gmall.payment.config.AlipayConfig;
import com.zn.gmall.payment.service.api.AlipayService;
import com.zn.gmall.payment.service.api.PaymentService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Package: com.zn.gmall.payment.controller.api
 * Description:
 * Created on 2024-11-10 00:58
 *
 * @author zhaonian
 */
@Controller
@RequestMapping("/api/payment/alipay")
@Slf4j
@SuppressWarnings("all")
public class AlipayController {

    @Resource
    private AlipayService alipayService;
    @Resource
    private PaymentService paymentService;
    @Resource
    private RedisTemplate redisTemplate;

    @ApiOperation("根据订单id查询支付记录")
    @RequestMapping("getPaymentInfo/{outTradeNo}")
    @ResponseBody
    public PaymentInfo getPaymentInfo(@PathVariable String outTradeNo) {
        PaymentInfo paymentInfo = paymentService.getPaymentInfo(outTradeNo, PaymentType.ALIPAY.name());
        if (null != paymentInfo) {
            return paymentInfo;
        }
        return null;
    }


    // 查看是否有交易记录
    @ApiOperation("查看是否有交易记录")
    @RequestMapping("checkPayment/{orderId}")
    @ResponseBody
    public Boolean checkPayment(@PathVariable Long orderId) {
        // 调用退款接口
        boolean flag = alipayService.checkPayment(orderId);
        return flag;
    }


    @ApiOperation("关闭交易记录")
    @RequestMapping("closePay/{orderId}")
    @ResponseBody
    public Boolean closePay(@PathVariable Long orderId) {
        Boolean aBoolean = alipayService.closePay(orderId);
        return aBoolean;
    }


    @ApiOperation("支付宝退款")
    @RequestMapping("refund/{orderId}")
    @ResponseBody
    public Result refund(@PathVariable(value = "orderId") Long orderId) {

        // 调用退款接口
        boolean flag = alipayService.refund(orderId);
        return Result.ok(flag);
    }


    @ApiOperation("支付宝回调")
    @RequestMapping("/callback/notify")
    @ResponseBody
    public String callbackNotify(@RequestParam Map<String, String> paramsMap) {
        // Map<String, String> paramsMap = ... // 将异步通知中收到的所有参数都存放到map中
        boolean signVerified = false; //调用SDK验证签名
        try {
            signVerified = AlipaySignature.rsaCheckV1(paramsMap, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        //  获取异步通知的参数中的订单号！
        String outTradeNo = paramsMap.get("out_trade_no");
        //  获取异步通知的参数中的订单总金额！
        String totalAmount = paramsMap.get("total_amount");
        //  获取异步通知的参数中的appId！
        String appId = paramsMap.get("app_id");
        //  获取异步通知的参数中的交易状态！
        String tradeStatus = paramsMap.get("trade_status");
        //  根据outTradeNo 查询数据！
        PaymentInfo paymentinfo = this.paymentService.getPaymentInfo(outTradeNo, PaymentType.ALIPAY.name());
        //  保证异步通知的幂等性！notify_id
        String notifyId = paramsMap.get("notify_id");

        //  true:
        if (signVerified) {
            // TODO 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
            if (paymentinfo == null || new BigDecimal("0.01").compareTo(new BigDecimal(totalAmount)) != 0 || !appId.equals(paymentinfo.getUserId())) {
                return "failure";
            }
            //  放入redis！ setnx：当 key 不存在的时候生效！
            Boolean flag = this.redisTemplate.opsForValue().setIfAbsent(notifyId, notifyId, 1462, TimeUnit.MINUTES);
            //  说明已经处理过了！
            if (!flag) {
                return "failure";
            }
            if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                //  修改交易记录状态！再订单状态！
                this.paymentService.paySuccess(outTradeNo, PaymentType.ALIPAY.name(), paramsMap);
                //  this.paymentService.paySuccess(paymentinfo.getId(),paramsMap);
                return "success";
            }
        } else {
            // TODO 验签失败则记录异常日志，并在response中返回failure.
            return "failure";
        }
        return "failure";
    }


    /**
     * 支付宝回调
     *
     * @return
     */
    @ApiOperation("支付宝回调")
    @RequestMapping("callback/return")
    public String callBack() {
        // 同步回调给用户展示信息
        return "redirect:" + AlipayConfig.return_order_url;
    }


    @ApiOperation("支付宝支付")
    @RequestMapping("submit/{orderId}")
    @ResponseBody
    public String submitOrder(@PathVariable Long orderId) {
        log.info("orderId:{}", orderId);
        if (orderId == null) {
            return "订单号为空";
        }
        String from = alipayService.createaliPay(orderId);
        return from;
    }
}

