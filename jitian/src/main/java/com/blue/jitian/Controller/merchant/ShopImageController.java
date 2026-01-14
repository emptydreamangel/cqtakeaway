package com.blue.jitian.Controller.merchant;

import com.blue.jitian.Entity.ShopImage;
import com.blue.jitian.Service.ShopImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 商家图片控制器
 * 提供商家图片相关的REST API接口
 */
@RestController
@RequestMapping("/api/shop-image")
public class ShopImageController {

    @Autowired
    private ShopImageService imageService;

    /**
     * 根据商家ID获取所有图片
     * @param shopId 商家ID
     * @return 图片列表
     */
    @GetMapping("/shop/{shopId}")
    public ResponseEntity<List<ShopImage>> getImagesByShopId(@PathVariable("shopId") Long shopId) {
        List<ShopImage> images = imageService.getImagesByShopId(shopId);
        return ResponseEntity.ok(images);
    }

    /**
     * 根据商家ID和类型获取图片
     * @param shopId 商家ID
     * @param type 图片类型（1:环境图, 2:菜品图）
     * @return 图片列表
     */
    @GetMapping("/shop/{shopId}/type/{type}")
    public ResponseEntity<List<ShopImage>> getImagesByShopIdAndType(@PathVariable("shopId") Long shopId,
                                                                     @PathVariable("type") Integer type) {
        List<ShopImage> images = imageService.getImagesByShopIdAndType(shopId, type);
        return ResponseEntity.ok(images);
    }

    /**
     * 获取商家的环境图
     * @param shopId 商家ID
     * @return 环境图列表
     */
    @GetMapping("/shop/{shopId}/environment")
    public ResponseEntity<List<ShopImage>> getEnvironmentImages(@PathVariable("shopId") Long shopId) {
        List<ShopImage> images = imageService.getEnvironmentImages(shopId);
        return ResponseEntity.ok(images);
    }

    /**
     * 获取商家的菜品图
     * @param shopId 商家ID
     * @return 菜品图列表
     */
    @GetMapping("/shop/{shopId}/dish")
    public ResponseEntity<List<ShopImage>> getDishImages(@PathVariable("shopId") Long shopId) {
        List<ShopImage> images = imageService.getDishImages(shopId);
        return ResponseEntity.ok(images);
    }

    /**
     * 根据ID获取图片详情
     * @param id 图片ID
     * @return 图片详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getImageById(@PathVariable("id") Long id) {
        ShopImage image = imageService.getById(id);
        if (image == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "图片不存在"));
        }
        return ResponseEntity.ok(image);
    }

    /**
     * 添加图片
     * @param image 图片对象
     * @return 添加结果
     */
    @PostMapping
    public ResponseEntity<?> addImage(@RequestBody ShopImage image) {
        // 验证必填字段
        if (image.getShop_id() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "商家ID不能为空"));
        }
        if (image.getImage_url() == null || image.getImage_url().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "图片URL不能为空"));
        }
        
        boolean success = imageService.addImage(image);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED).body(image);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "添加失败"));
    }

    /**
     * 批量添加图片
     * @param request 请求体（包含images列表）
     * @return 添加结果
     */
    @PostMapping("/batch")
    public ResponseEntity<?> batchAddImages(@RequestBody Map<String, List<ShopImage>> request) {
        List<ShopImage> images = request.get("images");
        if (images == null || images.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "图片列表不能为空"));
        }
        
        boolean success = imageService.batchAddImages(images);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "批量添加成功", "count", images.size()));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "批量添加失败"));
    }

    /**
     * 更新图片
     * @param id 图片ID
     * @param image 图片对象
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateImage(@PathVariable("id") Long id, @RequestBody ShopImage image) {
        ShopImage existing = imageService.getById(id);
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "图片不存在"));
        }
        
        image.setImage_id(id);
        boolean success = imageService.updateImage(image);
        if (success) {
            return ResponseEntity.ok(image);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "更新失败"));
    }

    /**
     * 更新图片排序
     * @param id 图片ID
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
        
        boolean success = imageService.updateSortOrder(id, sortOrder);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "排序更新成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "排序更新失败"));
    }

    /**
     * 批量更新排序
     * @param images 图片列表（包含id和sortOrder）
     * @return 更新结果
     */
    @PatchMapping("/batch/sort")
    public ResponseEntity<?> batchUpdateSortOrder(@RequestBody List<ShopImage> images) {
        if (images == null || images.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "图片列表不能为空"));
        }
        
        int count = imageService.batchUpdateSortOrder(images);
        return ResponseEntity.ok(Map.of(
                "message", "批量更新排序成功",
                "count", count
        ));
    }

    /**
     * 删除图片
     * @param id 图片ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable("id") Long id) {
        ShopImage image = imageService.getById(id);
        if (image == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "图片不存在"));
        }
        
        boolean success = imageService.deleteImage(id);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "删除成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "删除失败"));
    }

    /**
     * 批量删除图片
     * @param request 请求体（包含imageIds）
     * @return 删除结果
     */
    @DeleteMapping("/batch")
    public ResponseEntity<?> batchDelete(@RequestBody Map<String, List<Long>> request) {
        List<Long> imageIds = request.get("imageIds");
        if (imageIds == null || imageIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "图片ID列表不能为空"));
        }
        
        boolean success = imageService.batchDelete(imageIds);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "批量删除成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "批量删除失败"));
    }

    /**
     * 删除商家的所有图片
     * @param shopId 商家ID
     * @return 删除结果
     */
    @DeleteMapping("/shop/{shopId}")
    public ResponseEntity<?> deleteByShopId(@PathVariable("shopId") Long shopId) {
        boolean success = imageService.deleteByShopId(shopId);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "删除成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "删除失败"));
    }

    /**
     * 删除商家指定类型的所有图片
     * @param shopId 商家ID
     * @param type 图片类型
     * @return 删除结果
     */
    @DeleteMapping("/shop/{shopId}/type/{type}")
    public ResponseEntity<?> deleteByShopIdAndType(@PathVariable("shopId") Long shopId,
                                                    @PathVariable("type") Integer type) {
        boolean success = imageService.deleteByShopIdAndType(shopId, type);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "删除成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "删除失败"));
    }

    /**
     * 统计商家图片数量
     * @param shopId 商家ID
     * @return 统计结果
     */
    @GetMapping("/stats/shop/{shopId}")
    public ResponseEntity<Map<String, Long>> getStats(@PathVariable("shopId") Long shopId) {
        long total = imageService.countByShopId(shopId);
        long environment = imageService.countByShopIdAndType(shopId, 1);
        long dish = imageService.countByShopIdAndType(shopId, 2);
        
        return ResponseEntity.ok(Map.of(
                "total", total,
                "environment", environment,
                "dish", dish
        ));
    }

    /**
     * 获取最大排序值
     * @param shopId 商家ID
     * @return 最大排序值
     */
    @GetMapping("/max-sort/{shopId}")
    public ResponseEntity<Map<String, Integer>> getMaxSortOrder(@PathVariable("shopId") Long shopId) {
        Integer maxSort = imageService.getMaxSortOrder(shopId);
        return ResponseEntity.ok(Map.of("maxSortOrder", maxSort));
    }
}
