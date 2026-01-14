package com.blue.jitian.Controller.product;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blue.jitian.Entity.Product;
import com.blue.jitian.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 商品控制器
 * 提供商品相关的REST API接口
 */
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 根据商家ID获取商品
     * @param shopId 商家ID
     * @return 商品列表
     */
    @GetMapping("/shop/{shopId}")
    public ResponseEntity<List<Product>> getProductsByShopId(@PathVariable("shopId") Long shopId) {
        List<Product> products = productService.getProductsByShopId(shopId);
        return ResponseEntity.ok(products);
    }

    /**
     * 根据分类ID获取商品
     * @param categoryId 分类ID
     * @return 商品列表
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategoryId(@PathVariable("categoryId") Long categoryId) {
        List<Product> products = productService.getProductsByCategoryId(categoryId);
        return ResponseEntity.ok(products);
    }

    /**
     * 根据商家ID和分类ID获取商品
     * @param shopId 商家ID
     * @param categoryId 分类ID
     * @return 商品列表
     */
    @GetMapping("/shop/{shopId}/category/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByShopIdAndCategoryId(@PathVariable("shopId") Long shopId,
                                                                          @PathVariable("categoryId") Long categoryId) {
        List<Product> products = productService.getProductsByShopIdAndCategoryId(shopId, categoryId);
        return ResponseEntity.ok(products);
    }

    /**
     * 根据ID获取商品详情
     * @param id 商品ID
     * @return 商品详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long id) {
        Product product = productService.getById(id);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "商品不存在"));
        }
        return ResponseEntity.ok(product);
    }

    /**
     * 搜索商品
     * @param name 商品名称
     * @return 商品列表
     */
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam("name") String name) {
        List<Product> products = productService.searchProductsByName(name);
        return ResponseEntity.ok(products);
    }

    /**
     * 根据商家ID搜索商品
     * @param shopId 商家ID
     * @param name 商品名称
     * @return 商品列表
     */
    @GetMapping("/shop/{shopId}/search")
    public ResponseEntity<List<Product>> searchProductsByShopId(@PathVariable("shopId") Long shopId,
                                                                 @RequestParam("name") String name) {
        List<Product> products = productService.searchProductsByShopIdAndName(shopId, name);
        return ResponseEntity.ok(products);
    }

    /**
     * 获取热销商品
     * @param shopId 商家ID
     * @param limit 限制数量
     * @return 商品列表
     */
    @GetMapping("/shop/{shopId}/hot")
    public ResponseEntity<List<Product>> getHotProducts(@PathVariable("shopId") Long shopId,
                                                         @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        List<Product> products = productService.getHotProducts(shopId, limit);
        return ResponseEntity.ok(products);
    }

    /**
     * 获取新品
     * @param shopId 商家ID
     * @param limit 限制数量
     * @return 商品列表
     */
    @GetMapping("/shop/{shopId}/new")
    public ResponseEntity<List<Product>> getNewProducts(@PathVariable("shopId") Long shopId,
                                                         @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        List<Product> products = productService.getNewProducts(shopId, limit);
        return ResponseEntity.ok(products);
    }

    /**
     * 分页查询商品
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    @GetMapping("/page")
    public ResponseEntity<Page<Product>> getProductsByPage(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<Product> page = productService.getProductsByPage(pageNum, pageSize);
        return ResponseEntity.ok(page);
    }

    /**
     * 分页查询商家商品
     * @param shopId 商家ID
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    @GetMapping("/shop/{shopId}/page")
    public ResponseEntity<Page<Product>> getProductsByShopIdWithPage(@PathVariable("shopId") Long shopId,
                                                                     @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                                     @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<Product> page = productService.getProductsByShopIdWithPage(shopId, pageNum, pageSize);
        return ResponseEntity.ok(page);
    }

    /**
     * 添加商品
     * @param product 商品对象
     * @return 添加结果
     */
    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        // 验证必填字段
        if (product.getShop_id() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "商家ID不能为空"));
        }
        if (product.getCategory_id() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "分类ID不能为空"));
        }
        if (product.getProduct_name() == null || product.getProduct_name().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "商品名称不能为空"));
        }
        if (product.getPrice() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "价格不能为空"));
        }
        
        boolean success = productService.addProduct(product);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED).body(product);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "添加失败"));
    }

    /**
     * 更新商品
     * @param id 商品ID
     * @param product 商品对象
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") Long id, @RequestBody Product product) {
        Product existing = productService.getById(id);
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "商品不存在"));
        }
        
        product.setProduct_id(id);
        boolean success = productService.updateProduct(product);
        if (success) {
            return ResponseEntity.ok(product);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "更新失败"));
    }

    /**
     * 删除商品
     * @param id 商品ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {
        Product product = productService.getById(id);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "商品不存在"));
        }
        
        boolean success = productService.deleteProduct(id);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "删除成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "删除失败"));
    }

    /**
     * 批量删除商品
     * @param request 请求体（包含productIds）
     * @return 删除结果
     */
    @DeleteMapping("/batch")
    public ResponseEntity<?> batchDelete(@RequestBody Map<String, List<Long>> request) {
        List<Long> productIds = request.get("productIds");
        if (productIds == null || productIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "商品ID列表不能为空"));
        }
        
        boolean success = productService.batchDelete(productIds);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "批量删除成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "批量删除失败"));
    }

    /**
     * 上架商品
     * @param id 商品ID
     * @return 操作结果
     */
    @PatchMapping("/{id}/on-sale")
    public ResponseEntity<?> onSale(@PathVariable("id") Long id) {
        boolean success = productService.onSale(id);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "上架成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "上架失败"));
    }

    /**
     * 下架商品
     * @param id 商品ID
     * @return 操作结果
     */
    @PatchMapping("/{id}/off-sale")
    public ResponseEntity<?> offSale(@PathVariable("id") Long id) {
        boolean success = productService.offSale(id);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "下架成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "下架失败"));
    }

    /**
     * 更新库存
     * @param id 商品ID
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
        
        Product product = productService.getById(id);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "商品不存在"));
        }
        
        product.setStock(stock);
        boolean success = productService.updateProduct(product);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "库存更新成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "库存更新失败"));
    }

    /**
     * 更新排序
     * @param id 商品ID
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
        
        boolean success = productService.updateSortOrder(id, sortOrder);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "排序更新成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "排序更新失败"));
    }

    /**
     * 批量更新排序
     * @param products 商品列表（包含id和sortOrder）
     * @return 更新结果
     */
    @PatchMapping("/batch/sort")
    public ResponseEntity<?> batchUpdateSortOrder(@RequestBody List<Product> products) {
        if (products == null || products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "商品列表不能为空"));
        }
        
        int count = productService.batchUpdateSortOrder(products);
        return ResponseEntity.ok(Map.of(
                "message", "批量更新排序成功",
                "count", count
        ));
    }

    /**
     * 统计商家商品数量
     * @param shopId 商家ID
     * @return 统计结果
     */
    @GetMapping("/stats/shop/{shopId}")
    public ResponseEntity<Map<String, Long>> getStats(@PathVariable("shopId") Long shopId) {
        long total = productService.countByShopId(shopId);
        long onSale = productService.countOnSaleByShopId(shopId);
        
        return ResponseEntity.ok(Map.of(
                "total", total,
                "onSale", onSale,
                "offSale", total - onSale
        ));
    }

    /**
     * 统计分类商品数量
     * @param categoryId 分类ID
     * @return 数量
     */
    @GetMapping("/stats/category/{categoryId}")
    public ResponseEntity<Map<String, Long>> countByCategoryId(@PathVariable("categoryId") Long categoryId) {
        long count = productService.countByCategoryId(categoryId);
        return ResponseEntity.ok(Map.of("count", count));
    }
}
