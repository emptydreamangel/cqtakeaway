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
@TableName("orders")
public class Order {
    @TableId(type = IdType.AUTO)
    @TableField("order_id")
    Long order_id;
    
    @TableField("order_no")
    String order_no;  // 订单号，唯一
    
    @TableField("user_id")
    Long user_id;
    
    @TableField("shop_id")
    Long shop_id;
    
    @TableField("total_amount")
    BigDecimal total_amount;  // 商品总金额
    
    @TableField("delivery_fee")
    BigDecimal delivery_fee;  // 配送费
    
    @TableField("packing_fee")
    BigDecimal packing_fee;  // 打包费
    
    @TableField("discount_amount")
    BigDecimal discount_amount;  // 优惠金额
    
    @TableField("coupon_discount")
    BigDecimal coupon_discount;  // 优惠券优惠金额
    
    @TableField("pay_amount")
    BigDecimal pay_amount;  // 实付金额
    
    @TableField("receiver_name")
    String receiver_name;  // 收货人姓名
    
    @TableField("receiver_phone")
    String receiver_phone;  // 收货人电话
    
    @TableField("receiver_address")
    String receiver_address;  // 收货地址
    
    @TableField("receiver_longitude")
    BigDecimal receiver_longitude;  // 收货地址经度
    
    @TableField("receiver_latitude")
    BigDecimal receiver_latitude;  // 收货地址纬度
    
    @TableField("remark")
    String remark;  // 订单备注
    
    @TableField("delivery_time")
    Integer delivery_time;  // 期望送达时间（分钟）
    
    @TableField("status")
    Integer status;  // 订单状态：0:待支付 1:待接单 2:待配送 3:配送中 4:已完成 5:已取消
    
    @TableField("pay_status")
    Integer pay_status;  // 支付状态：0:未支付 1:已支付 2:退款中 3:已退款
    
    @TableField("pay_time")
    LocalDateTime pay_time;  // 支付时间
    
    @TableField("pay_method")
    Integer pay_method;  // 支付方式：1:微信 2:支付宝
    
    @TableField("accept_time")
    LocalDateTime accept_time;  // 商家接单时间
    
    @TableField("dispatch_time")
    LocalDateTime dispatch_time;  // 配送员接单时间
    
    @TableField("complete_time")
    LocalDateTime complete_time;  // 完成时间
    
    @TableField("cancel_time")
    LocalDateTime cancel_time;  // 取消时间
    
    @TableField("cancel_reason")
    String cancel_reason;  // 取消原因
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    LocalDateTime created_at;
    
    @TableField(value = "updated_at", fill = FieldFill.UPDATE)
    LocalDateTime updated_at;
}
