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
@TableName("shop_images")
public class ShopImage {
    @TableId(type = IdType.AUTO)
    @TableField("image_id")
    Long image_id;
    
    @TableField("shop_id")
    Long shop_id;
    
    @TableField("image_url")
    String image_url;
    
    @TableField("image_type")
    Integer image_type;  // 1: 环境图, 2: 菜品图
    
    @TableField("sort_order")
    Integer sort_order;  // 排序
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    LocalDateTime created_at;
}
