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
@TableName("user_favorites")
public class UserFavorite {
    @TableId(type = IdType.AUTO)
    @TableField("favorite_id")
    Long favorite_id;
    
    @TableField("user_id")
    Long user_id;
    
    @TableField("shop_id")
    Long shop_id;
    
    @TableField("product_id")
    Long product_id;
    
    @TableField("type")
    Integer type;  // 1: 店铺, 2: 商品
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    LocalDateTime created_at;
}
