package com.blue.jitian.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.jitian.Entity.Product;
import com.blue.jitian.Mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;


@Service
public class ProductService extends ServiceImpl<ProductMapper, Product> {

    /**
     * 根据商家ID查询商品
     * @param shopId 商家ID
     * @return 商品列表
     */
    public List<Product> getProductsByShopId(Long shopId) {
        return this.baseMapper.findByShopId(shopId);
    }

    /**
     * 根据分类ID查询商品
     * @param categoryId 分类ID
     * @return 商品列表
     */
    public List<Product> getProductsByCategoryId(Long categoryId) {
        return this.baseMapper.findByCategoryId(categoryId);
    }

    /**
     * 根据商家ID和分类ID查询商品
     * @param shopId 商家ID
     * @param categoryId 分类ID
     * @return 商品列表
     */
    public List<Product> getProductsByShopIdAndCategoryId(Long shopId, Long categoryId) {
        return this.baseMapper.findByShopIdAndCategoryId(shopId, categoryId);
    }

    /**
     * 根据状态查询商品
     * @param status 状态
     * @return 商品列表
     */
    public List<Product> getProductsByStatus(Integer status) {
        return this.baseMapper.findByStatus(status);
    }

    /**
     * 根据商家ID和状态查询商品
     * @param shopId 商家ID
     * @param status 状态
     * @return 商品列表
     */
    public List<Product> getProductsByShopIdAndStatus(Long shopId, Integer status) {
        return this.baseMapper.findByShopIdAndStatus(shopId, status);
    }

    /**
     * 根据名称搜索商品
     * @param productName 商品名称
     * @return 商品列表
     */
    public List<Product> searchProductsByName(String productName) {
        if (!StringUtils.hasText(productName)) {
            return List.of();
        }
        return this.baseMapper.findByNameLike(productName);
    }

    /**
     * 根据商家ID和名称搜索商品
     * @param shopId 商家ID
     * @param productName 商品名称
     * @return 商品列表
     */
    public List<Product> searchProductsByShopIdAndName(Long shopId, String productName) {
        if (!StringUtils.hasText(productName)) {
            return List.of();
        }
        return this.baseMapper.findByShopIdAndNameLike(shopId, productName);
    }

    /**
     * 添加商品
     * @param product 商品对象
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean addProduct(Product product) {
        // 设置默认值
        if (product.getStock() == null) {
            product.setStock(-1);  // 默认无限库存
        }
        if (product.getSales_count() == null) {
            product.setSales_count(0);
        }
        if (product.getStatus() == null) {
            product.setStatus(1);  // 默认上架
        }
        if (product.getSort_order() == null) {
            Integer maxSort = this.baseMapper.getMaxSortOrder(product.getShop_id());
            product.setSort_order(maxSort + 1);
        }
        
        return this.save(product);
    }

    /**
     * 更新商品
     * @param product 商品对象
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProduct(Product product) {
        if (product.getProduct_id() == null) {
            return false;
        }
        return this.updateById(product);
    }

    /**
     * 更新商品状态
     * @param productId 商品ID
     * @param status 状态
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Long productId, Integer status) {
        return this.baseMapper.updateStatus(productId, status) > 0;
    }

    /**
     * 上架商品
     * @param productId 商品ID
     * @return 是否成功
     */
    public boolean onSale(Long productId) {
        return updateStatus(productId, 1);
    }

    /**
     * 下架商品
     * @param productId 商品ID
     * @return 是否成功
     */
    public boolean offSale(Long productId) {
        return updateStatus(productId, 0);
    }

