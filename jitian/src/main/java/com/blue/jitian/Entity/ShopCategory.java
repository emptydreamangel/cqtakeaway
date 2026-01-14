package com.blue.jitian.Entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("category_id")
    Integer categoryId;
    
    @TableField("category_name")
    @JsonProperty("category_name")
    String categoryName;
    
    @TableField("icon")
    String icon;
    
    @TableField("sort_order")
    @JsonProperty("sort_order")
    Integer sortOrder;
    
    @TableField("status")
    Integer status;
}
