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
@TableName("shops")
public class Shop {
    @TableId(type = IdType.AUTO)
    @TableField("shop_id")
    Long shop_id;
    
    @TableField("category_id")
    Integer category_id;
    
    @TableField("shop_name")
    String shop_name;
    
    @TableField("logo")
    String logo;
    
    @TableField("banner")
    String banner;
    
    @TableField("description")
    String description;
    
    @TableField("province")
    String province;
    
    @TableField("city")
    String city;
    
    @TableField("district")
    String district;
    
    @TableField("address")
    String address;
    
    @TableField("longitude")
    BigDecimal longitude;  // 经度
    
    @TableField("latitude")
    BigDecimal latitude;   // 纬度
    
    @TableField("phone")
    String phone;
    
    @TableField("business_hours")
    String business_hours;  // 营业时间，JSON格式字符串
    
    @TableField("delivery_time")
    Integer delivery_time;  // 预计配送时间（分钟）
    
    @TableField("min_order_amount")
    BigDecimal min_order_amount;  // 起送金额
    
    @TableField("delivery_fee")
    BigDecimal delivery_fee;  // 配送费
    
    @TableField("packing_fee")
    BigDecimal packing_fee;  // 打包费
    
    @TableField("rating")
    BigDecimal rating;  // 评分
    
    @TableField("sales_count")
    Integer sales_count;  // 销量
    
    @TableField("status")
    Integer status;  // 0: 休息中, 1: 营业中, 2: 打烊
    
    @TableField("is_auth")
    Integer is_auth;  // 0: 未认证, 1: 已认证
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    LocalDateTime created_at;
    
    @TableField(value = "updated_at", fill = FieldFill.UPDATE)
    LocalDateTime updated_at;
}
