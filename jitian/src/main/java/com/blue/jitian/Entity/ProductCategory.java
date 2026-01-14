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
@TableName("product_categories")
public class ProductCategory {
    @TableId(type = IdType.AUTO)
    @TableField("category_id")
    Long category_id;
    
    @TableField("shop_id")
    Long shop_id;
    
    @TableField("category_name")
    String category_name;
    
    @TableField("sort_order")
    Integer sort_order;  // 排序
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    LocalDateTime created_at;
}
