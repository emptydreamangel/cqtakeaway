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
@TableName("refund_records")
public class RefundRecord {
    @TableId(type = IdType.AUTO)
    @TableField("refund_id")
    Long refundId;
    
    @TableField("order_id")
    Long orderId;
    
    @TableField("user_id")
    Long userId;
    
    @TableField("payment_id")
    Long paymentId;
    
    @TableField("out_refund_no")
    String outRefundNo;  // 商户退款单号
    
    @TableField("refund_no")
    String refundNo;  // 第三方退款流水号
    
    @TableField("refund_amount")
    BigDecimal refundAmount;  // 退款金额
    
    @TableField("refund_reason")
    String refundReason;  // 退款原因
    
    @TableField("status")
    Integer status;  // 0:退款中 1:成功 2:失败
    
    @TableField("success_time")
    LocalDateTime successTime;  // 退款成功时间
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    LocalDateTime createdAt;
}