    /**
     * 增加销量
     * @param productId 商品ID
     * @param increment 增加的数量
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean incrementSalesCount(Long productId, Integer increment) {
        if (increment == null || increment <= 0) {
            return false;
        }
        return this.baseMapper.incrementSalesCount(productId, increment) > 0;
    }

    /**
     * 减少库存
     * @param productId 商品ID
     * @param decrement 减少的数量
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean decrementStock(Long productId, Integer decrement) {
        if (decrement == null || decrement <= 0) {
            return false;
        }
        return this.baseMapper.decrementStock(productId, decrement) > 0;
    }

    /**
     * 增加库存
     * @param productId 商品ID
     * @param increment 增加的数量
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean incrementStock(Long productId, Integer increment) {
        if (increment == null || increment <= 0) {
            return false;
        }
        return this.baseMapper.incrementStock(productId, increment) > 0;
    }

    /**
     * 更新排序
     * @param productId 商品ID
     * @param sortOrder 排序值
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSortOrder(Long productId, Integer sortOrder) {
        return this.baseMapper.updateSortOrder(productId, sortOrder) > 0;
    }

    /**
     * 批量更新排序
     * @param products 商品列表（包含id和sortOrder）
     * @return 更新成功的数量
     */
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdateSortOrder(List<Product> products) {
        int count = 0;
        for (Product product : products) {
            if (updateSortOrder(product.getProduct_id(), product.getSort_order())) {
                count++;
            }
        }
        return count;
    }

    /**
     * 删除商品
     * @param productId 商品ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteProduct(Long productId) {
        return this.removeById(productId);
    }

    /**
     * 批量删除商品
     * @param productIds 商品ID列表
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDelete(List<Long> productIds) {
        return this.removeByIds(productIds);
    }

    /**
     * 删除商家的所有商品
     * @param shopId 商家ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByShopId(Long shopId) {
        return this.baseMapper.deleteByShopId(shopId) > 0;
    }

    /**
     * 删除分类下的所有商品
     * @param categoryId 分类ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByCategoryId(Long categoryId) {
        return this.baseMapper.deleteByCategoryId(categoryId) > 0;
    }

    /**
     * 统计商家的商品数量
     * @param shopId 商家ID
     * @return 数量
     */
    public long countByShopId(Long shopId) {
        return this.baseMapper.countByShopId(shopId);
    }

    /**
     * 统计分类下的商品数量
     * @param categoryId 分类ID
     * @return 数量
     */
    public long countByCategoryId(Long categoryId) {
        return this.baseMapper.countByCategoryId(categoryId);
    }

    /**
     * 统计上架商品数量
     * @param shopId 商家ID
     * @return 数量
     */
    public long countOnSaleByShopId(Long shopId) {
        return this.baseMapper.countOnSaleByShopId(shopId);
    }

    /**
     * 查询商家的热销商品
     * @param shopId 商家ID
     * @param limit 限制数量
     * @return 商品列表
     */
    public List<Product> getHotProducts(Long shopId, Integer limit) {
        return this.baseMapper.findHotProductsByShopId(shopId, limit);
    }

    /**
     * 查询商家的新品
     * @param shopId 商家ID
     * @param limit 限制数量
     * @return 商品列表
     */
    public List<Product> getNewProducts(Long shopId, Integer limit) {
        return this.baseMapper.findNewProductsByShopId(shopId, limit);
    }

    /**
     * 获取最大排序值
     * @param shopId 商家ID
     * @return 最大排序值
     */
    public Integer getMaxSortOrder(Long shopId) {
        return this.baseMapper.getMaxSortOrder(shopId);
    }

    /**
     * 分页查询商品
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    public Page<Product> getProductsByPage(Integer pageNum, Integer pageSize) {
        Page<Product> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("created_at");
        return this.page(page, queryWrapper);
    }

    /**
     * 分页查询商家的商品
     * @param shopId 商家ID
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    public Page<Product> getProductsByShopIdWithPage(Long shopId, Integer pageNum, Integer pageSize) {
        Page<Product> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shop_id", shopId);
        queryWrapper.eq("status", 1);
        queryWrapper.orderByAsc("sort_order").orderByDesc("product_id");
        return this.page(page, queryWrapper);
    }

    /**
     * 分页查询分类的商品
     * @param categoryId 分类ID
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    public Page<Product> getProductsByCategoryIdWithPage(Long categoryId, Integer pageNum, Integer pageSize) {
        Page<Product> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId);
        queryWrapper.eq("status", 1);
        queryWrapper.orderByAsc("sort_order").orderByDesc("product_id");
        return this.page(page, queryWrapper);
    }
}
