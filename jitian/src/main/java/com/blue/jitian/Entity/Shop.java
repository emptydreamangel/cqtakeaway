package com.blue.jitian.Entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("shop_id")
    Long shopId;
    
    @TableField("category_id")
    @JsonProperty("category_id")
    Integer categoryId;
    
    @TableField("shop_name")
    @JsonProperty("shop_name")
    String shopName;
    
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
    BigDecimal longitude;
    
    @TableField("latitude")
    BigDecimal latitude;
    
    @TableField("phone")
    String phone;
    
    @TableField("business_hours")
    @JsonProperty("business_hours")
    String businessHours;
    
    @TableField("delivery_time")
    @JsonProperty("delivery_time")
    Integer deliveryTime;
    
    @TableField("min_order_amount")
    @JsonProperty("min_order_amount")
    BigDecimal minOrderAmount;
    
    @TableField("delivery_fee")
    @JsonProperty("delivery_fee")
    BigDecimal deliveryFee;
    
    @TableField("packing_fee")
    @JsonProperty("packing_fee")
    BigDecimal packingFee;
    
    @TableField("rating")
    BigDecimal rating;
    
    @TableField("sales_count")
    @JsonProperty("sales_count")
    Integer salesCount;
    
    @TableField("status")
    Integer status;
    
    @TableField("is_auth")
    @JsonProperty("is_auth")
    Integer isAuth;
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    @JsonProperty("created_at")
    LocalDateTime createdAt;
    
    @TableField(value = "updated_at", fill = FieldFill.UPDATE)
    @JsonProperty("updated_at")
    LocalDateTime updatedAt;
}
