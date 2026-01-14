package com.blue.jitian.Controller.merchant;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blue.jitian.Entity.Shop;
import com.blue.jitian.Service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * 商家控制器
 * 提供商家相关的REST API接口
 */
@RestController
@RequestMapping("/api/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    /**
     * 获取所有营业中的商家
     * @return 商家列表
     */
    @GetMapping("/business")
    public ResponseEntity<List<Shop>> getBusinessShops() {
        List<Shop> shops = shopService.getBusinessShops();
        return ResponseEntity.ok(shops);
    }

    /**
     * 根据分类ID查询商家
     * @param categoryId 分类ID
     * @return 商家列表
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Shop>> getShopsByCategoryId(@PathVariable("categoryId") Integer categoryId) {
        List<Shop> shops = shopService.getShopsByCategoryId(categoryId);
        return ResponseEntity.ok(shops);
    }

    /**
     * 根据状态查询商家
     * @param status 状态
     * @return 商家列表
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Shop>> getShopsByStatus(@PathVariable("status") Integer status) {
        List<Shop> shops = shopService.getShopsByStatus(status);
        return ResponseEntity.ok(shops);
    }

    /**
     * 根据ID获取商家详情
     * @param id 商家ID
     * @return 商家详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getShopById(@PathVariable("id") Long id) {
        Shop shop = shopService.getById(id);
        if (shop == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "商家不存在"));
        }
        return ResponseEntity.ok(shop);
    }

    /**
     * 根据名称搜索商家
     * @param name 商家名称
     * @return 商家列表
     */
    @GetMapping("/search")
    public ResponseEntity<List<Shop>> searchShops(@RequestParam("name") String name) {
        List<Shop> shops = shopService.searchShopsByName(name);
        return ResponseEntity.ok(shops);
    }

    /**
     * 根据地区查询商家
     * @param province 省份
     * @param city 城市
     * @param district 区县
     * @return 商家列表
     */
    @GetMapping("/region")
    public ResponseEntity<List<Shop>> getShopsByRegion(@RequestParam("province") String province,
                                                        @RequestParam("city") String city,
                                                        @RequestParam("district") String district) {
        List<Shop> shops = shopService.getShopsByRegion(province, city, district);
        return ResponseEntity.ok(shops);
    }

    /**
     * 根据位置范围查询商家
     * @param minLng 最小经度
     * @param maxLng 最大经度
     * @param minLat 最小纬度
     * @param maxLat 最大纬度
     * @return 商家列表
     */
    @GetMapping("/location")
    public ResponseEntity<List<Shop>> getShopsByLocation(@RequestParam("minLng") BigDecimal minLng,
                                                          @RequestParam("maxLng") BigDecimal maxLng,
                                                          @RequestParam("minLat") BigDecimal minLat,
                                                          @RequestParam("maxLat") BigDecimal maxLat) {
        List<Shop> shops = shopService.getShopsByLocationRange(minLng, maxLng, minLat, maxLat);
        return ResponseEntity.ok(shops);
    }

    /**
     * 获取评分最高的商家
     * @param limit 限制数量
     * @return 商家列表
     */
    @GetMapping("/top/rating")
    public ResponseEntity<List<Shop>> getTopRatedShops(@RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        List<Shop> shops = shopService.getTopRatedShops(limit);
        return ResponseEntity.ok(shops);
    }

    /**
     * 获取销量最高的商家
     * @param limit 限制数量
     * @return 商家列表
     */
    @GetMapping("/top/sales")
    public ResponseEntity<List<Shop>> getTopSalesShops(@RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        List<Shop> shops = shopService.getTopSalesShops(limit);
        return ResponseEntity.ok(shops);
    }

    /**
     * 分页查询商家
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    @GetMapping("/page")
    public ResponseEntity<Page<Shop>> getShopsByPage(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<Shop> page = shopService.getShopsByPage(pageNum, pageSize);
        return ResponseEntity.ok(page);
    }

    /**
     * 分页查询营业中的商家
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    @GetMapping("/business/page")
    public ResponseEntity<Page<Shop>> getBusinessShopsByPage(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<Shop> page = shopService.getBusinessShopsByPage(pageNum, pageSize);
        return ResponseEntity.ok(page);
    }

    /**
     * 分页查询指定分类的商家
     * @param categoryId 分类ID
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    @GetMapping("/category/{categoryId}/page")
    public ResponseEntity<Page<Shop>> getShopsByCategoryIdWithPage(@PathVariable("categoryId") Integer categoryId,
                                                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                                    @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<Shop> page = shopService.getShopsByCategoryIdWithPage(categoryId, pageNum, pageSize);
        return ResponseEntity.ok(page);
    }

    /**
     * 添加商家
     * @param shop 商家对象
     * @return 添加结果
     */
    @PostMapping
    public ResponseEntity<?> addShop(@RequestBody Shop shop) {
        // 验证必填字段
        if (shop.getShop_name() == null || shop.getShop_name().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "商家名称不能为空"));
        }
        if (shop.getCategory_id() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "分类不能为空"));
        }
        if (shop.getAddress() == null || shop.getAddress().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "地址不能为空"));
        }
        
        boolean success = shopService.addShop(shop);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED).body(shop);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "添加失败"));
    }

    /**
     * 更新商家
     * @param id 商家ID
     * @param shop 商家对象
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateShop(@PathVariable("id") Long id, @RequestBody Shop shop) {
        Shop existing = shopService.getById(id);
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "商家不存在"));
        }
        
        shop.setShop_id(id);
        boolean success = shopService.updateShop(shop);
        if (success) {
            return ResponseEntity.ok(shop);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "更新失败"));
    }

    /**
     * 删除商家
     * @param id 商家ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteShop(@PathVariable("id") Long id) {
        Shop shop = shopService.getById(id);
        if (shop == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "商家不存在"));
        }
        
        boolean success = shopService.deleteShop(id);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "删除成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "删除失败"));
    }

    /**
     * 批量删除商家
     * @param request 请求体（包含shopIds）
     * @return 删除结果
     */
    @DeleteMapping("/batch")
    public ResponseEntity<?> batchDelete(@RequestBody Map<String, List<Long>> request) {
        List<Long> shopIds = request.get("shopIds");
        if (shopIds == null || shopIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "商家ID列表不能为空"));
        }
        
        boolean success = shopService.batchDelete(shopIds);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "批量删除成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "批量删除失败"));
    }

    /**
     * 更新商家状态
     * @param id 商家ID
     * @param request 请求体（包含status）
     * @return 更新结果
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable("id") Long id,
                                          @RequestBody Map<String, Integer> request) {
        Integer status = request.get("status");
        if (status == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "状态不能为空"));
        }
        
        boolean success = shopService.updateStatus(id, status);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "状态更新成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "状态更新失败"));
    }

    /**
     * 设置为营业中
     * @param id 商家ID
     * @return 更新结果
     */
    @PatchMapping("/{id}/business")
    public ResponseEntity<?> setBusinessStatus(@PathVariable("id") Long id) {
        boolean success = shopService.setBusinessStatus(id);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "已设置为营业中"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "操作失败"));
    }

    /**
     * 设置为休息中
     * @param id 商家ID
     * @return 更新结果
     */
    @PatchMapping("/{id}/rest")
    public ResponseEntity<?> setRestStatus(@PathVariable("id") Long id) {
        boolean success = shopService.setRestStatus(id);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "已设置为休息中"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "操作失败"));
    }

    /**
     * 设置为打烊
     * @param id 商家ID
     * @return 更新结果
     */
    @PatchMapping("/{id}/closed")
    public ResponseEntity<?> setClosedStatus(@PathVariable("id") Long id) {
        boolean success = shopService.setClosedStatus(id);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "已设置为打烊"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "操作失败"));
    }

    /**
     * 认证商家
     * @param id 商家ID
     * @return 认证结果
     */
    @PatchMapping("/{id}/authenticate")
    public ResponseEntity<?> authenticateShop(@PathVariable("id") Long id) {
        boolean success = shopService.authenticateShop(id);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "认证成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "认证失败"));
    }

    /**
     * 取消认证
     * @param id 商家ID
     * @return 取消结果
     */
    @PatchMapping("/{id}/unauthenticate")
    public ResponseEntity<?> unauthenticateShop(@PathVariable("id") Long id) {
        boolean success = shopService.unauthenticateShop(id);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "已取消认证"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "操作失败"));
    }

    /**
     * 增加销量
     * @param id 商家ID
     * @param request 请求体（包含increment）
     * @return 更新结果
     */
    @PatchMapping("/{id}/sales")
    public ResponseEntity<?> incrementSales(@PathVariable("id") Long id,
                                            @RequestBody Map<String, Integer> request) {
        Integer increment = request.get("increment");
        if (increment == null || increment <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "增量必须大于0"));
        }
        
        boolean success = shopService.incrementSalesCount(id, increment);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "销量更新成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "销量更新失败"));
    }

    /**
     * 统计数据
     * @return 统计结果
     */
    @GetMapping("/stats/count")
    public ResponseEntity<Map<String, Long>> getStats() {
        long total = shopService.countTotal();
        long business = shopService.countBusinessShops();
        long auth = shopService.countAuthShops();
        
        return ResponseEntity.ok(Map.of(
                "total", total,
                "business", business,
                "auth", auth
        ));
    }

    /**
     * 统计分类下的商家数量
     * @param categoryId 分类ID
     * @return 数量
     */
    @GetMapping("/stats/category/{categoryId}")
    public ResponseEntity<Map<String, Long>> countByCategoryId(@PathVariable("categoryId") Integer categoryId) {
        long count = shopService.countByCategoryId(categoryId);
        return ResponseEntity.ok(Map.of("count", count));
    }
}
