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
@TableName("coupons")
public class Coupon {
    @TableId(type = IdType.AUTO)
    @TableField("coupon_id")
    Long coupon_id;
    
    @TableField("coupon_name")
    String coupon_name;  // 优惠券名称
    
    @TableField("coupon_type")
    Integer coupon_type;  // 优惠券类型：1:满减 2:折扣 3:免配送费
    
    @TableField("min_amount")
    BigDecimal min_amount;  // 最低消费金额
    
    @TableField("discount_amount")
    BigDecimal discount_amount;  // 减免金额（满减券）
    
    @TableField("discount_rate")
    BigDecimal discount_rate;  // 折扣率（折扣券）
    
    @TableField("max_discount")
    BigDecimal max_discount;  // 最高减免金额（折扣券）
    
    @TableField("total_count")
    Integer total_count;  // 总发行数量
    
    @TableField("received_count")
    Integer received_count;  // 已领取数量
    
    @TableField("used_count")
    Integer used_count;  // 已使用数量
    
    @TableField("valid_type")
    Integer valid_type;  // 有效期类型：1:固定天数 2:固定日期
    
    @TableField("valid_days")
    Integer valid_days;  // 有效天数（valid_type=1时使用）
    
    @TableField("start_time")
    LocalDateTime start_time;  // 开始时间（valid_type=2时使用）
    
    @TableField("end_time")
    LocalDateTime end_time;  // 结束时间（valid_type=2时使用）
    
    @TableField("shop_id")
    Long shop_id;  // 商家ID（null表示平台券）
    
    @TableField("status")
    Integer status;  // 状态：0:已结束 1:进行中
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    LocalDateTime created_at;
}
