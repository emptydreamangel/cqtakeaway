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
@TableName("user_addresses")
public class UserAddress {
    @TableId(type = IdType.AUTO)
    @TableField("address_id")
    Long address_id;
    
    @TableField("user_id")
    Long user_id;
    
    @TableField("receiver_name")
    String receiver_name;
    
    @TableField("receiver_phone")
    String receiver_phone;
    
    @TableField("province")
    String province;
    
    @TableField("city")
    String city;
    
    @TableField("district")
    String district;
    
    @TableField("detail_address")
    String detail_address;
    
    @TableField("longitude")
    BigDecimal longitude;  // 经度
    
    @TableField("latitude")
    BigDecimal latitude;   // 纬度
    
    @TableField("is_default")
    Integer is_default;  // 0: 否, 1: 是
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    LocalDateTime created_at;
    
    @TableField(value = "updated_at", fill = FieldFill.UPDATE)
    LocalDateTime updated_at;
}
