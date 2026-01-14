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
@TableName("delivery_riders")
public class DeliveryRider {
    @TableId(type = IdType.AUTO)
    @TableField("rider_id")
    Long rider_id;
    
    @TableField("rider_name")
    String rider_name;
    
    @TableField("phone")
    String phone;  // 手机号，唯一
    
    @TableField("password_hash")
    String password_hash;  // 密码哈希
    
    @TableField("id_card")
    String id_card;  // 身份证号
    
    @TableField("avatar")
    String avatar;  // 头像
    
    @TableField("vehicle_type")
    Integer vehicle_type;  // 车辆类型：1:电动车 2:摩托车
    
    @TableField("vehicle_no")
    String vehicle_no;  // 车牌号
    
    @TableField("balance")
    BigDecimal balance;  // 余额
    
    @TableField("status")
    Integer status;  // 状态：0:休息 1:接单中 2:配送中
    
    @TableField("is_online")
    Integer is_online;  // 是否在线：0:离线 1:在线
    
    @TableField("current_longitude")
    BigDecimal current_longitude;  // 当前经度
    
    @TableField("current_latitude")
    BigDecimal current_latitude;  // 当前纬度
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    LocalDateTime created_at;
    
    @TableField(value = "updated_at", fill = FieldFill.UPDATE)
    LocalDateTime updated_at;
}
