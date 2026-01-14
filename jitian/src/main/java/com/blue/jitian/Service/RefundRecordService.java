package com.blue.jitian.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.jitian.Entity.RefundRecord;
import com.blue.jitian.Mapper.RefundRecordMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
public class RefundRecordService extends ServiceImpl<RefundRecordMapper, RefundRecord> {

    /**
     * 创建退款记录
     * @param orderId 订单ID
     * @param userId 用户ID
     * @param paymentId 支付记录ID
     * @param refundAmount 退款金额
     * @param refundReason 退款原因
     * @return RefundRecord对象
     */
    @Transactional
    public RefundRecord createRefund(Long orderId, Long userId, Long paymentId, 
                                      BigDecimal refundAmount, String refundReason) {
        // 生成唯一的商户退款单号
        String outRefundNo = generateOutRefundNo();
        
        RefundRecord refundRecord = RefundRecord.builder()
                .orderId(orderId)
                .userId(userId)
                .paymentId(paymentId)
                .outRefundNo(outRefundNo)
                .refundAmount(refundAmount)
                .refundReason(refundReason)
                .status(0)  // 退款中
                .build();
        
        if (this.save(refundRecord)) {
            return refundRecord;
        }
        return null;
    }

    /**
     * 生成商户退款单号
     * @return 商户退款单号
     */
    private String generateOutRefundNo() {
        // 格式: REF + 时间戳 + 随机UUID（取前8位）
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return "REF" + timestamp + uuid;
    }

    /**
     * 更新退款状态为成功
     * @param outRefundNo 商户退款单号
     * @param refundNo 第三方退款流水号
     * @return 更新是否成功
     */
    @Transactional
    public boolean updateRefundSuccess(String outRefundNo, String refundNo) {
        LambdaUpdateWrapper<RefundRecord> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(RefundRecord::getOutRefundNo, outRefundNo)
                .set(RefundRecord::getStatus, 1)
                .set(RefundRecord::getRefundNo, refundNo)
                .set(RefundRecord::getSuccessTime, LocalDateTime.now());
        return this.update(wrapper);
    }

    /**
     * 更新退款状态为失败
     * @param outRefundNo 商户退款单号
     * @return 更新是否成功
     */
    @Transactional
    public boolean updateRefundFailed(String outRefundNo) {
        LambdaUpdateWrapper<RefundRecord> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(RefundRecord::getOutRefundNo, outRefundNo)
                .set(RefundRecord::getStatus, 2);
        return this.update(wrapper);
    }

    /**
     * 根据商户退款单号查询退款记录
     * @param outRefundNo 商户退款单号
     * @return RefundRecord对象
     */
    public RefundRecord getByOutRefundNo(String outRefundNo) {
        return this.lambdaQuery()
                .eq(RefundRecord::getOutRefundNo, outRefundNo)
                .one();
    }

    /**
     * 根据订单ID查询退款记录
     * @param orderId 订单ID
     * @return RefundRecord列表
     */
    public List<RefundRecord> getByOrderId(Long orderId) {
        return this.lambdaQuery()
                .eq(RefundRecord::getOrderId, orderId)
                .orderByDesc(RefundRecord::getCreatedAt)
                .list();
    }

