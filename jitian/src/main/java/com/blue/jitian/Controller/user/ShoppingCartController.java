package com.blue.jitian.Controller.user;

import com.blue.jitian.Entity.ShoppingCart;
import com.blue.jitian.Service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 购物车控制器
 * 提供购物车相关的REST API接口
 */
@RestController
@RequestMapping("/api/cart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService cartService;

    /**
     * 获取用户购物车列表
     * @param userId 用户ID
     * @return 购物车列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ShoppingCart>> getCartsByUserId(@PathVariable("userId") Long userId) {
        List<ShoppingCart> carts = cartService.getCartsByUserId(userId);
        return ResponseEntity.ok(carts);
    }

    /**
     * 获取用户在指定商家的购物车列表
     * @param userId 用户ID
     * @param shopId 商家ID
     * @return 购物车列表
     */
    @GetMapping("/user/{userId}/shop/{shopId}")
    public ResponseEntity<List<ShoppingCart>> getCartsByUserIdAndShopId(@PathVariable("userId") Long userId,
                                                                         @PathVariable("shopId") Long shopId) {
        List<ShoppingCart> carts = cartService.getCartsByUserIdAndShopId(userId, shopId);
        return ResponseEntity.ok(carts);
    }

    /**
     * 根据ID获取购物车项详情
     * @param id 购物车ID
     * @return 购物车详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCartById(@PathVariable("id") Long id) {
        ShoppingCart cart = cartService.getById(id);
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "购物车项不存在"));
        }
        return ResponseEntity.ok(cart);
    }

    /**
     * 添加商品到购物车
     * @param cart 购物车对象
     * @return 添加结果
     */
    @PostMapping
    public ResponseEntity<?> addToCart(@RequestBody ShoppingCart cart) {
        // 验证必填字段
        if (cart.getUser_id() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "用户ID不能为空"));
        }
        if (cart.getShop_id() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "商家ID不能为空"));
        }
        if (cart.getProduct_id() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "商品ID不能为空"));
        }
        
        boolean success = cartService.addToCart(cart);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "添加成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "添加失败"));
    }

    /**
     * 更新购物车数量
     * @param id 购物车ID
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
        
        ShoppingCart cart = cartService.getById(id);
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "购物车项不存在"));
        }
        
        boolean success = cartService.updateQuantity(id, quantity);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "更新成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "更新失败"));
    }

    /**
     * 增加购物车数量
     * @param id 购物车ID
     * @param request 请求体（包含increment）
     * @return 更新结果
     */
    @PatchMapping("/{id}/increment")
    public ResponseEntity<?> incrementQuantity(@PathVariable("id") Long id,
                                              @RequestBody Map<String, Integer> request) {
        Integer increment = request.get("increment");
        if (increment == null || increment <= 0) {
            increment = 1;  // 默认增加1
        }
        
        boolean success = cartService.incrementQuantity(id, increment);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "增加成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "增加失败"));
    }

    /**
     * 减少购物车数量
     * @param id 购物车ID
     * @param request 请求体（包含decrement）
     * @return 更新结果
     */
    @PatchMapping("/{id}/decrement")
    public ResponseEntity<?> decrementQuantity(@PathVariable("id") Long id,
                                              @RequestBody Map<String, Integer> request) {
        Integer decrement = request.get("decrement");
        if (decrement == null || decrement <= 0) {
            decrement = 1;  // 默认减少1
        }
        
        boolean success = cartService.decrementQuantity(id, decrement);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "减少成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "减少失败"));
    }

    /**
     * 删除购物车项
     * @param id 购物车ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCart(@PathVariable("id") Long id) {
        ShoppingCart cart = cartService.getById(id);
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "购物车项不存在"));
        }
        
        boolean success = cartService.deleteCart(id);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "删除成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "删除失败"));
    }

    /**
     * 批量删除购物车项
     * @param request 请求体（包含cartIds）
     * @return 删除结果
     */
    @DeleteMapping("/batch")
    public ResponseEntity<?> batchDelete(@RequestBody Map<String, List<Long>> request) {
        List<Long> cartIds = request.get("cartIds");
        if (cartIds == null || cartIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "购物车ID列表不能为空"));
        }
        
        boolean success = cartService.batchDelete(cartIds);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "批量删除成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "批量删除失败"));
    }

    /**
     * 清空用户购物车
     * @param userId 用户ID
     * @return 清空结果
     */
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> clearCart(@PathVariable("userId") Long userId) {
        boolean success = cartService.clearCart(userId);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "清空成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "清空失败"));
    }

    /**
     * 清空用户在指定商家的购物车
     * @param userId 用户ID
     * @param shopId 商家ID
     * @return 清空结果
     */
    @DeleteMapping("/user/{userId}/shop/{shopId}")
    public ResponseEntity<?> clearCartByShop(@PathVariable("userId") Long userId,
                                             @PathVariable("shopId") Long shopId) {
        boolean success = cartService.clearCartByShop(userId, shopId);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "清空成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "清空失败"));
    }

    /**
     * 统计用户购物车项数量
     * @param userId 用户ID
     * @return 数量
     */
    @GetMapping("/count/user/{userId}")
    public ResponseEntity<Map<String, Long>> countByUserId(@PathVariable("userId") Long userId) {
        long count = cartService.countByUserId(userId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    /**
     * 统计用户在指定商家的购物车项数量
     * @param userId 用户ID
     * @param shopId 商家ID
     * @return 数量
     */
    @GetMapping("/count/user/{userId}/shop/{shopId}")
    public ResponseEntity<Map<String, Long>> countByUserIdAndShopId(@PathVariable("userId") Long userId,
                                                                     @PathVariable("shopId") Long shopId) {
        long count = cartService.countByUserIdAndShopId(userId, shopId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    /**
     * 获取用户购物车中的商家列表
     * @param userId 用户ID
     * @return 商家ID列表
     */
    @GetMapping("/shops/user/{userId}")
    public ResponseEntity<List<Long>> getShopIdsByUserId(@PathVariable("userId") Long userId) {
        List<Long> shopIds = cartService.getShopIdsByUserId(userId);
        return ResponseEntity.ok(shopIds);
    }

    /**
     * 检查购物车项是否存在
     * @param userId 用户ID
     * @param productId 商品ID
     * @param specId 规格ID（可选）
     * @return 检查结果
     */
    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkCartItemExist(@RequestParam("userId") Long userId,
                                                                    @RequestParam("productId") Long productId,
                                                                    @RequestParam(value = "specId", required = false) Long specId) {
        boolean exists = cartService.isCartItemExist(userId, productId, specId);
        return ResponseEntity.ok(Map.of("exists", exists));
    }
}
