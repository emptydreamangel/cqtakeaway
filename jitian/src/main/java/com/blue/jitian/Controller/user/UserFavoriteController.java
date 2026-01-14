package com.blue.jitian.Controller.user;

import com.blue.jitian.Entity.UserFavorite;
import com.blue.jitian.Service.UserFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 用户收藏控制器
 * 提供用户收藏相关的REST API接口
 */
@RestController
@RequestMapping("/api/favorite")
public class UserFavoriteController {

    @Autowired
    private UserFavoriteService favoriteService;

    /**
     * 收藏店铺
     * @param request 请求体（包含userId和shopId）
     * @return 收藏结果
     */
    @PostMapping("/shop")
    public ResponseEntity<?> favoriteShop(@RequestBody Map<String, Long> request) {
        Long userId = request.get("userId");
        Long shopId = request.get("shopId");
        
        if (userId == null || shopId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "用户ID和店铺ID不能为空"));
        }
        
        boolean success = favoriteService.favoriteShop(userId, shopId);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "收藏店铺成功"));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("message", "已经收藏过该店铺"));
    }

    /**
     * 收藏商品
     * @param request 请求体（包含userId、shopId和productId）
     * @return 收藏结果
     */
    @PostMapping("/product")
    public ResponseEntity<?> favoriteProduct(@RequestBody Map<String, Long> request) {
        Long userId = request.get("userId");
        Long shopId = request.get("shopId");
        Long productId = request.get("productId");
        
        if (userId == null || shopId == null || productId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "用户ID、店铺ID和商品ID不能为空"));
        }
        
        boolean success = favoriteService.favoriteProduct(userId, shopId, productId);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "收藏商品成功"));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("message", "已经收藏过该商品"));
    }

    /**
     * 取消收藏店铺
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @return 取消结果
     */
    @DeleteMapping("/shop")
    public ResponseEntity<?> unfavoriteShop(@RequestParam("userId") Long userId,
                                            @RequestParam("shopId") Long shopId) {
        boolean success = favoriteService.unfavoriteShop(userId, shopId);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "取消收藏成功"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "未找到该收藏记录"));
    }

    /**
     * 取消收藏商品
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @param productId 商品ID
     * @return 取消结果
     */
    @DeleteMapping("/product")
    public ResponseEntity<?> unfavoriteProduct(@RequestParam("userId") Long userId,
                                               @RequestParam("shopId") Long shopId,
                                               @RequestParam("productId") Long productId) {
        boolean success = favoriteService.unfavoriteProduct(userId, shopId, productId);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "取消收藏成功"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "未找到该收藏记录"));
    }

    /**
     * 切换店铺收藏状态
     * @param request 请求体（包含userId和shopId）
     * @return 切换结果
     */
    @PostMapping("/shop/toggle")
    public ResponseEntity<?> toggleShopFavorite(@RequestBody Map<String, Long> request) {
        Long userId = request.get("userId");
        Long shopId = request.get("shopId");
        
        if (userId == null || shopId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "用户ID和店铺ID不能为空"));
        }
        
        boolean favorited = favoriteService.toggleShopFavorite(userId, shopId);
        return ResponseEntity.ok(Map.of(
                "message", favorited ? "收藏成功" : "取消收藏成功",
                "favorited", favorited
        ));
    }

    /**
     * 切换商品收藏状态
     * @param request 请求体（包含userId、shopId和productId）
     * @return 切换结果
     */
    @PostMapping("/product/toggle")
    public ResponseEntity<?> toggleProductFavorite(@RequestBody Map<String, Long> request) {
        Long userId = request.get("userId");
        Long shopId = request.get("shopId");
        Long productId = request.get("productId");
        
        if (userId == null || shopId == null || productId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "用户ID、店铺ID和商品ID不能为空"));
        }
        
        boolean favorited = favoriteService.toggleProductFavorite(userId, shopId, productId);
        return ResponseEntity.ok(Map.of(
                "message", favorited ? "收藏成功" : "取消收藏成功",
                "favorited", favorited
        ));
    }

    /**
     * 检查是否收藏了店铺
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @return 检查结果
     */
    @GetMapping("/shop/check")
    public ResponseEntity<Map<String, Boolean>> checkShopFavorite(@RequestParam("userId") Long userId,
                                                                   @RequestParam("shopId") Long shopId) {
        boolean favorited = favoriteService.isShopFavorited(userId, shopId);
        return ResponseEntity.ok(Map.of("favorited", favorited));
    }

    /**
     * 检查是否收藏了商品
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @param productId 商品ID
     * @return 检查结果
     */
    @GetMapping("/product/check")
    public ResponseEntity<Map<String, Boolean>> checkProductFavorite(@RequestParam("userId") Long userId,
                                                                      @RequestParam("shopId") Long shopId,
                                                                      @RequestParam("productId") Long productId) {
        boolean favorited = favoriteService.isProductFavorited(userId, shopId, productId);
        return ResponseEntity.ok(Map.of("favorited", favorited));
    }

    /**
     * 获取用户的所有收藏
     * @param userId 用户ID
     * @return 收藏列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserFavorite>> getUserFavorites(@PathVariable("userId") Long userId) {
        List<UserFavorite> favorites = favoriteService.getUserFavorites(userId);
        return ResponseEntity.ok(favorites);
    }

    /**
     * 获取用户收藏的店铺列表
     * @param userId 用户ID
     * @return 收藏列表
     */
    @GetMapping("/user/{userId}/shops")
    public ResponseEntity<List<UserFavorite>> getUserFavoriteShops(@PathVariable("userId") Long userId) {
        List<UserFavorite> favorites = favoriteService.getUserFavoriteShops(userId);
        return ResponseEntity.ok(favorites);
    }

    /**
     * 获取用户收藏的商品列表
     * @param userId 用户ID
     * @return 收藏列表
     */
    @GetMapping("/user/{userId}/products")
    public ResponseEntity<List<UserFavorite>> getUserFavoriteProducts(@PathVariable("userId") Long userId) {
        List<UserFavorite> favorites = favoriteService.getUserFavoriteProducts(userId);
        return ResponseEntity.ok(favorites);
    }

    /**
     * 根据类型获取用户收藏
     * @param userId 用户ID
     * @param type 类型（1:店铺，2:商品）
     * @return 收藏列表
     */
    @GetMapping("/user/{userId}/type/{type}")
    public ResponseEntity<List<UserFavorite>> getUserFavoritesByType(@PathVariable("userId") Long userId,
                                                                      @PathVariable("type") Integer type) {
        List<UserFavorite> favorites = favoriteService.getUserFavoritesByType(userId, type);
        return ResponseEntity.ok(favorites);
    }

    /**
     * 统计用户收藏总数
     * @param userId 用户ID
     * @return 收藏数量
     */
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Map<String, Long>> countUserFavorites(@PathVariable("userId") Long userId) {
        long count = favoriteService.countUserFavorites(userId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    /**
     * 统计用户各类型收藏数量
     * @param userId 用户ID
     * @return 各类型收藏数量
     */
    @GetMapping("/user/{userId}/count/detail")
    public ResponseEntity<Map<String, Long>> countUserFavoritesByType(@PathVariable("userId") Long userId) {
        long totalCount = favoriteService.countUserFavorites(userId);
        long shopCount = favoriteService.countUserShopFavorites(userId);
        long productCount = favoriteService.countUserProductFavorites(userId);
        
        return ResponseEntity.ok(Map.of(
                "total", totalCount,
                "shops", shopCount,
                "products", productCount
        ));
    }

    /**
     * 统计店铺被收藏的次数
     * @param shopId 店铺ID
     * @return 收藏次数
     */
    @GetMapping("/shop/{shopId}/count")
    public ResponseEntity<Map<String, Long>> countShopFavorites(@PathVariable("shopId") Long shopId) {
        long count = favoriteService.countShopFavorites(shopId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    /**
     * 统计商品被收藏的次数
     * @param shopId 店铺ID
     * @param productId 商品ID
     * @return 收藏次数
     */
    @GetMapping("/product/{shopId}/{productId}/count")
    public ResponseEntity<Map<String, Long>> countProductFavorites(@PathVariable("shopId") Long shopId,
                                                                    @PathVariable("productId") Long productId) {
        long count = favoriteService.countProductFavorites(shopId, productId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    /**
     * 获取收藏了某店铺的所有用户
     * @param shopId 店铺ID
     * @return 收藏列表
     */
    @GetMapping("/shop/{shopId}/fans")
    public ResponseEntity<List<UserFavorite>> getShopFans(@PathVariable("shopId") Long shopId) {
        List<UserFavorite> fans = favoriteService.getShopFans(shopId);
        return ResponseEntity.ok(fans);
    }

    /**
     * 清空用户所有收藏
     * @param userId 用户ID
     * @return 清空结果
     */
    @DeleteMapping("/user/{userId}/clear")
    public ResponseEntity<?> clearUserFavorites(@PathVariable("userId") Long userId) {
        int count = favoriteService.clearUserFavorites(userId);
        return ResponseEntity.ok(Map.of(
                "message", "清空收藏成功",
                "count", count
        ));
    }
}
