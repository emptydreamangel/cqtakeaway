package com.blue.jitian.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blue.jitian.Entity.OrderStatusLog;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;


@Mapper
public interface OrderStatusLogMapper extends BaseMapper<OrderStatusLog> {

    /**
     * 根据订单ID查询状态日志
     * @param orderId 订单ID
     * @return 状态日志列表
     */
    @Select("SELECT * FROM order_status_log WHERE order_id = #{orderId} ORDER BY created_at ASC")
    List<OrderStatusLog> findByOrderId(@Param("orderId") Long orderId);

    /**
     * 根据订单ID查询最新的状态日志
     * @param orderId 订单ID
     * @return 状态日志
     */
    @Select("SELECT * FROM order_status_log WHERE order_id = #{orderId} ORDER BY created_at DESC LIMIT 1")
    OrderStatusLog findLatestByOrderId(@Param("orderId") Long orderId);

    /**
     * 根据操作人类型和操作人ID查询日志
     * @param operatorType 操作人类型
     * @param operatorId 操作人ID
     * @return 状态日志列表
     */
    @Select("SELECT * FROM order_status_log WHERE operator_type = #{operatorType} AND operator_id = #{operatorId} ORDER BY created_at DESC")
    List<OrderStatusLog> findByOperator(@Param("operatorType") Integer operatorType, @Param("operatorId") Long operatorId);

    /**
     * 根据新状态查询日志
     * @param newStatus 新状态
     * @return 状态日志列表
     */
    @Select("SELECT * FROM order_status_log WHERE new_status = #{newStatus} ORDER BY created_at DESC")
    List<OrderStatusLog> findByNewStatus(@Param("newStatus") Integer newStatus);

    /**
     * 查询指定时间范围内的状态日志
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 状态日志列表
     */
    @Select("SELECT * FROM order_status_log WHERE created_at >= #{startTime} AND created_at <= #{endTime} ORDER BY created_at DESC")
    List<OrderStatusLog> findByTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 统计订单的状态变更次数
     * @param orderId 订单ID
     * @return 变更次数
     */
    @Select("SELECT COUNT(*) FROM order_status_log WHERE order_id = #{orderId}")
    long countByOrderId(@Param("orderId") Long orderId);

    /**
     * 删除订单的所有状态日志
     * @param orderId 订单ID
     * @return 影响行数
     */
    @Delete("DELETE FROM order_status_log WHERE order_id = #{orderId}")
    int deleteByOrderId(@Param("orderId") Long orderId);

    /**
     * 查询订单在指定状态的日志
     * @param orderId 订单ID
     * @param status 状态值
     * @return 状态日志列表
     */
    @Select("SELECT * FROM order_status_log WHERE order_id = #{orderId} AND new_status = #{status} ORDER BY created_at DESC")
    List<OrderStatusLog> findByOrderIdAndStatus(@Param("orderId") Long orderId, @Param("status") Integer status);

    /**
     * 批量插入状态日志
     * @param logs 状态日志列表
     * @return 影响行数
     */
    @Insert("<script>" +
            "INSERT INTO order_status_log (order_id, old_status, new_status, operator_type, operator_id, remark) VALUES " +
            "<foreach collection='logs' item='log' separator=','>" +
            "(#{log.order_id}, #{log.old_status}, #{log.new_status}, #{log.operator_type}, #{log.operator_id}, #{log.remark})" +
            "</foreach>" +
            "</script>")
    int batchInsert(@Param("logs") List<OrderStatusLog> logs);
}
