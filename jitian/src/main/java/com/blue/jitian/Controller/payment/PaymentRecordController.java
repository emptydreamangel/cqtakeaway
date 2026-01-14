package com.blue.jitian.Controller.payment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blue.jitian.Entity.PaymentRecord;
import com.blue.jitian.Service.PaymentRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/payment-records")
public class PaymentRecordController {

    @Autowired
    private PaymentRecordService paymentRecordService;

    /**
     * 创建支付记录
     * @param orderId 订单ID
     * @param userId 用户ID
     * @param amount 支付金额
     * @param payMethod 支付方式
     * @return 创建结果
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createPayment(@RequestParam Long orderId,
                                                               @RequestParam Long userId,
                                                               @RequestParam BigDecimal amount,
                                                               @RequestParam Integer payMethod) {
        Map<String, Object> response = new HashMap<>();
        PaymentRecord paymentRecord = paymentRecordService.createPayment(orderId, userId, amount, payMethod);
        if (paymentRecord != null) {
            response.put("success", true);
            response.put("message", "支付记录创建成功");
            response.put("data", paymentRecord);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "支付记录创建失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 支付成功回调（模拟）
     * @param outTradeNo 商户订单号
     * @param transactionNo 第三方交易流水号
     * @return 更新结果
     */
    @PostMapping("/notify/success")
    public ResponseEntity<Map<String, Object>> paymentSuccess(@RequestParam String outTradeNo,
                                                                @RequestParam String transactionNo) {
        Map<String, Object> response = new HashMap<>();
        boolean success = paymentRecordService.updatePaymentSuccess(outTradeNo, transactionNo);
        if (success) {
            response.put("success", true);
            response.put("message", "支付成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "更新失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 支付失败回调（模拟）
     * @param outTradeNo 商户订单号
     * @return 更新结果
     */
    @PostMapping("/notify/failed")
    public ResponseEntity<Map<String, Object>> paymentFailed(@RequestParam String outTradeNo) {
        Map<String, Object> response = new HashMap<>();
        boolean success = paymentRecordService.updatePaymentFailed(outTradeNo);
        if (success) {
            response.put("success", true);
            response.put("message", "支付失败状态已更新");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "更新失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 申请退款
     * @param paymentId 支付记录ID
     * @return 退款结果
     */
    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<Map<String, Object>> refund(@PathVariable Long paymentId) {
        Map<String, Object> response = new HashMap<>();
        boolean success = paymentRecordService.updatePaymentRefund(paymentId);
        if (success) {
            response.put("success", true);
            response.put("message", "退款成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "退款失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 根据商户订单号查询支付记录
     * @param outTradeNo 商户订单号
     * @return PaymentRecord对象
     */
    @GetMapping("/out-trade-no/{outTradeNo}")
    public ResponseEntity<PaymentRecord> getByOutTradeNo(@PathVariable String outTradeNo) {
        PaymentRecord paymentRecord = paymentRecordService.getByOutTradeNo(outTradeNo);
        if (paymentRecord != null) {
            return ResponseEntity.ok(paymentRecord);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 根据订单ID查询支付记录
     * @param orderId 订单ID
     * @return PaymentRecord列表
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentRecord>> getByOrderId(@PathVariable Long orderId) {
        List<PaymentRecord> records = paymentRecordService.getByOrderId(orderId);
        return ResponseEntity.ok(records);
    }

    /**
     * 根据用户ID查询支付记录
     * @param userId 用户ID
     * @param status 状态（可选）
     * @return PaymentRecord列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentRecord>> getByUserId(@PathVariable Long userId,
                                                             @RequestParam(required = false) Integer status) {
        List<PaymentRecord> records = paymentRecordService.getByUserId(userId, status);
        return ResponseEntity.ok(records);
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
    @GetMapping("/page")
    public ResponseEntity<Page<PaymentRecord>> getPaymentPage(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long orderId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer payMethod) {
        Page<PaymentRecord> page = paymentRecordService.getPaymentPage(current, size, userId, orderId, status, payMethod);
        return ResponseEntity.ok(page);
    }

    /**
     * 根据ID查询支付记录详情
     * @param id 支付记录ID
     * @return PaymentRecord对象
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentRecord> getById(@PathVariable Long id) {
        PaymentRecord paymentRecord = paymentRecordService.getById(id);
        if (paymentRecord != null) {
            return ResponseEntity.ok(paymentRecord);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 统计各支付状态的记录数量
     * @return 统计结果
     */
    @GetMapping("/stats/status")
    public ResponseEntity<List<Map<String, Object>>> countByStatus() {
        List<Map<String, Object>> stats = paymentRecordService.countByStatus();
        return ResponseEntity.ok(stats);
    }

    /**
     * 统计各支付方式的记录数量和金额
     * @return 统计结果
     */
    @GetMapping("/stats/pay-method")
    public ResponseEntity<List<Map<String, Object>>> countByPayMethod() {
        List<Map<String, Object>> stats = paymentRecordService.countByPayMethod();
        return ResponseEntity.ok(stats);
    }

    /**
     * 查询用户的支付记录统计
     * @param userId 用户ID
     * @return 统计结果
     */
    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<List<Map<String, Object>>> countByUserIdAndStatus(@PathVariable Long userId) {
        List<Map<String, Object>> stats = paymentRecordService.countByUserIdAndStatus(userId);
        return ResponseEntity.ok(stats);
    }

    /**
     * 查询指定时间范围内的支付成功总金额
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 总金额
     */
    @GetMapping("/stats/amount")
    public ResponseEntity<Map<String, Object>> sumSuccessAmountByTimeRange(
            @RequestParam String startTime,
            @RequestParam String endTime) {
        Map<String, Object> response = new HashMap<>();
        BigDecimal totalAmount = paymentRecordService.sumSuccessAmountByTimeRange(startTime, endTime);
        response.put("startTime", startTime);
        response.put("endTime", endTime);
        response.put("totalAmount", totalAmount);
        return ResponseEntity.ok(response);
    }

    /**
     * 查询待支付超时的记录
     * @param minutes 超时分钟数
     * @return 超时记录列表
     */
    @GetMapping("/timeout")
    public ResponseEntity<List<PaymentRecord>> findTimeoutPayments(
            @RequestParam(defaultValue = "30") int minutes) {
        List<PaymentRecord> records = paymentRecordService.findTimeoutPayments(minutes);
        return ResponseEntity.ok(records);
    }

    /**
     * 批量更新超时支付记录状态为失败
     * @param minutes 超时分钟数
     * @return 更新结果
     */
    @PostMapping("/timeout/update")
    public ResponseEntity<Map<String, Object>> updateTimeoutPayments(
            @RequestParam(defaultValue = "30") int minutes) {
        Map<String, Object> response = new HashMap<>();
        int count = paymentRecordService.updateTimeoutPayments(minutes);
        response.put("success", true);
        response.put("message", "已更新" + count + "条超时支付记录");
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    /**
     * 按日期统计支付成功的订单数和金额
     * @param days 统计最近N天
     * @return 统计结果
     */
    @GetMapping("/stats/daily")
    public ResponseEntity<List<Map<String, Object>>> dailySuccessStats(
            @RequestParam(defaultValue = "7") int days) {
        List<Map<String, Object>> stats = paymentRecordService.dailySuccessStats(days);
        return ResponseEntity.ok(stats);
    }

    /**
     * 检查订单是否已有成功的支付记录
     * @param orderId 订单ID
     * @return 检查结果
     */
    @GetMapping("/order/{orderId}/check-success")
    public ResponseEntity<Map<String, Object>> hasSuccessPayment(@PathVariable Long orderId) {
        Map<String, Object> response = new HashMap<>();
        boolean hasSuccess = paymentRecordService.hasSuccessPayment(orderId);
        response.put("hasSuccessPayment", hasSuccess);
        return ResponseEntity.ok(response);
    }

    /**
     * 根据第三方交易流水号查询支付记录
     * @param transactionNo 第三方交易流水号
     * @return PaymentRecord对象
     */
    @GetMapping("/transaction-no/{transactionNo}")
    public ResponseEntity<PaymentRecord> getByTransactionNo(@PathVariable String transactionNo) {
        PaymentRecord paymentRecord = paymentRecordService.getByTransactionNo(transactionNo);
        if (paymentRecord != null) {
            return ResponseEntity.ok(paymentRecord);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 计算用户累计支付成功金额
     * @param userId 用户ID
     * @return 累计金额
     */
    @GetMapping("/user/{userId}/total-amount")
    public ResponseEntity<Map<String, Object>> sumSuccessAmountByUserId(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        BigDecimal totalAmount = paymentRecordService.sumSuccessAmountByUserId(userId);
        response.put("userId", userId);
        response.put("totalAmount", totalAmount);
        return ResponseEntity.ok(response);
    }
}
