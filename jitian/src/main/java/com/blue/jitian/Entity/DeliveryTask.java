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
@TableName("delivery_tasks")
public class DeliveryTask {
    @TableId(type = IdType.AUTO)
    @TableField("task_id")
    Long task_id;
    
    @TableField("order_id")
    Long order_id;  // 订单ID
    
    @TableField("rider_id")
    Long rider_id;  // 骑手ID
    
    @TableField("shop_id")
    Long shop_id;  // 商家ID
    
    @TableField("shop_address")
    String shop_address;  // 商家地址
    
    @TableField("shop_longitude")
    BigDecimal shop_longitude;  // 商家经度
    
    @TableField("shop_latitude")
    BigDecimal shop_latitude;  // 商家纬度
    
    @TableField("user_id")
    Long user_id;  // 用户ID
    
    @TableField("receiver_name")
    String receiver_name;  // 收货人姓名
    
    @TableField("receiver_phone")
    String receiver_phone;  // 收货人电话
    
    @TableField("receiver_address")
    String receiver_address;  // 收货地址
    
    @TableField("receiver_longitude")
    BigDecimal receiver_longitude;  // 收货经度
    
    @TableField("receiver_latitude")
    BigDecimal receiver_latitude;  // 收货纬度
    
    @TableField("distance")
    Integer distance;  // 配送距离（米）
    
    @TableField("delivery_fee")
    BigDecimal delivery_fee;  // 配送费
    
    @TableField("rider_income")
    BigDecimal rider_income;  // 骑手收入
    
    @TableField("status")
    Integer status;  // 状态：0:待接单 1:已接单 2:已取餐 3:配送中 4:已送达
    
    @TableField("accept_time")
    LocalDateTime accept_time;  // 接单时间
    
    @TableField("pickup_time")
    LocalDateTime pickup_time;  // 取餐时间
    
    @TableField("complete_time")
    LocalDateTime complete_time;  // 完成时间
    
    @TableField("cancel_time")
    LocalDateTime cancel_time;  // 取消时间
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    LocalDateTime created_at;
    
    @TableField(value = "updated_at", fill = FieldFill.UPDATE)
    LocalDateTime updated_at;
}
