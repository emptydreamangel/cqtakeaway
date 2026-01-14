package com.blue.jitian.Controller.order;

import com.blue.jitian.Entity.OrderItem;
import com.blue.jitian.Service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * 订单明细控制器
 * 提供订单明细相关的REST API接口
 */
@RestController
@RequestMapping("/api/order-item")
public class OrderItemController {

    @Autowired
    private OrderItemService itemService;

    /**
     * 根据订单ID查询所有订单明细
     * @param orderId 订单ID
     * @return 订单明细列表
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItem>> getItemsByOrderId(@PathVariable("orderId") Long orderId) {
        List<OrderItem> items = itemService.getItemsByOrderId(orderId);
        return ResponseEntity.ok(items);
    }

    /**
     * 根据ID获取订单明细详情
     * @param id 明细ID
     * @return 订单明细详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getItemById(@PathVariable("id") Long id) {
        OrderItem item = itemService.getById(id);
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "订单明细不存在"));
        }
        return ResponseEntity.ok(item);
    }

    /**
     * 根据商品ID查询订单明细
     * @param productId 商品ID
     * @return 订单明细列表
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<OrderItem>> getItemsByProductId(@PathVariable("productId") Long productId) {
        List<OrderItem> items = itemService.getItemsByProductId(productId);
        return ResponseEntity.ok(items);
    }

    /**
     * 查询指定商品在订单中的明细
     * @param orderId 订单ID
     * @param productId 商品ID
     * @return 订单明细
     */
    @GetMapping("/order/{orderId}/product/{productId}")
    public ResponseEntity<?> getByOrderIdAndProductId(@PathVariable("orderId") Long orderId,
                                                       @PathVariable("productId") Long productId) {
        OrderItem item = itemService.getByOrderIdAndProductId(orderId, productId);
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "订单明细不存在"));
        }
        return ResponseEntity.ok(item);
    }

    /**
     * 添加订单明细
     * @param item 订单明细对象
     * @return 添加结果
     */
    @PostMapping
    public ResponseEntity<?> addOrderItem(@RequestBody OrderItem item) {
        // 验证必填字段
        if (item.getOrder_id() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "订单ID不能为空"));
        }
        if (item.getProduct_id() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "商品ID不能为空"));
        }
        if (item.getProduct_name() == null || item.getProduct_name().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "商品名称不能为空"));
        }
        if (item.getProduct_price() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "商品单价不能为空"));
        }
        if (item.getQuantity() == null || item.getQuantity() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "数量必须大于0"));
        }
        
        boolean success = itemService.addOrderItem(item);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED).body(item);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "添加失败"));
    }

    /**
     * 批量添加订单明细
     * @param request 请求体（包含items列表）
     * @return 添加结果
     */
    @PostMapping("/batch")
    public ResponseEntity<?> batchAddOrderItems(@RequestBody Map<String, List<OrderItem>> request) {
        List<OrderItem> items = request.get("items");
        if (items == null || items.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "订单明细列表不能为空"));
        }
        
        boolean success = itemService.batchAddOrderItems(items);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "批量添加成功", "count", items.size()));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "批量添加失败"));
    }

    /**
     * 更新订单明细
     * @param id 明细ID
     * @param item 订单明细对象
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderItem(@PathVariable("id") Long id, @RequestBody OrderItem item) {
        OrderItem existing = itemService.getById(id);
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "订单明细不存在"));
        }
        
        item.setItem_id(id);
        boolean success = itemService.updateOrderItem(item);
        if (success) {
            return ResponseEntity.ok(item);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "更新失败"));
    }

    /**
     * 更新订单明细数量
     * @param id 明细ID
     * @param request 请求体（包含quantity）
     * @return 更新结果
     */
    @PatchMapping("/{id}/quantity")
    public ResponseEntity<?> updateQuantity(@PathVariable("id") Long id,
                                           @RequestBody Map<String, Integer> request) {
        Integer quantity = request.get("quantity");
        if (quantity == null || quantity <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "数量必须大于0"));
        }
        
        boolean success = itemService.updateQuantity(id, quantity);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "更新成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "更新失败"));
    }

    /**
     * 删除订单明细
     * @param id 明细ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderItem(@PathVariable("id") Long id) {
        OrderItem item = itemService.getById(id);
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "订单明细不存在"));
        }
        
        boolean success = itemService.deleteOrderItem(id);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "删除成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "删除失败"));
    }

    /**
     * 批量删除订单明细
     * @param request 请求体（包含itemIds）
     * @return 删除结果
     */
    @DeleteMapping("/batch")
    public ResponseEntity<?> batchDelete(@RequestBody Map<String, List<Long>> request) {
        List<Long> itemIds = request.get("itemIds");
        if (itemIds == null || itemIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "明细ID列表不能为空"));
        }
        
        boolean success = itemService.batchDelete(itemIds);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "批量删除成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "批量删除失败"));
    }

    /**
     * 删除订单的所有明细
     * @param orderId 订单ID
     * @return 删除结果
     */
    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<?> deleteByOrderId(@PathVariable("orderId") Long orderId) {
        boolean success = itemService.deleteByOrderId(orderId);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "删除成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "删除失败"));
    }

    /**
     * 统计订单中的商品数量
     * @param orderId 订单ID
     * @return 商品数量
     */
    @GetMapping("/count/order/{orderId}")
    public ResponseEntity<Map<String, Long>> countByOrderId(@PathVariable("orderId") Long orderId) {
        long count = itemService.countByOrderId(orderId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    /**
     * 计算订单商品总金额
     * @param orderId 订单ID
     * @return 总金额
     */
    @GetMapping("/sum/order/{orderId}")
    public ResponseEntity<Map<String, BigDecimal>> sumSubtotalByOrderId(@PathVariable("orderId") Long orderId) {
        BigDecimal sum = itemService.sumSubtotalByOrderId(orderId);
        return ResponseEntity.ok(Map.of("totalAmount", sum));
    }

    /**
     * 统计商品的销售数量
     * @param productId 商品ID
     * @return 销售数量
     */
    @GetMapping("/stats/product/{productId}/quantity")
    public ResponseEntity<Map<String, Integer>> sumQuantityByProductId(@PathVariable("productId") Long productId) {
        int quantity = itemService.sumQuantityByProductId(productId);
        return ResponseEntity.ok(Map.of("totalQuantity", quantity));
    }
}
