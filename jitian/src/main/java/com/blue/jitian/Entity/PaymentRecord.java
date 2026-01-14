package com.blue.jitian.Entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Builder
@Data
@TableName("payment_records")
public class PaymentRecord {
    @TableId(type = IdType.AUTO)
    @TableField("payment_id")
    Long paymentId;
    
    @TableField("order_id")
    Long orderId;
    
    @TableField("user_id")
    Long userId;
    
    @TableField("out_trade_no")
    String outTradeNo;  // 商户订单号
    
    @TableField("transaction_no")
    String transactionNo;  // 第三方交易流水号
    
    @TableField("pay_method")
    Integer payMethod;  // 1:微信 2:支付宝
    
    @TableField("amount")
    BigDecimal amount;  // 支付金额
    
    @TableField("status")
    Integer status;  // 0:待支付 1:成功 2:失败 3:退款
    
    @TableField("notify_time")
    LocalDateTime notifyTime;  // 第三方回调通知时间
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    LocalDateTime createdAt;
}
