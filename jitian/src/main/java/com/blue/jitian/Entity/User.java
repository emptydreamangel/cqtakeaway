package com.blue.jitian.Entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@TableName("users")
public class User {

    @TableId(type = IdType.AUTO)
    @TableField("user_id")
    @JsonProperty("user_id")
    Long user_id;

    @TableField("phone")
    String phone;

    @TableField("password_hash")
    @JsonProperty("password")
    String password_hash;

    @TableField("nickname")
    String nickname;

    @TableField("avatar")
    String avatar;

    @TableField("gender")
    Integer gender;  // 0: 未知, 1: 男, 2: 女

    @TableField("status")
    Integer status;  // 1: 正常, 0: 禁用

    @TableField(value = "register_time", fill = FieldFill.INSERT)
    @JsonProperty("register_time")
    LocalDateTime register_time;

    @TableField(value = "last_login_time")
    @JsonProperty("last_login_time")
    LocalDateTime last_login_time;
}
