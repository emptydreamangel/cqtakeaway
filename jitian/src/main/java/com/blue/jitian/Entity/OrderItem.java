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
@TableName("order_items")
public class OrderItem {
    @TableId(type = IdType.AUTO)
    @TableField("item_id")
    Long item_id;
    
    @TableField("order_id")
    Long order_id;
    
    @TableField("product_id")
    Long product_id;
    
    @TableField("product_name")
    String product_name;  // 商品名称快照
    
    @TableField("product_image")
    String product_image;  // 商品图片快照
    
    @TableField("product_price")
    BigDecimal product_price;  // 商品单价快照
    
    @TableField("quantity")
    Integer quantity;  // 购买数量
    
    @TableField("spec_name")
    String spec_name;  // 规格名称（可选）
    
    @TableField("options")
    String options;  // 选项详情，JSONB类型，存储JSON字符串
    
    @TableField("subtotal")
    BigDecimal subtotal;  // 小计金额
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    LocalDateTime created_at;
}
