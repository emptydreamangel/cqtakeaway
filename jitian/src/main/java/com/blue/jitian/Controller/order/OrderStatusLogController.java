package com.blue.jitian.Controller.order;

import com.blue.jitian.Entity.OrderStatusLog;
import com.blue.jitian.Service.OrderStatusLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


/**
 * 订单状态日志控制器
 * 提供订单状态日志相关的REST API接口
 */
@RestController
@RequestMapping("/api/order-status-log")
public class OrderStatusLogController {

    @Autowired
    private OrderStatusLogService logService;

    /**
     * 根据订单ID查询状态日志
     * @param orderId 订单ID
     * @return 状态日志列表
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderStatusLog>> getLogsByOrderId(@PathVariable("orderId") Long orderId) {
        List<OrderStatusLog> logs = logService.getLogsByOrderId(orderId);
        return ResponseEntity.ok(logs);
    }

    /**
     * 根据订单ID查询最新的状态日志
     * @param orderId 订单ID
     * @return 状态日志
     */
    @GetMapping("/order/{orderId}/latest")
    public ResponseEntity<?> getLatestLogByOrderId(@PathVariable("orderId") Long orderId) {
        OrderStatusLog log = logService.getLatestLogByOrderId(orderId);
        if (log == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "日志不存在"));
        }
        return ResponseEntity.ok(log);
    }

    /**
     * 根据ID获取日志详情
     * @param id 日志ID
     * @return 日志详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getLogById(@PathVariable("id") Long id) {
        OrderStatusLog log = logService.getById(id);
        if (log == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "日志不存在"));
        }
        return ResponseEntity.ok(log);
    }

    /**
     * 根据操作人查询日志
     * @param operatorType 操作人类型
     * @param operatorId 操作人ID
     * @return 状态日志列表
     */
    @GetMapping("/operator/{operatorType}/{operatorId}")
    public ResponseEntity<List<OrderStatusLog>> getLogsByOperator(@PathVariable("operatorType") Integer operatorType,
                                                                   @PathVariable("operatorId") Long operatorId) {
        List<OrderStatusLog> logs = logService.getLogsByOperator(operatorType, operatorId);
        return ResponseEntity.ok(logs);
    }

    /**
     * 根据新状态查询日志
     * @param newStatus 新状态
     * @return 状态日志列表
     */
    @GetMapping("/status/{newStatus}")
    public ResponseEntity<List<OrderStatusLog>> getLogsByNewStatus(@PathVariable("newStatus") Integer newStatus) {
        List<OrderStatusLog> logs = logService.getLogsByNewStatus(newStatus);
        return ResponseEntity.ok(logs);
    }

    /**
     * 查询订单在指定状态的日志
     * @param orderId 订单ID
     * @param status 状态值
     * @return 状态日志列表
     */
    @GetMapping("/order/{orderId}/status/{status}")
    public ResponseEntity<List<OrderStatusLog>> getLogsByOrderIdAndStatus(@PathVariable("orderId") Long orderId,
                                                                           @PathVariable("status") Integer status) {
        List<OrderStatusLog> logs = logService.getLogsByOrderIdAndStatus(orderId, status);
        return ResponseEntity.ok(logs);
    }

    /**
     * 查询指定时间范围内的状态日志
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 状态日志列表
     */
    @GetMapping("/time-range")
    public ResponseEntity<List<OrderStatusLog>> getLogsByTimeRange(
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        List<OrderStatusLog> logs = logService.getLogsByTimeRange(startTime, endTime);
        return ResponseEntity.ok(logs);
    }

    /**
     * 添加状态日志
     * @param log 状态日志对象
     * @return 添加结果
     */
    @PostMapping
    public ResponseEntity<?> addLog(@RequestBody OrderStatusLog log) {
        // 验证必填字段
        if (log.getOrder_id() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "订单ID不能为空"));
        }
        if (log.getNew_status() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "新状态不能为空"));
        }
        
        boolean success = logService.addLog(log);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED).body(log);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "添加失败"));
    }

    /**
     * 批量添加状态日志
     * @param request 请求体（包含logs列表）
     * @return 添加结果
     */
    @PostMapping("/batch")
    public ResponseEntity<?> batchAddLogs(@RequestBody Map<String, List<OrderStatusLog>> request) {
        List<OrderStatusLog> logs = request.get("logs");
        if (logs == null || logs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "日志列表不能为空"));
        }
        
        boolean success = logService.batchAddLogs(logs);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "批量添加成功", "count", logs.size()));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "批量添加失败"));
    }

    /**
     * 记录订单状态变更
     * @param request 请求体（包含订单ID、旧状态、新状态等）
     * @return 记录结果
     */
    @PostMapping("/record")
    public ResponseEntity<?> recordStatusChange(@RequestBody Map<String, Object> request) {
        Long orderId = ((Number) request.get("orderId")).longValue();
        Integer oldStatus = request.get("oldStatus") != null ? ((Number) request.get("oldStatus")).intValue() : null;
        Integer newStatus = ((Number) request.get("newStatus")).intValue();
        Integer operatorType = request.get("operatorType") != null ? ((Number) request.get("operatorType")).intValue() : null;
        Long operatorId = request.get("operatorId") != null ? ((Number) request.get("operatorId")).longValue() : null;
        String remark = (String) request.get("remark");
        
        if (orderId == null || newStatus == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "订单ID和新状态不能为空"));
        }
        
        boolean success = logService.recordStatusChange(orderId, oldStatus, newStatus, operatorType, operatorId, remark);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "记录成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "记录失败"));
    }

    /**
     * 删除状态日志
     * @param id 日志ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLog(@PathVariable("id") Long id) {
        OrderStatusLog log = logService.getById(id);
        if (log == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "日志不存在"));
        }
        
        boolean success = logService.deleteLog(id);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "删除成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "删除失败"));
    }

    /**
     * 删除订单的所有状态日志
     * @param orderId 订单ID
     * @return 删除结果
     */
    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<?> deleteByOrderId(@PathVariable("orderId") Long orderId) {
        boolean success = logService.deleteByOrderId(orderId);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "删除成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "删除失败"));
    }

    /**
     * 统计订单的状态变更次数
     * @param orderId 订单ID
     * @return 变更次数
     */
    @GetMapping("/count/order/{orderId}")
    public ResponseEntity<Map<String, Long>> countByOrderId(@PathVariable("orderId") Long orderId) {
        long count = logService.countByOrderId(orderId);
        return ResponseEntity.ok(Map.of("count", count));
    }
}
