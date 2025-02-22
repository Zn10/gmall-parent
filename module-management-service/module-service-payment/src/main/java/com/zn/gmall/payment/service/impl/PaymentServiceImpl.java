package com.zn.gmall.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zn.gmall.model.enums.PaymentStatus;
import com.zn.gmall.model.order.OrderInfo;
import com.zn.gmall.model.payment.PaymentInfo;
import com.zn.gmall.mq.constant.MqConst;
import com.zn.gmall.mq.service.RabbitService;
import com.zn.gmall.payment.mapper.PaymentInfoMapper;
import com.zn.gmall.payment.service.api.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * Package: com.zn.gmall.payment.service.impl
 * Description:
 * Created on 2024-11-10 00:53
 *
 * @author zhaonian
 */
@Service
@Slf4j
@SuppressWarnings("all")
public class PaymentServiceImpl implements PaymentService {

    @Resource
    private PaymentInfoMapper paymentInfoMapper;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private RabbitService rabbitService;

    @Override
    public PaymentInfo getPaymentInfo(String outTradeNo, String name) {
        QueryWrapper<PaymentInfo> paymentInfoQueryWrapper = new QueryWrapper<>();
        paymentInfoQueryWrapper.eq("out_trade_no", outTradeNo);
        paymentInfoQueryWrapper.eq("payment_type", name);
        return paymentInfoMapper.selectOne(paymentInfoQueryWrapper);
    }

    @Override
    public void paySuccess(String outTradeNo, String paymentType, Map<String, String> paramsMap) {
//  根据outTradeNo，paymentType 查询
        PaymentInfo paymentInfoQuery = this.getPaymentInfo(outTradeNo, paymentType);
        if (paymentInfoQuery == null) {
            return;
        }

        try {
            //  改造一下更新的方法！
            PaymentInfo paymentInfo = new PaymentInfo();
            paymentInfo.setCallbackTime(new Date());
            paymentInfo.setPaymentStatus(PaymentStatus.PAID.name());
            paymentInfo.setCallbackContent(paramsMap.toString());
            paymentInfo.setTradeNo(paramsMap.get("trade_no"));
            //  查询条件也可以作为更新条件！
            this.updatePaymentInfo(outTradeNo, paymentType, paymentInfo);
        } catch (Exception e) {
            //  删除key
            this.redisTemplate.delete(paramsMap.get("notify_id"));
            e.printStackTrace();
        }
        //  发送通知：更新订单的状态！
        this.rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_PAYMENT_PAY, MqConst.ROUTING_PAYMENT_PAY, paymentInfoQuery.getOrderId());

    }

    @Override
    public void updatePaymentInfo(String outTradeNo, String name, PaymentInfo paymentInfo) {
        QueryWrapper<PaymentInfo> paymentInfoQueryWrapper = new QueryWrapper<>();
        paymentInfoQueryWrapper.eq("out_trade_no", outTradeNo);
        paymentInfoQueryWrapper.eq("payment_type", name);
        paymentInfoMapper.update(paymentInfo, paymentInfoQueryWrapper);
    }

    @Override
    public void savePaymentInfo(OrderInfo orderInfo, String paymentType) {
        QueryWrapper<PaymentInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderInfo.getId());
        queryWrapper.eq("payment_type", paymentType);
        Integer count = paymentInfoMapper.selectCount(queryWrapper);
        if (count > 0) return;

        // 保存交易记录
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setCreateTime(new Date());
        paymentInfo.setOrderId(orderInfo.getId());
        paymentInfo.setPaymentType(paymentType);
        paymentInfo.setOutTradeNo(orderInfo.getOutTradeNo());
        paymentInfo.setPaymentStatus(PaymentStatus.UNPAID.name());
        paymentInfo.setSubject(orderInfo.getTradeBody());
        //paymentInfo.setSubject("test");
        paymentInfo.setTotalAmount(orderInfo.getTotalAmount());

        paymentInfoMapper.insert(paymentInfo);
    }

    @Override
    public void updatePaymentInfo(String outTradeNo, PaymentInfo paymentInfo) {
        QueryWrapper<PaymentInfo> paymentInfoQueryWrapper = new QueryWrapper<>();
        paymentInfoQueryWrapper.eq("out_trade_no", outTradeNo);
        paymentInfoMapper.update(paymentInfo, paymentInfoQueryWrapper);
    }

    @Override
    public void closePayment(Long orderId) {
        // 设置关闭交易记录的条件  118
        QueryWrapper<PaymentInfo> paymentInfoQueryWrapper = new QueryWrapper<>();
        paymentInfoQueryWrapper.eq("order_id", orderId);
        // 如果当前的交易记录不存在，则不更新交易记录
        Integer count = paymentInfoMapper.selectCount(paymentInfoQueryWrapper);
        if (null == count || count == 0) return;
        // 在关闭支付宝交易之前。还需要关闭paymentInfo
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setPaymentStatus(PaymentStatus.CLOSED.name());
        paymentInfoMapper.update(paymentInfo, paymentInfoQueryWrapper);
    }
}

