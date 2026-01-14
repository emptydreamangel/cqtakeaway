package com.blue.jitian.Controller.product;

import com.blue.jitian.Entity.ProductSpec;
import com.blue.jitian.Service.ProductSpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 商品规格控制器
 * 提供商品规格相关的REST API接口
 */
@RestController
@RequestMapping("/api/product-spec")
public class ProductSpecController {

    @Autowired
    private ProductSpecService specService;

    /**
     * 根据商品ID获取所有规格
     * @param productId 商品ID
     * @return 规格列表
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductSpec>> getSpecsByProductId(@PathVariable("productId") Long productId) {
        List<ProductSpec> specs = specService.getSpecsByProductId(productId);
        return ResponseEntity.ok(specs);
    }

    /**
     * 根据ID获取规格详情
     * @param id 规格ID
     * @return 规格详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getSpecById(@PathVariable("id") Long id) {
        ProductSpec spec = specService.getById(id);
        if (spec == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "规格不存在"));
        }
        return ResponseEntity.ok(spec);
    }

    /**
     * 根据商品ID和名称查询规格
     * @param productId 商品ID
     * @param name 规格名称
     * @return 规格详情
     */
    @GetMapping("/product/{productId}/name/{name}")
    public ResponseEntity<?> getSpecByProductIdAndName(@PathVariable("productId") Long productId,
                                                        @PathVariable("name") String name) {
        ProductSpec spec = specService.getByProductIdAndName(productId, name);
        if (spec == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "规格不存在"));
        }
        return ResponseEntity.ok(spec);
    }

    /**
     * 添加规格
     * @param spec 规格对象
     * @return 添加结果
     */
    @PostMapping
    public ResponseEntity<?> addSpec(@RequestBody ProductSpec spec) {
        // 验证必填字段
        if (spec.getProduct_id() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "商品ID不能为空"));
        }
        if (spec.getSpec_name() == null || spec.getSpec_name().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "规格名称不能为空"));
        }
        
        boolean success = specService.addSpec(spec);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED).body(spec);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("message", "规格名称已存在"));
    }

    /**
     * 批量添加规格
     * @param request 请求体（包含specs列表）
     * @return 添加结果
     */
    @PostMapping("/batch")
    public ResponseEntity<?> batchAddSpecs(@RequestBody Map<String, List<ProductSpec>> request) {
        List<ProductSpec> specs = request.get("specs");
        if (specs == null || specs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "规格列表不能为空"));
        }
        
        boolean success = specService.batchAddSpecs(specs);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "批量添加成功", "count", specs.size()));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "批量添加失败"));
    }

    /**
     * 更新规格
     * @param id 规格ID
     * @param spec 规格对象
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSpec(@PathVariable("id") Long id, @RequestBody ProductSpec spec) {
        ProductSpec existing = specService.getById(id);
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "规格不存在"));
        }
        
        spec.setSpec_id(id);
        boolean success = specService.updateSpec(spec);
        if (success) {
            return ResponseEntity.ok(spec);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("message", "规格名称已存在"));
    }

    /**
     * 更新排序
     * @param id 规格ID
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
        
        boolean success = specService.updateSortOrder(id, sortOrder);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "排序更新成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "排序更新失败"));
    }

    /**
     * 批量更新排序
     * @param specs 规格列表（包含id和sortOrder）
     * @return 更新结果
     */
    @PatchMapping("/batch/sort")
    public ResponseEntity<?> batchUpdateSortOrder(@RequestBody List<ProductSpec> specs) {
        if (specs == null || specs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "规格列表不能为空"));
        }
        
        int count = specService.batchUpdateSortOrder(specs);
        return ResponseEntity.ok(Map.of(
                "message", "批量更新排序成功",
                "count", count
        ));
    }

    /**
     * 更新库存
     * @param id 规格ID
     * @param request 请求体（包含stock）
     * @return 更新结果
     */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<?> updateStock(@PathVariable("id") Long id,
                                         @RequestBody Map<String, Integer> request) {
        Integer stock = request.get("stock");
        if (stock == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "库存值不能为空"));
        }
        
        ProductSpec spec = specService.getById(id);
        if (spec == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "规格不存在"));
        }
        
        spec.setStock(stock);
        boolean success = specService.updateSpec(spec);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "库存更新成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "库存更新失败"));
    }

    /**
     * 删除规格
     * @param id 规格ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSpec(@PathVariable("id") Long id) {
        ProductSpec spec = specService.getById(id);
        if (spec == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "规格不存在"));
        }
        
        boolean success = specService.deleteSpec(id);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "删除成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "删除失败"));
    }

    /**
     * 批量删除规格
     * @param request 请求体（包含specIds）
     * @return 删除结果
     */
    @DeleteMapping("/batch")
    public ResponseEntity<?> batchDelete(@RequestBody Map<String, List<Long>> request) {
        List<Long> specIds = request.get("specIds");
        if (specIds == null || specIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "规格ID列表不能为空"));
        }
        
        boolean success = specService.batchDelete(specIds);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "批量删除成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "批量删除失败"));
    }

    /**
     * 删除商品的所有规格
     * @param productId 商品ID
     * @return 删除结果
     */
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<?> deleteByProductId(@PathVariable("productId") Long productId) {
        boolean success = specService.deleteByProductId(productId);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "删除成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "删除失败"));
    }

    /**
     * 检查规格名称是否存在
     * @param productId 商品ID
     * @param name 规格名称
     * @return 检查结果
     */
    @GetMapping("/check/product/{productId}/name/{name}")
    public ResponseEntity<Map<String, Boolean>> checkNameExist(@PathVariable("productId") Long productId,
                                                                @PathVariable("name") String name) {
        boolean exists = specService.isNameExist(productId, name);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    /**
     * 统计商品的规格数量
     * @param productId 商品ID
     * @return 数量
     */
    @GetMapping("/stats/product/{productId}")
    public ResponseEntity<Map<String, Long>> countByProductId(@PathVariable("productId") Long productId) {
        long count = specService.countByProductId(productId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    /**
     * 获取最大排序值
     * @param productId 商品ID
     * @return 最大排序值
     */
    @GetMapping("/max-sort/{productId}")
    public ResponseEntity<Map<String, Integer>> getMaxSortOrder(@PathVariable("productId") Long productId) {
        Integer maxSort = specService.getMaxSortOrder(productId);
        return ResponseEntity.ok(Map.of("maxSortOrder", maxSort));
    }
}
