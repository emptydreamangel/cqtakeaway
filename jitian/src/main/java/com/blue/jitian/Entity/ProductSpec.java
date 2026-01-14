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
@TableName("product_specs")
public class ProductSpec {
    @TableId(type = IdType.AUTO)
    @TableField("spec_id")
    Long spec_id;
    
    @TableField("product_id")
    Long product_id;
    
    @TableField("spec_name")
    String spec_name;  // 规格名称，如"大份"、"中杯"
    
    @TableField("price_add")
    BigDecimal price_add;  // 加价金额
    
    @TableField("stock")
    Integer stock;  // 库存，-1表示无限
    
    @TableField("sort_order")
    Integer sort_order;  // 排序
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    LocalDateTime created_at;
}
