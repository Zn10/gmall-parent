package com.zn.gmall.model.order;

import com.zn.gmall.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "订单状态记录")
@TableName("order_status_log")
public class OrderStatusLog extends BaseEntity {
   
   private static final long serialVersionUID = 1L;
   
   @ApiModelProperty(value = "orderId")
   @TableField("order_id")
   private Long orderId;

   @ApiModelProperty(value = "orderStatus")
   @TableField("order_status")
   private String orderStatus;

   @ApiModelProperty(value = "operateTime")
   @TableField("operate_time")
   private Date operateTime;

}