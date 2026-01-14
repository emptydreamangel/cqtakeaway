package com.blue.jitian.Entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Builder
@Data
@TableName("user_coupons")
public class UserCoupon {
    @TableId(type = IdType.AUTO)
    @TableField("user_coupon_id")
    Long userCouponId;
    
    @TableField("user_id")
    Long userId;
    
    @TableField("coupon_id")
    Long couponId;
    
    @TableField("status")
    Integer status;  // 0:未使用 1:已使用 2:已过期
    
    @TableField("order_id")
    Long orderId;
    
    @TableField(value = "receive_time", fill = FieldFill.INSERT)
    LocalDateTime receiveTime;
    
    @TableField("use_time")
    LocalDateTime useTime;
    
    @TableField("expire_time")
    LocalDateTime expireTime;
}
