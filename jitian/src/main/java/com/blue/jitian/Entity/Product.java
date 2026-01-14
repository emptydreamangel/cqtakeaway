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
@TableName("products")
public class Product {
    @TableId(type = IdType.AUTO)
    @TableField("product_id")
    Long product_id;
    
    @TableField("shop_id")
    Long shop_id;
    
    @TableField("category_id")
    Long category_id;
    
    @TableField("product_name")
    String product_name;
    
    @TableField("description")
    String description;
    
    @TableField("main_image")
    String main_image;  // 主图
    
    @TableField("images")
    String images;  // 图片数组，JSON格式字符串
    
    @TableField("price")
    BigDecimal price;  // 现价
    
    @TableField("original_price")
    BigDecimal original_price;  // 原价
    
    @TableField("stock")
    Integer stock;  // 库存，-1表示无限
    
    @TableField("sales_count")
    Integer sales_count;  // 销量
    
    @TableField("status")
    Integer status;  // 0: 下架, 1: 上架
    
    @TableField("sort_order")
    Integer sort_order;  // 排序
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    LocalDateTime created_at;
    
    @TableField(value = "updated_at", fill = FieldFill.UPDATE)
    LocalDateTime updated_at;
}
