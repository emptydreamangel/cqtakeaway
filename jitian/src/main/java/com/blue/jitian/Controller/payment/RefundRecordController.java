package com.blue.jitian.Controller.payment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blue.jitian.Entity.RefundRecord;
import com.blue.jitian.Service.RefundRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/refund-records")
public class RefundRecordController {

    @Autowired
    private RefundRecordService refundRecordService;

    /**
     * 创建退款记录
     * @param orderId 订单ID
     * @param userId 用户ID
     * @param paymentId 支付记录ID
     * @param refundAmount 退款金额
     * @param refundReason 退款原因
     * @return 创建结果
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createRefund(@RequestParam Long orderId,
                                                              @RequestParam Long userId,
                                                              @RequestParam Long paymentId,
                                                              @RequestParam BigDecimal refundAmount,
                                                              @RequestParam(required = false) String refundReason) {
        Map<String, Object> response = new HashMap<>();
        RefundRecord refundRecord = refundRecordService.createRefund(orderId, userId, paymentId, 
                                                                       refundAmount, refundReason);
        if (refundRecord != null) {
            response.put("success", true);
            response.put("message", "退款申请已提交");
            response.put("data", refundRecord);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "退款申请失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 退款成功回调（模拟）
     * @param outRefundNo 商户退款单号
     * @param refundNo 第三方退款流水号
     * @return 更新结果
     */
    @PostMapping("/notify/success")
    public ResponseEntity<Map<String, Object>> refundSuccess(@RequestParam String outRefundNo,
                                                               @RequestParam String refundNo) {
        Map<String, Object> response = new HashMap<>();
        boolean success = refundRecordService.updateRefundSuccess(outRefundNo, refundNo);
        if (success) {
            response.put("success", true);
            response.put("message", "退款成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "更新失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 退款失败回调（模拟）
     * @param outRefundNo 商户退款单号
     * @return 更新结果
     */
    @PostMapping("/notify/failed")
    public ResponseEntity<Map<String, Object>> refundFailed(@RequestParam String outRefundNo) {
        Map<String, Object> response = new HashMap<>();
        boolean success = refundRecordService.updateRefundFailed(outRefundNo);
        if (success) {
            response.put("success", true);
            response.put("message", "退款失败状态已更新");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "更新失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 根据商户退款单号查询退款记录
     * @param outRefundNo 商户退款单号
     * @return RefundRecord对象
     */
    @GetMapping("/out-refund-no/{outRefundNo}")
    public ResponseEntity<RefundRecord> getByOutRefundNo(@PathVariable String outRefundNo) {
        RefundRecord refundRecord = refundRecordService.getByOutRefundNo(outRefundNo);
        if (refundRecord != null) {
            return ResponseEntity.ok(refundRecord);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 根据订单ID查询退款记录
     * @param orderId 订单ID
     * @return RefundRecord列表
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<RefundRecord>> getByOrderId(@PathVariable Long orderId) {
        List<RefundRecord> records = refundRecordService.getByOrderId(orderId);
        return ResponseEntity.ok(records);
    }

    /**
     * 根据用户ID查询退款记录
     * @param userId 用户ID
     * @param status 状态（可选）
     * @return RefundRecord列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RefundRecord>> getByUserId(@PathVariable Long userId,
                                                            @RequestParam(required = false) Integer status) {
        List<RefundRecord> records = refundRecordService.getByUserId(userId, status);
        return ResponseEntity.ok(records);
    }

    /**
     * 根据支付记录ID查询退款记录
     * @param paymentId 支付记录ID
     * @return RefundRecord列表
     */
    @GetMapping("/payment/{paymentId}")
    public ResponseEntity<List<RefundRecord>> getByPaymentId(@PathVariable Long paymentId) {
        List<RefundRecord> records = refundRecordService.getByPaymentId(paymentId);
        return ResponseEntity.ok(records);
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
    @GetMapping("/page")
    public ResponseEntity<Page<RefundRecord>> getRefundPage(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long orderId,
            @RequestParam(required = false) Integer status) {
        Page<RefundRecord> page = refundRecordService.getRefundPage(current, size, userId, orderId, status);
        return ResponseEntity.ok(page);
    }

    /**
     * 根据ID查询退款记录详情
     * @param id 退款记录ID
     * @return RefundRecord对象
     */
    @GetMapping("/{id}")
    public ResponseEntity<RefundRecord> getById(@PathVariable Long id) {
        RefundRecord refundRecord = refundRecordService.getById(id);
        if (refundRecord != null) {
            return ResponseEntity.ok(refundRecord);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 统计各退款状态的记录数量
     * @return 统计结果
     */
    @GetMapping("/stats/status")
    public ResponseEntity<List<Map<String, Object>>> countByStatus() {
        List<Map<String, Object>> stats = refundRecordService.countByStatus();
        return ResponseEntity.ok(stats);
    }

    /**
     * 查询用户的退款记录统计
     * @param userId 用户ID
     * @return 统计结果
     */
    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<List<Map<String, Object>>> countByUserIdAndStatus(@PathVariable Long userId) {
        List<Map<String, Object>> stats = refundRecordService.countByUserIdAndStatus(userId);
        return ResponseEntity.ok(stats);
    }

    /**
     * 查询指定时间范围内的退款成功总金额
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 总金额
     */
    @GetMapping("/stats/amount")
    public ResponseEntity<Map<String, Object>> sumSuccessAmountByTimeRange(
            @RequestParam String startTime,
            @RequestParam String endTime) {
        Map<String, Object> response = new HashMap<>();
        BigDecimal totalAmount = refundRecordService.sumSuccessAmountByTimeRange(startTime, endTime);
        response.put("startTime", startTime);
        response.put("endTime", endTime);
        response.put("totalAmount", totalAmount);
        return ResponseEntity.ok(response);
    }

    /**
     * 按日期统计退款成功的订单数和金额
     * @param days 统计最近N天
     * @return 统计结果
     */
    @GetMapping("/stats/daily")
    public ResponseEntity<List<Map<String, Object>>> dailySuccessStats(
            @RequestParam(defaultValue = "7") int days) {
        List<Map<String, Object>> stats = refundRecordService.dailySuccessStats(days);
        return ResponseEntity.ok(stats);
    }

    /**
     * 查询退款中超时的记录
     * @param hours 超时小时数
     * @return 超时记录列表
     */
    @GetMapping("/timeout")
    public ResponseEntity<List<RefundRecord>> findTimeoutRefunds(
            @RequestParam(defaultValue = "24") int hours) {
        List<RefundRecord> records = refundRecordService.findTimeoutRefunds(hours);
        return ResponseEntity.ok(records);
    }

    /**
     * 批量更新超时退款记录状态为失败
     * @param hours 超时小时数
     * @return 更新结果
     */
    @PostMapping("/timeout/update")
    public ResponseEntity<Map<String, Object>> updateTimeoutRefunds(
            @RequestParam(defaultValue = "24") int hours) {
        Map<String, Object> response = new HashMap<>();
        int count = refundRecordService.updateTimeoutRefunds(hours);
        response.put("success", true);
        response.put("message", "已更新" + count + "条超时退款记录");
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    /**
     * 检查订单是否已有成功的退款记录
     * @param orderId 订单ID
     * @return 检查结果
     */
    @GetMapping("/order/{orderId}/check-success")
    public ResponseEntity<Map<String, Object>> hasSuccessRefund(@PathVariable Long orderId) {
        Map<String, Object> response = new HashMap<>();
        boolean hasSuccess = refundRecordService.hasSuccessRefund(orderId);
        response.put("hasSuccessRefund", hasSuccess);
        return ResponseEntity.ok(response);
    }

    /**
     * 检查订单是否有进行中的退款
     * @param orderId 订单ID
     * @return 检查结果
     */
    @GetMapping("/order/{orderId}/check-processing")
    public ResponseEntity<Map<String, Object>> hasProcessingRefund(@PathVariable Long orderId) {
        Map<String, Object> response = new HashMap<>();
        boolean hasProcessing = refundRecordService.hasProcessingRefund(orderId);
        response.put("hasProcessingRefund", hasProcessing);
        return ResponseEntity.ok(response);
    }

    /**
     * 根据第三方退款流水号查询退款记录
     * @param refundNo 第三方退款流水号
     * @return RefundRecord对象
     */
    @GetMapping("/refund-no/{refundNo}")
    public ResponseEntity<RefundRecord> getByRefundNo(@PathVariable String refundNo) {
        RefundRecord refundRecord = refundRecordService.getByRefundNo(refundNo);
        if (refundRecord != null) {
            return ResponseEntity.ok(refundRecord);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 计算用户累计退款成功金额
     * @param userId 用户ID
     * @return 累计金额
     */
    @GetMapping("/user/{userId}/total-amount")
    public ResponseEntity<Map<String, Object>> sumSuccessAmountByUserId(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        BigDecimal totalAmount = refundRecordService.sumSuccessAmountByUserId(userId);
        response.put("userId", userId);
        response.put("totalAmount", totalAmount);
        return ResponseEntity.ok(response);
    }

    /**
     * 统计退款原因分布
     * @return 统计结果
     */
    @GetMapping("/stats/reasons")
    public ResponseEntity<List<Map<String, Object>>> countByRefundReason() {
        List<Map<String, Object>> stats = refundRecordService.countByRefundReason();
        return ResponseEntity.ok(stats);
    }
}
