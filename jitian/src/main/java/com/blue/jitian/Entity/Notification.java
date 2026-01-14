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
@TableName("notifications")
public class Notification {
    @TableId(type = IdType.AUTO)
    @TableField("notification_id")
    Long notificationId;
    
    @TableField("user_id")
    Long userId;
    
    @TableField("notification_type")
    Integer notificationType;  // 1:订单 2:活动 3:系统
    
    @TableField("title")
    String title;
    
    @TableField("content")
    String content;
    
    @TableField("link_url")
    String linkUrl;
    
    @TableField("is_read")
    Integer isRead;  // 0:未读 1:已读
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    LocalDateTime createdAt;
}
