package com.blue.jitian.Controller.product;

import com.blue.jitian.Entity.ProductCategory;
import com.blue.jitian.Service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 商品分类控制器
 * 提供商品分类相关的REST API接口
 */
@RestController
@RequestMapping("/api/product-category")
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService categoryService;

    /**
     * 根据商家ID获取所有商品分类
     * @param shopId 商家ID
     * @return 分类列表
     */
    @GetMapping("/shop/{shopId}")
    public ResponseEntity<List<ProductCategory>> getCategoriesByShopId(@PathVariable("shopId") Long shopId) {
        List<ProductCategory> categories = categoryService.getCategoriesByShopId(shopId);
        return ResponseEntity.ok(categories);
    }

    /**
     * 根据ID获取分类详情
     * @param id 分类ID
     * @return 分类详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable("id") Long id) {
        ProductCategory category = categoryService.getById(id);
        if (category == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "分类不存在"));
        }
        return ResponseEntity.ok(category);
    }

    /**
     * 根据商家ID和名称查询分类
     * @param shopId 商家ID
     * @param name 分类名称
     * @return 分类详情
     */
    @GetMapping("/shop/{shopId}/name/{name}")
    public ResponseEntity<?> getCategoryByShopIdAndName(@PathVariable("shopId") Long shopId,
                                                         @PathVariable("name") String name) {
        ProductCategory category = categoryService.getByShopIdAndName(shopId, name);
        if (category == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "分类不存在"));
        }
        return ResponseEntity.ok(category);
    }

    /**
     * 添加分类
     * @param category 分类对象
     * @return 添加结果
     */
    @PostMapping
    public ResponseEntity<?> addCategory(@RequestBody ProductCategory category) {
        // 验证必填字段
        if (category.getShop_id() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "商家ID不能为空"));
        }
        if (category.getCategory_name() == null || category.getCategory_name().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "分类名称不能为空"));
        }
        
        boolean success = categoryService.addCategory(category);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED).body(category);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("message", "分类名称已存在"));
    }

    /**
     * 批量添加分类
     * @param request 请求体（包含categories列表）
     * @return 添加结果
     */
    @PostMapping("/batch")
    public ResponseEntity<?> batchAddCategories(@RequestBody Map<String, List<ProductCategory>> request) {
        List<ProductCategory> categories = request.get("categories");
        if (categories == null || categories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "分类列表不能为空"));
        }
        
        boolean success = categoryService.batchAddCategories(categories);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "批量添加成功", "count", categories.size()));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "批量添加失败"));
    }

    /**
     * 更新分类
     * @param id 分类ID
     * @param category 分类对象
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") Long id, @RequestBody ProductCategory category) {
        ProductCategory existing = categoryService.getById(id);
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "分类不存在"));
        }
        
        category.setCategory_id(id);
        boolean success = categoryService.updateCategory(category);
        if (success) {
            return ResponseEntity.ok(category);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("message", "分类名称已存在"));
    }

    /**
     * 更新排序
     * @param id 分类ID
     * @param request 请求体（包含sortOrder）
     * @return 更新结果
     */
    @PatchMapping("/{id}/sort")
    public ResponseEntity<?> updateSortOrder(@PathVariable("id") Long id,
                                             @RequestBody Map<String, Integer> request) {
        Integer sortOrder = request.get("sortOrder");
        if (sortOrder == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "排序值不能为空"));
        }
        
        boolean success = categoryService.updateSortOrder(id, sortOrder);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "排序更新成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "排序更新失败"));
    }

    /**
     * 批量更新排序
     * @param categories 分类列表（包含id和sortOrder）
     * @return 更新结果
     */
    @PatchMapping("/batch/sort")
    public ResponseEntity<?> batchUpdateSortOrder(@RequestBody List<ProductCategory> categories) {
        if (categories == null || categories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "分类列表不能为空"));
        }
        
        int count = categoryService.batchUpdateSortOrder(categories);
        return ResponseEntity.ok(Map.of(
                "message", "批量更新排序成功",
                "count", count
        ));
    }

    /**
     * 删除分类
     * @param id 分类ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id) {
        ProductCategory category = categoryService.getById(id);
        if (category == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "分类不存在"));
        }
        
        boolean success = categoryService.deleteCategory(id);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "删除成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "删除失败"));
    }

    /**
     * 批量删除分类
     * @param request 请求体（包含categoryIds）
     * @return 删除结果
     */
    @DeleteMapping("/batch")
    public ResponseEntity<?> batchDelete(@RequestBody Map<String, List<Long>> request) {
        List<Long> categoryIds = request.get("categoryIds");
        if (categoryIds == null || categoryIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "分类ID列表不能为空"));
        }
        
        boolean success = categoryService.batchDelete(categoryIds);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "批量删除成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "批量删除失败"));
    }

    /**
     * 删除商家的所有分类
     * @param shopId 商家ID
     * @return 删除结果
     */
    @DeleteMapping("/shop/{shopId}")
    public ResponseEntity<?> deleteByShopId(@PathVariable("shopId") Long shopId) {
        boolean success = categoryService.deleteByShopId(shopId);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "删除成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "删除失败"));
    }

    /**
     * 检查分类名称是否存在
     * @param shopId 商家ID
     * @param name 分类名称
     * @return 检查结果
     */
    @GetMapping("/check/shop/{shopId}/name/{name}")
    public ResponseEntity<Map<String, Boolean>> checkNameExist(@PathVariable("shopId") Long shopId,
                                                                @PathVariable("name") String name) {
        boolean exists = categoryService.isNameExist(shopId, name);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    /**
     * 统计商家的分类数量
     * @param shopId 商家ID
     * @return 数量
     */
    @GetMapping("/stats/shop/{shopId}")
    public ResponseEntity<Map<String, Long>> countByShopId(@PathVariable("shopId") Long shopId) {
        long count = categoryService.countByShopId(shopId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    /**
     * 获取商家分类的最大排序值
     * @param shopId 商家ID
     * @return 最大排序值
     */
    @GetMapping("/max-sort/{shopId}")
    public ResponseEntity<Map<String, Integer>> getMaxSortOrder(@PathVariable("shopId") Long shopId) {
        Integer maxSort = categoryService.getMaxSortOrder(shopId);
        return ResponseEntity.ok(Map.of("maxSortOrder", maxSort));
    }
}
