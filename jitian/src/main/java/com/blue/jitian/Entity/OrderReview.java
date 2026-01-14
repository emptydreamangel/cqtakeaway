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
@TableName("order_reviews")
public class OrderReview {
    @TableId(type = IdType.AUTO)
    @TableField("review_id")
    Long review_id;
    
    @TableField("order_id")
    Long order_id;  // 订单ID，唯一索引
    
    @TableField("user_id")
    Long user_id;  // 用户ID
    
    @TableField("shop_id")
    Long shop_id;  // 商家ID
    
    @TableField("rider_id")
    Long rider_id;  // 骑手ID
    
    @TableField("shop_rating")
    Integer shop_rating;  // 商家评分：1-5分
    
    @TableField("delivery_rating")
    Integer delivery_rating;  // 配送评分：1-5分
    
    @TableField("taste_rating")
    Integer taste_rating;  // 口味评分：1-5分
    
    @TableField("content")
    String content;  // 评价内容
    
    @TableField("images")
    String images;  // 评价图片（JSONB存储为JSON字符串）
    
    @TableField("is_anonymous")
    Integer is_anonymous;  // 是否匿名：0:否 1:是
    
    @TableField("like_count")
    Integer like_count;  // 点赞数
    
    @TableField("reply_content")
    String reply_content;  // 商家回复内容
    
    @TableField("reply_time")
    LocalDateTime reply_time;  // 商家回复时间
    
    @TableField("status")
    Integer status;  // 状态：0:隐藏 1:显示
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    LocalDateTime created_at;
    
    @TableField(value = "updated_at", fill = FieldFill.UPDATE)
    LocalDateTime updated_at;
}
