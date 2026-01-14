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
@TableName("admins")
public class Admin {
    @TableId(type = IdType.AUTO)
    @TableField("admin_id")
    int admin_id;
    String username;
    String password_hash;
    String real_name;
    String phone;
    int role_id;
    int status;
    @TableField(value = "last_login_time")
    LocalDateTime last_login_time;
    @TableField(value = "created_at",fill = FieldFill.INSERT)
    LocalDateTime created_at;
    @TableField(value = "updated_at",fill = FieldFill.UPDATE)
    LocalDateTime updated_at;
}
