package com.blue.jitian.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.jitian.Entity.PaymentRecord;
import com.blue.jitian.Mapper.PaymentRecordMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
public class PaymentRecordService extends ServiceImpl<PaymentRecordMapper, PaymentRecord> {

    /**
     * 创建支付记录
     * @param orderId 订单ID
     * @param userId 用户ID
     * @param amount 支付金额
     * @param payMethod 支付方式
     * @return PaymentRecord对象
     */
    @Transactional
    public PaymentRecord createPayment(Long orderId, Long userId, BigDecimal amount, Integer payMethod) {
        // 生成唯一的商户订单号
        String outTradeNo = generateOutTradeNo();
        
        PaymentRecord paymentRecord = PaymentRecord.builder()
                .orderId(orderId)
                .userId(userId)
                .outTradeNo(outTradeNo)
                .payMethod(payMethod)
                .amount(amount)
                .status(0)  // 待支付
                .build();
        
        if (this.save(paymentRecord)) {
            return paymentRecord;
        }
        return null;
    }

    /**
     * 生成商户订单号
     * @return 商户订单号
     */
    private String generateOutTradeNo() {
        // 格式: PAY + 时间戳 + 随机UUID（取前8位）
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return "PAY" + timestamp + uuid;
    }

    /**
     * 更新支付状态为成功
     * @param outTradeNo 商户订单号
     * @param transactionNo 第三方交易流水号
     * @return 更新是否成功
     */
    @Transactional
    public boolean updatePaymentSuccess(String outTradeNo, String transactionNo) {
        LambdaUpdateWrapper<PaymentRecord> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(PaymentRecord::getOutTradeNo, outTradeNo)
                .set(PaymentRecord::getStatus, 1)
                .set(PaymentRecord::getTransactionNo, transactionNo)
                .set(PaymentRecord::getNotifyTime, LocalDateTime.now());
        return this.update(wrapper);
    }

    /**
     * 更新支付状态为失败
     * @param outTradeNo 商户订单号
     * @return 更新是否成功
     */
    @Transactional
    public boolean updatePaymentFailed(String outTradeNo) {
        LambdaUpdateWrapper<PaymentRecord> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(PaymentRecord::getOutTradeNo, outTradeNo)
                .set(PaymentRecord::getStatus, 2)
                .set(PaymentRecord::getNotifyTime, LocalDateTime.now());
        return this.update(wrapper);
    }

    /**
     * 更新支付状态为退款
     * @param paymentId 支付记录ID
     * @return 更新是否成功
     */
    @Transactional
    public boolean updatePaymentRefund(Long paymentId) {
        PaymentRecord paymentRecord = new PaymentRecord();
        paymentRecord.setPaymentId(paymentId);
        paymentRecord.setStatus(3);
        return this.updateById(paymentRecord);
    }

    /**
     * 根据商户订单号查询支付记录
     * @param outTradeNo 商户订单号
     * @return PaymentRecord对象
     */
    public PaymentRecord getByOutTradeNo(String outTradeNo) {
        return this.lambdaQuery()
                .eq(PaymentRecord::getOutTradeNo, outTradeNo)
                .one();
    }

    /**
     * 根据订单ID查询支付记录
     * @param orderId 订单ID
     * @return PaymentRecord列表
     */
    public List<PaymentRecord> getByOrderId(Long orderId) {
        return this.lambdaQuery()
                .eq(PaymentRecord::getOrderId, orderId)
                .orderByDesc(PaymentRecord::getCreatedAt)
                .list();
    }

