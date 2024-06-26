package com.zn.gmall.model.enums;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum OrderStatus {
    UNPAID("未支付"),
    PAID("已支付" ),
    WAITING_DELEVER("待发货"),
    DELEVERED("已发货"),
    CLOSED("已关闭"),
    FINISHED("已完结") ,
    SPLIT("订单已拆分");

    private final String comment ;
    //  根据订单状态来呼气订单名称
    public static String getStatusNameByStatus(String status) {
        OrderStatus arrObj[] = OrderStatus.values();
        for (OrderStatus obj : arrObj) {
            if (obj.name().equals(status)) {
                return obj.getComment();
            }
        }
        return "";
    }

    OrderStatus(String comment ){
        this.comment=comment;
    }

}
