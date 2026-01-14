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
@TableName("order_status_log")
public class OrderStatusLog {
    @TableId(type = IdType.AUTO)
    @TableField("log_id")
    Long log_id;
    
    @TableField("order_id")
    Long order_id;
    
    @TableField("old_status")
    Integer old_status;  // 旧状态
    
    @TableField("new_status")
    Integer new_status;  // 新状态
    
    @TableField("operator_type")
    Integer operator_type;  // 操作人类型：1:用户 2:商家 3:骑手 4:系统
    
    @TableField("operator_id")
    Long operator_id;  // 操作人ID
    
    @TableField("remark")
    String remark;  // 备注
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    LocalDateTime created_at;
}
