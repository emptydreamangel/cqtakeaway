package com.blue.jitian.Controller.merchant;

import com.blue.jitian.Entity.ShopCategory;
import com.blue.jitian.Service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 商家分类控制器
 * 提供商家分类相关的REST API接口
 */
@RestController
@RequestMapping("/api/category")
public class ShopCategoryController {

    @Autowired
    private ShopCategoryService categoryService;

    /**
     * 获取所有启用的分类（用于前端展示）
     * @return 分类列表
     */
    @GetMapping("/enabled")
    public ResponseEntity<List<ShopCategory>> getEnabledCategories() {
        List<ShopCategory> categories = categoryService.getAllEnabledCategories();
        return ResponseEntity.ok(categories);
    }

    /**
     * 获取所有分类（按排序，用于后台管理）
     * @return 分类列表
     */
    @GetMapping("/all")
    public ResponseEntity<List<ShopCategory>> getAllCategories() {
        List<ShopCategory> categories = categoryService.getAllCategoriesOrdered();
        return ResponseEntity.ok(categories);
    }

    /**
     * 根据状态获取分类
     * @param status 状态（0:禁用，1:启用）
     * @return 分类列表
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ShopCategory>> getCategoriesByStatus(@PathVariable("status") Integer status) {
        List<ShopCategory> categories = categoryService.getCategoriesByStatus(status);
        return ResponseEntity.ok(categories);
    }

    /**
     * 根据ID获取分类详情
     * @param id 分类ID
     * @return 分类详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable("id") Integer id) {
        ShopCategory category = categoryService.getById(id);
        if (category == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "分类不存在"));
        }
        return ResponseEntity.ok(category);
    }

    /**
     * 根据名称查询分类
     * @param name 分类名称
     * @return 分类详情
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<?> getCategoryByName(@PathVariable("name") String name) {
        ShopCategory category = categoryService.getByName(name);
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
    public ResponseEntity<?> addCategory(@RequestBody ShopCategory category) {
        // 验证必填字段
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
     * 更新分类
     * @param id 分类ID
     * @param category 分类对象
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") Integer id, 
                                            @RequestBody ShopCategory category) {
        ShopCategory existing = categoryService.getById(id);
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
     * 删除分类
     * @param id 分类ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Integer id) {
        ShopCategory category = categoryService.getById(id);
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
    public ResponseEntity<?> batchDelete(@RequestBody Map<String, List<Integer>> request) {
        List<Integer> categoryIds = request.get("categoryIds");
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
     * 更新分类状态
     * @param id 分类ID
     * @param request 请求体（包含status）
     * @return 更新结果
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable("id") Integer id,
                                          @RequestBody Map<String, Integer> request) {
        Integer status = request.get("status");
        if (status == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "状态不能为空"));
        }
        
        boolean success = categoryService.updateStatus(id, status);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "状态更新成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "状态更新失败"));
    }

    /**
     * 启用分类
     * @param id 分类ID
     * @return 启用结果
     */
    @PatchMapping("/{id}/enable")
    public ResponseEntity<?> enableCategory(@PathVariable("id") Integer id) {
        boolean success = categoryService.enableCategory(id);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "启用成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "启用失败"));
    }

    /**
     * 禁用分类
     * @param id 分类ID
     * @return 禁用结果
     */
    @PatchMapping("/{id}/disable")
    public ResponseEntity<?> disableCategory(@PathVariable("id") Integer id) {
        boolean success = categoryService.disableCategory(id);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "禁用成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "禁用失败"));
    }

    /**
     * 更新排序
     * @param id 分类ID
     * @param request 请求体（包含sortOrder）
     * @return 更新结果
     */
    @PatchMapping("/{id}/sort")
    public ResponseEntity<?> updateSortOrder(@PathVariable("id") Integer id,
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
    public ResponseEntity<?> batchUpdateSortOrder(@RequestBody List<ShopCategory> categories) {
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
     * 检查分类名称是否存在
     * @param name 分类名称
     * @return 检查结果
     */
    @GetMapping("/check/name/{name}")
    public ResponseEntity<Map<String, Boolean>> checkNameExist(@PathVariable("name") String name) {
        boolean exists = categoryService.isNameExist(name);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    /**
     * 统计分类数量
     * @return 统计结果
     */
    @GetMapping("/stats/count")
    public ResponseEntity<Map<String, Long>> getStats() {
        long total = categoryService.countTotal();
        long enabled = categoryService.countEnabled();
        long disabled = categoryService.countDisabled();
        
        return ResponseEntity.ok(Map.of(
                "total", total,
                "enabled", enabled,
                "disabled", disabled
        ));
    }

    /**
     * 获取最大排序值
     * @return 最大排序值
     */
    @GetMapping("/max-sort")
    public ResponseEntity<Map<String, Integer>> getMaxSortOrder() {
        Integer maxSort = categoryService.getMaxSortOrder();
        return ResponseEntity.ok(Map.of("maxSortOrder", maxSort));
    }
}
