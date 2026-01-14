package com.blue.jitian.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.jitian.Entity.OrderStatusLog;
import com.blue.jitian.Mapper.OrderStatusLogMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class OrderStatusLogService extends ServiceImpl<OrderStatusLogMapper, OrderStatusLog> {

    /**
     * 根据订单ID查询状态日志
     * @param orderId 订单ID
     * @return 状态日志列表
     */
    public List<OrderStatusLog> getLogsByOrderId(Long orderId) {
        return this.baseMapper.findByOrderId(orderId);
    }

    /**
     * 根据订单ID查询最新的状态日志
     * @param orderId 订单ID
     * @return 状态日志
     */
    public OrderStatusLog getLatestLogByOrderId(Long orderId) {
        return this.baseMapper.findLatestByOrderId(orderId);
    }

    /**
     * 根据操作人查询日志
     * @param operatorType 操作人类型
     * @param operatorId 操作人ID
     * @return 状态日志列表
     */
    public List<OrderStatusLog> getLogsByOperator(Integer operatorType, Long operatorId) {
        return this.baseMapper.findByOperator(operatorType, operatorId);
    }

    /**
     * 根据新状态查询日志
     * @param newStatus 新状态
     * @return 状态日志列表
     */
    public List<OrderStatusLog> getLogsByNewStatus(Integer newStatus) {
        return this.baseMapper.findByNewStatus(newStatus);
    }

    /**
     * 查询指定时间范围内的状态日志
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 状态日志列表
     */
    public List<OrderStatusLog> getLogsByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return this.baseMapper.findByTimeRange(startTime, endTime);
    }

    /**
     * 查询订单在指定状态的日志
     * @param orderId 订单ID
     * @param status 状态值
     * @return 状态日志列表
     */
    public List<OrderStatusLog> getLogsByOrderIdAndStatus(Long orderId, Integer status) {
        return this.baseMapper.findByOrderIdAndStatus(orderId, status);
    }

    /**
     * 添加状态日志
     * @param log 状态日志对象
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean addLog(OrderStatusLog log) {
        return this.save(log);
    }

    /**
     * 批量添加状态日志
     * @param logs 状态日志列表
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean batchAddLogs(List<OrderStatusLog> logs) {
        if (logs == null || logs.isEmpty()) {
            return false;
        }
        return this.baseMapper.batchInsert(logs) > 0;
    }

    /**
     * 记录订单状态变更
     * @param orderId 订单ID
     * @param oldStatus 旧状态
     * @param newStatus 新状态
     * @param operatorType 操作人类型
     * @param operatorId 操作人ID
     * @param remark 备注
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean recordStatusChange(Long orderId, Integer oldStatus, Integer newStatus, 
                                      Integer operatorType, Long operatorId, String remark) {
        OrderStatusLog log = OrderStatusLog.builder()
                .order_id(orderId)
                .old_status(oldStatus)
                .new_status(newStatus)
                .operator_type(operatorType)
                .operator_id(operatorId)
                .remark(remark)
                .build();
        return this.save(log);
    }

    /**
     * 删除状态日志
     * @param logId 日志ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteLog(Long logId) {
        return this.removeById(logId);
    }

    /**
     * 删除订单的所有状态日志
     * @param orderId 订单ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByOrderId(Long orderId) {
        return this.baseMapper.deleteByOrderId(orderId) > 0;
    }

    /**
     * 统计订单的状态变更次数
     * @param orderId 订单ID
     * @return 变更次数
     */
    public long countByOrderId(Long orderId) {
        return this.baseMapper.countByOrderId(orderId);
    }
}
