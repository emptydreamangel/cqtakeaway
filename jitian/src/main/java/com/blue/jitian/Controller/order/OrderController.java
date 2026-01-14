package com.blue.jitian.Controller.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.blue.jitian.Entity.Order;
import com.blue.jitian.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


/**
 * 订单控制器
 * 提供订单相关的REST API接口
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 根据订单号查询订单
     * @param orderNo 订单号
     * @return 订单详情
     */
    @GetMapping("/no/{orderNo}")
    public ResponseEntity<?> getOrderByNo(@PathVariable("orderNo") String orderNo) {
        Order order = orderService.getByOrderNo(orderNo);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "订单不存在"));
        }
        return ResponseEntity.ok(order);
    }

    /**
     * 根据ID查询订单详情
     * @param id 订单ID
     * @return 订单详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable("id") Long id) {
        Order order = orderService.getById(id);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "订单不存在"));
        }
        return ResponseEntity.ok(order);
    }

    /**
     * 根据用户ID查询订单列表
     * @param userId 用户ID
     * @return 订单列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable("userId") Long userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    /**
     * 根据用户ID分页查询订单
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    @GetMapping("/user/{userId}/page")
    public ResponseEntity<IPage<Order>> getOrdersByUserIdPage(@PathVariable("userId") Long userId,
                                                               @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        IPage<Order> page = orderService.getOrdersByUserIdPage(userId, pageNum, pageSize);
        return ResponseEntity.ok(page);
    }

    /**
     * 根据商家ID查询订单列表
     * @param shopId 商家ID
     * @return 订单列表
     */
    @GetMapping("/shop/{shopId}")
    public ResponseEntity<List<Order>> getOrdersByShopId(@PathVariable("shopId") Long shopId) {
        List<Order> orders = orderService.getOrdersByShopId(shopId);
        return ResponseEntity.ok(orders);
    }

    /**
     * 根据商家ID分页查询订单
     * @param shopId 商家ID
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    @GetMapping("/shop/{shopId}/page")
    public ResponseEntity<IPage<Order>> getOrdersByShopIdPage(@PathVariable("shopId") Long shopId,
                                                               @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        IPage<Order> page = orderService.getOrdersByShopIdPage(shopId, pageNum, pageSize);
        return ResponseEntity.ok(page);
    }

    /**
     * 根据状态查询订单列表
     * @param status 订单状态
     * @return 订单列表
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable("status") Integer status) {
        List<Order> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }

    /**
     * 根据用户ID和状态查询订单列表
     * @param userId 用户ID
     * @param status 订单状态
     * @return 订单列表
     */
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByUserIdAndStatus(@PathVariable("userId") Long userId,
                                                                   @PathVariable("status") Integer status) {
        List<Order> orders = orderService.getOrdersByUserIdAndStatus(userId, status);
        return ResponseEntity.ok(orders);
    }

    /**
     * 根据用户ID和状态分页查询订单
     * @param userId 用户ID
     * @param status 订单状态
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    @GetMapping("/user/{userId}/status/{status}/page")
    public ResponseEntity<IPage<Order>> getOrdersByUserIdAndStatusPage(@PathVariable("userId") Long userId,
                                                                        @PathVariable("status") Integer status,
                                                                        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        IPage<Order> page = orderService.getOrdersByUserIdAndStatusPage(userId, status, pageNum, pageSize);
        return ResponseEntity.ok(page);
    }

    /**
     * 根据商家ID和状态查询订单列表
     * @param shopId 商家ID
     * @param status 订单状态
     * @return 订单列表
     */
    @GetMapping("/shop/{shopId}/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByShopIdAndStatus(@PathVariable("shopId") Long shopId,
                                                                   @PathVariable("status") Integer status) {
        List<Order> orders = orderService.getOrdersByShopIdAndStatus(shopId, status);
        return ResponseEntity.ok(orders);
    }

    /**
     * 根据商家ID和状态分页查询订单
     * @param shopId 商家ID
     * @param status 订单状态
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    @GetMapping("/shop/{shopId}/status/{status}/page")
    public ResponseEntity<IPage<Order>> getOrdersByShopIdAndStatusPage(@PathVariable("shopId") Long shopId,
                                                                        @PathVariable("status") Integer status,
                                                                        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        IPage<Order> page = orderService.getOrdersByShopIdAndStatusPage(shopId, status, pageNum, pageSize);
        return ResponseEntity.ok(page);
    }

    /**
     * 创建订单
     * @param order 订单对象
     * @return 创建结果
     */
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        // 验证必填字段
        if (order.getUser_id() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "用户ID不能为空"));
        }
        if (order.getShop_id() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "商家ID不能为空"));
        }
        if (order.getTotal_amount() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "商品总金额不能为空"));
        }
        if (order.getPay_amount() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "实付金额不能为空"));
        }
        if (order.getReceiver_name() == null || order.getReceiver_name().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "收货人姓名不能为空"));
        }
        if (order.getReceiver_phone() == null || order.getReceiver_phone().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "收货人电话不能为空"));
        }
        if (order.getReceiver_address() == null || order.getReceiver_address().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "收货地址不能为空"));
        }
        
        boolean success = orderService.createOrder(order);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "创建订单失败"));
    }

    /**
     * 更新订单
     * @param id 订单ID
     * @param order 订单对象
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable("id") Long id, @RequestBody Order order) {
        Order existing = orderService.getById(id);
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "订单不存在"));
        }
        
        order.setOrder_id(id);
        boolean success = orderService.updateById(order);
        if (success) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "更新订单失败"));
    }

    /**
     * 支付订单
     * @param id 订单ID
     * @param request 请求体（包含payMethod）
     * @return 支付结果
     */
    @PatchMapping("/{id}/pay")
    public ResponseEntity<?> payOrder(@PathVariable("id") Long id,
                                      @RequestBody Map<String, Integer> request) {
        Integer payMethod = request.get("payMethod");
        if (payMethod == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "支付方式不能为空"));
        }
        
        Order order = orderService.getById(id);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "订单不存在"));
        }
        if (order.getStatus() != 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "订单状态不是待支付"));
        }
        
        boolean success = orderService.payOrder(id, payMethod);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "支付成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "支付失败"));
    }

    /**
     * 商家接单
     * @param id 订单ID
     * @return 接单结果
     */
    @PatchMapping("/{id}/accept")
    public ResponseEntity<?> acceptOrder(@PathVariable("id") Long id) {
        boolean success = orderService.acceptOrder(id);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "接单成功"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", "接单失败，订单状态不是待接单"));
    }

    /**
     * 配送员接单
     * @param id 订单ID
     * @return 接单结果
     */
    @PatchMapping("/{id}/dispatch")
    public ResponseEntity<?> dispatchOrder(@PathVariable("id") Long id) {
        boolean success = orderService.dispatchOrder(id);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "接单成功"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", "接单失败，订单状态不是待配送"));
    }

    /**
     * 完成订单
     * @param id 订单ID
     * @return 完成结果
     */
    @PatchMapping("/{id}/complete")
    public ResponseEntity<?> completeOrder(@PathVariable("id") Long id) {
        boolean success = orderService.completeOrder(id);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "订单已完成"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", "完成失败，订单状态不是配送中"));
    }

    /**
     * 取消订单
     * @param id 订单ID
     * @param request 请求体（包含cancelReason）
     * @return 取消结果
     */
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable("id") Long id,
                                         @RequestBody Map<String, String> request) {
        String cancelReason = request.get("cancelReason");
        
        boolean success = orderService.cancelOrder(id, cancelReason);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "订单已取消"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", "取消失败，订单已完成或已取消"));
    }

    /**
     * 统计用户订单数量
     * @param userId 用户ID
     * @return 数量
     */
    @GetMapping("/count/user/{userId}")
    public ResponseEntity<Map<String, Long>> countByUserId(@PathVariable("userId") Long userId) {
        long count = orderService.countByUserId(userId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    /**
     * 统计用户指定状态的订单数量
     * @param userId 用户ID
     * @param status 订单状态
     * @return 数量
     */
    @GetMapping("/count/user/{userId}/status/{status}")
    public ResponseEntity<Map<String, Long>> countByUserIdAndStatus(@PathVariable("userId") Long userId,
                                                                     @PathVariable("status") Integer status) {
        long count = orderService.countByUserIdAndStatus(userId, status);
        return ResponseEntity.ok(Map.of("count", count));
    }

    /**
     * 统计商家订单数量
     * @param shopId 商家ID
     * @return 数量
     */
    @GetMapping("/count/shop/{shopId}")
    public ResponseEntity<Map<String, Long>> countByShopId(@PathVariable("shopId") Long shopId) {
        long count = orderService.countByShopId(shopId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    /**
     * 统计商家指定状态的订单数量
     * @param shopId 商家ID
     * @param status 订单状态
     * @return 数量
     */
    @GetMapping("/count/shop/{shopId}/status/{status}")
    public ResponseEntity<Map<String, Long>> countByShopIdAndStatus(@PathVariable("shopId") Long shopId,
                                                                     @PathVariable("status") Integer status) {
        long count = orderService.countByShopIdAndStatus(shopId, status);
        return ResponseEntity.ok(Map.of("count", count));
    }

    /**
     * 查询时间范围内的订单
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 订单列表
     */
    @GetMapping("/time-range")
    public ResponseEntity<List<Order>> getOrdersByTimeRange(
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        List<Order> orders = orderService.getOrdersByTimeRange(startTime, endTime);
        return ResponseEntity.ok(orders);
    }

    /**
     * 查询用户时间范围内的订单
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 订单列表
     */
    @GetMapping("/user/{userId}/time-range")
    public ResponseEntity<List<Order>> getOrdersByUserIdAndTimeRange(
            @PathVariable("userId") Long userId,
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        List<Order> orders = orderService.getOrdersByUserIdAndTimeRange(userId, startTime, endTime);
        return ResponseEntity.ok(orders);
    }

    /**
     * 查询商家时间范围内的订单
     * @param shopId 商家ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 订单列表
     */
    @GetMapping("/shop/{shopId}/time-range")
    public ResponseEntity<List<Order>> getOrdersByShopIdAndTimeRange(
            @PathVariable("shopId") Long shopId,
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        List<Order> orders = orderService.getOrdersByShopIdAndTimeRange(shopId, startTime, endTime);
        return ResponseEntity.ok(orders);
    }
}