    /**
     * 根据用户ID查询退款记录
     * @param userId 用户ID
     * @param status 状态（可选）
     * @return RefundRecord列表
     */
    public List<RefundRecord> getByUserId(Long userId, Integer status) {
        LambdaQueryWrapper<RefundRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RefundRecord::getUserId, userId);
        if (status != null) {
            wrapper.eq(RefundRecord::getStatus, status);
        }
        wrapper.orderByDesc(RefundRecord::getCreatedAt);
        return this.list(wrapper);
    }

    /**
     * 根据支付记录ID查询退款记录
     * @param paymentId 支付记录ID
     * @return RefundRecord列表
     */
    public List<RefundRecord> getByPaymentId(Long paymentId) {
        return this.lambdaQuery()
                .eq(RefundRecord::getPaymentId, paymentId)
                .orderByDesc(RefundRecord::getCreatedAt)
                .list();
    }

    /**
     * 分页查询退款记录
     * @param current 当前页
     * @param size 每页大小
     * @param userId 用户ID（可选）
     * @param orderId 订单ID（可选）
     * @param status 状态（可选）
     * @return 分页结果
     */
    public Page<RefundRecord> getRefundPage(long current, long size, Long userId, 
                                             Long orderId, Integer status) {
        Page<RefundRecord> page = new Page<>(current, size);
        LambdaQueryWrapper<RefundRecord> wrapper = new LambdaQueryWrapper<>();
        
        if (userId != null) {
            wrapper.eq(RefundRecord::getUserId, userId);
        }
        if (orderId != null) {
            wrapper.eq(RefundRecord::getOrderId, orderId);
        }
        if (status != null) {
            wrapper.eq(RefundRecord::getStatus, status);
        }
        
        wrapper.orderByDesc(RefundRecord::getCreatedAt);
        return this.page(page, wrapper);
    }

    /**
     * 统计各退款状态的记录数量
     * @return 统计结果
     */
    public List<Map<String, Object>> countByStatus() {
        return this.baseMapper.countByStatus();
    }

    /**
     * 查询用户的退款记录统计
     * @param userId 用户ID
     * @return 统计结果
     */
    public List<Map<String, Object>> countByUserIdAndStatus(Long userId) {
        return this.baseMapper.countByUserIdAndStatus(userId);
    }

    /**
     * 查询指定时间范围内的退款成功总金额
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 总金额
     */
    public BigDecimal sumSuccessAmountByTimeRange(String startTime, String endTime) {
        return this.baseMapper.sumSuccessAmountByTimeRange(startTime, endTime);
    }

    /**
     * 按日期统计退款成功的订单数和金额
     * @param days 统计最近N天
     * @return 统计结果
     */
    public List<Map<String, Object>> dailySuccessStats(int days) {
        return this.baseMapper.dailySuccessStats(days);
    }

    /**
     * 查询退款中超时的记录
     * @param hours 超时小时数
     * @return 超时记录列表
     */
    public List<RefundRecord> findTimeoutRefunds(int hours) {
        return this.baseMapper.findTimeoutRefunds(hours);
    }

    /**
     * 检查订单是否已有成功的退款记录
     * @param orderId 订单ID
     * @return 存在返回true，否则返回false
     */
    public boolean hasSuccessRefund(Long orderId) {
        return this.lambdaQuery()
                .eq(RefundRecord::getOrderId, orderId)
                .eq(RefundRecord::getStatus, 1)
                .count() > 0;
    }

    /**
     * 检查订单是否有进行中的退款
     * @param orderId 订单ID
     * @return 存在返回true，否则返回false
     */
    public boolean hasProcessingRefund(Long orderId) {
        return this.lambdaQuery()
                .eq(RefundRecord::getOrderId, orderId)
                .eq(RefundRecord::getStatus, 0)
                .count() > 0;
    }

    /**
     * 根据第三方退款流水号查询退款记录
     * @param refundNo 第三方退款流水号
     * @return RefundRecord对象
     */
    public RefundRecord getByRefundNo(String refundNo) {
        return this.lambdaQuery()
                .eq(RefundRecord::getRefundNo, refundNo)
                .one();
    }

    /**
     * 计算用户累计退款成功金额
     * @param userId 用户ID
     * @return 累计金额
     */
    public BigDecimal sumSuccessAmountByUserId(Long userId) {
        List<RefundRecord> records = this.lambdaQuery()
                .eq(RefundRecord::getUserId, userId)
                .eq(RefundRecord::getStatus, 1)
                .list();
        
        return records.stream()
                .map(RefundRecord::getRefundAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 统计退款原因分布
     * @return 统计结果
     */
    public List<Map<String, Object>> countByRefundReason() {
        return this.baseMapper.countByRefundReason();
    }

    /**
     * 批量更新超时退款记录状态为失败
     * @param hours 超时小时数
     * @return 更新数量
     */
    @Transactional
    public int updateTimeoutRefunds(int hours) {
        List<RefundRecord> timeoutRefunds = findTimeoutRefunds(hours);
        if (timeoutRefunds.isEmpty()) {
            return 0;
        }
        
        LambdaUpdateWrapper<RefundRecord> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(RefundRecord::getStatus, 0)
                .lt(RefundRecord::getCreatedAt, LocalDateTime.now().minusHours(hours))
                .set(RefundRecord::getStatus, 2);
        
        return this.baseMapper.update(null, wrapper);
    }
}