    /**
     * 根据用户ID查询支付记录
     * @param userId 用户ID
     * @param status 状态（可选）
     * @return PaymentRecord列表
     */
    public List<PaymentRecord> getByUserId(Long userId, Integer status) {
        LambdaQueryWrapper<PaymentRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentRecord::getUserId, userId);
        if (status != null) {
            wrapper.eq(PaymentRecord::getStatus, status);
        }
        wrapper.orderByDesc(PaymentRecord::getCreatedAt);
        return this.list(wrapper);
    }

    /**
     * 分页查询支付记录
     * @param current 当前页
     * @param size 每页大小
     * @param userId 用户ID（可选）
     * @param orderId 订单ID（可选）
     * @param status 状态（可选）
     * @param payMethod 支付方式（可选）
     * @return 分页结果
     */
    public Page<PaymentRecord> getPaymentPage(long current, long size, Long userId, 
                                               Long orderId, Integer status, Integer payMethod) {
        Page<PaymentRecord> page = new Page<>(current, size);
        LambdaQueryWrapper<PaymentRecord> wrapper = new LambdaQueryWrapper<>();
        
        if (userId != null) {
            wrapper.eq(PaymentRecord::getUserId, userId);
        }
        if (orderId != null) {
            wrapper.eq(PaymentRecord::getOrderId, orderId);
        }
        if (status != null) {
            wrapper.eq(PaymentRecord::getStatus, status);
        }
        if (payMethod != null) {
            wrapper.eq(PaymentRecord::getPayMethod, payMethod);
        }
        
        wrapper.orderByDesc(PaymentRecord::getCreatedAt);
        return this.page(page, wrapper);
    }

    /**
     * 统计各支付状态的记录数量
     * @return 统计结果
     */
    public List<Map<String, Object>> countByStatus() {
        return this.baseMapper.countByStatus();
    }

    /**
     * 统计各支付方式的记录数量和金额
     * @return 统计结果
     */
    public List<Map<String, Object>> countByPayMethod() {
        return this.baseMapper.countByPayMethod();
    }

    /**
     * 查询用户的支付记录统计
     * @param userId 用户ID
     * @return 统计结果
     */
    public List<Map<String, Object>> countByUserIdAndStatus(Long userId) {
        return this.baseMapper.countByUserIdAndStatus(userId);
    }

    /**
     * 查询指定时间范围内的支付成功总金额
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 总金额
     */
    public BigDecimal sumSuccessAmountByTimeRange(String startTime, String endTime) {
        return this.baseMapper.sumSuccessAmountByTimeRange(startTime, endTime);
    }

    /**
     * 查询待支付超时的记录
     * @param minutes 超时分钟数
     * @return 超时记录列表
     */
    public List<PaymentRecord> findTimeoutPayments(int minutes) {
        return this.baseMapper.findTimeoutPayments(minutes);
    }

    /**
     * 批量更新超时支付记录状态为失败
     * @param minutes 超时分钟数
     * @return 更新数量
     */
    @Transactional
    public int updateTimeoutPayments(int minutes) {
        List<PaymentRecord> timeoutPayments = findTimeoutPayments(minutes);
        if (timeoutPayments.isEmpty()) {
            return 0;
        }
        
        LambdaUpdateWrapper<PaymentRecord> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(PaymentRecord::getStatus, 0)
                .lt(PaymentRecord::getCreatedAt, LocalDateTime.now().minusMinutes(minutes))
                .set(PaymentRecord::getStatus, 2)
                .set(PaymentRecord::getNotifyTime, LocalDateTime.now());
        
        return this.baseMapper.update(null, wrapper);
    }

    /**
     * 按日期统计支付成功的订单数和金额
     * @param days 统计最近N天
     * @return 统计结果
     */
    public List<Map<String, Object>> dailySuccessStats(int days) {
        return this.baseMapper.dailySuccessStats(days);
    }

    /**
     * 检查订单是否已有成功的支付记录
     * @param orderId 订单ID
     * @return 存在返回true，否则返回false
     */
    public boolean hasSuccessPayment(Long orderId) {
        return this.lambdaQuery()
                .eq(PaymentRecord::getOrderId, orderId)
                .eq(PaymentRecord::getStatus, 1)
                .count() > 0;
    }

    /**
     * 根据第三方交易流水号查询支付记录
     * @param transactionNo 第三方交易流水号
     * @return PaymentRecord对象
     */
    public PaymentRecord getByTransactionNo(String transactionNo) {
        return this.lambdaQuery()
                .eq(PaymentRecord::getTransactionNo, transactionNo)
                .one();
    }

    /**
     * 计算用户累计支付成功金额
     * @param userId 用户ID
     * @return 累计金额
     */
    public BigDecimal sumSuccessAmountByUserId(Long userId) {
        List<PaymentRecord> records = this.lambdaQuery()
                .eq(PaymentRecord::getUserId, userId)
                .eq(PaymentRecord::getStatus, 1)
                .list();
        
        return records.stream()
                .map(PaymentRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
