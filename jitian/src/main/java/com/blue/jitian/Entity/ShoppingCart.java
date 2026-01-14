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
@TableName("shopping_carts")
public class ShoppingCart {
    @TableId(type = IdType.AUTO)
    @TableField("cart_id")
    Long cart_id;
    
    @TableField("user_id")
    Long user_id;
    
    @TableField("shop_id")
    Long shop_id;
    
    @TableField("product_id")
    Long product_id;
    
    @TableField("quantity")
    Integer quantity;  // 数量
    
    @TableField("spec_id")
    Long spec_id;  // 规格ID（可选）
    
    @TableField("options")
    String options;  // 额外选项，JSONB类型，存储JSON字符串
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    LocalDateTime created_at;
    
    @TableField(value = "updated_at", fill = FieldFill.UPDATE)
    LocalDateTime updated_at;
}
