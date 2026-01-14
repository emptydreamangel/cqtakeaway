package com.blue.jitian.Entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Builder
@Data
@TableName("shop_categories")
public class ShopCategory {
    @TableId(type = IdType.AUTO)
    @TableField("category_id")
    Integer category_id;
    
    @TableField("category_name")
    String category_name;
    
    @TableField("icon")
    String icon;
    
    @TableField("sort_order")
    Integer sort_order;  // 排序，数字越小越靠前
    
    @TableField("status")
    Integer status;  // 0: 禁用, 1: 正常
}
