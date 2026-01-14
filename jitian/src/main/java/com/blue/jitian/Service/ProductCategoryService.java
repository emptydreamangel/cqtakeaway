package com.blue.jitian.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.jitian.Entity.ProductCategory;
import com.blue.jitian.Mapper.ProductCategoryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;


@Service
public class ProductCategoryService extends ServiceImpl<ProductCategoryMapper, ProductCategory> {

    /**
     * 根据商家ID查询所有分类
     * @param shopId 商家ID
     * @return 分类列表
     */
    public List<ProductCategory> getCategoriesByShopId(Long shopId) {
        return this.baseMapper.findByShopId(shopId);
    }

    /**
     * 根据商家ID和名称查询分类
     * @param shopId 商家ID
     * @param categoryName 分类名称
     * @return 分类对象
     */
    public ProductCategory getByShopIdAndName(Long shopId, String categoryName) {
        if (!StringUtils.hasText(categoryName)) {
            return null;
        }
        return this.baseMapper.findByShopIdAndName(shopId, categoryName);
    }

    /**
     * 检查分类名称是否存在（同一商家下）
     * @param shopId 商家ID
     * @param categoryName 分类名称
     * @return 是否存在
     */
    public boolean isNameExist(Long shopId, String categoryName) {
        if (!StringUtils.hasText(categoryName)) {
            return false;
        }
        return this.baseMapper.countByShopIdAndName(shopId, categoryName) > 0;
    }

    /**
     * 检查分类名称是否存在（排除指定ID）
     * @param shopId 商家ID
     * @param categoryName 分类名称
     * @param categoryId 要排除的分类ID
     * @return 是否存在
     */
    public boolean isNameExistExcludeId(Long shopId, String categoryName, Long categoryId) {
        if (!StringUtils.hasText(categoryName) || categoryId == null) {
            return false;
        }
        return this.baseMapper.countByShopIdAndNameExcludeId(shopId, categoryName, categoryId) > 0;
    }

    /**
     * 添加分类
     * @param category 分类对象
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean addCategory(ProductCategory category) {
        // 检查名称是否已存在
        if (isNameExist(category.getShop_id(), category.getCategory_name())) {
            return false;
        }
        
        // 如果没有设置排序，自动设置为最大值+1
        if (category.getSort_order() == null) {
            Integer maxSort = this.baseMapper.getMaxSortOrder(category.getShop_id());
            category.setSort_order(maxSort + 1);
        }
        
        return this.save(category);
    }

    /**
     * 批量添加分类
     * @param categories 分类列表
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean batchAddCategories(List<ProductCategory> categories) {
        if (categories == null || categories.isEmpty()) {
            return false;
        }
        
        // 为没有设置排序的分类自动设置排序
        Long shopId = categories.get(0).getShop_id();
        Integer maxSort = this.baseMapper.getMaxSortOrder(shopId);
        
        for (ProductCategory category : categories) {
            if (category.getSort_order() == null) {
                maxSort++;
                category.setSort_order(maxSort);
            }
        }
        
        return this.baseMapper.batchInsert(categories) > 0;
    }

    /**
     * 更新分类
     * @param category 分类对象
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCategory(ProductCategory category) {
        if (category.getCategory_id() == null) {
            return false;
        }
        
        // 检查名称是否与其他分类重复
        if (category.getCategory_name() != null) {
            ProductCategory existing = this.getById(category.getCategory_id());
            if (existing != null && 
                isNameExistExcludeId(existing.getShop_id(), category.getCategory_name(), category.getCategory_id())) {
                return false;
            }
        }
        
        return this.updateById(category);
    }

    /**
     * 更新排序
     * @param categoryId 分类ID
     * @param sortOrder 排序值
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSortOrder(Long categoryId, Integer sortOrder) {
        return this.baseMapper.updateSortOrder(categoryId, sortOrder) > 0;
    }

    /**
     * 批量更新排序
     * @param categories 分类列表（包含id和sortOrder）
     * @return 更新成功的数量
     */
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdateSortOrder(List<ProductCategory> categories) {
        int count = 0;
        for (ProductCategory category : categories) {
            if (updateSortOrder(category.getCategory_id(), category.getSort_order())) {
                count++;
            }
        }
        return count;
    }

    /**
     * 删除分类
     * @param categoryId 分类ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCategory(Long categoryId) {
        return this.removeById(categoryId);
    }

    /**
     * 批量删除分类
     * @param categoryIds 分类ID列表
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDelete(List<Long> categoryIds) {
        return this.removeByIds(categoryIds);
    }

    /**
     * 删除商家的所有分类
     * @param shopId 商家ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByShopId(Long shopId) {
        return this.baseMapper.deleteByShopId(shopId) > 0;
    }

    /**
     * 统计商家的分类数量
     * @param shopId 商家ID
     * @return 数量
     */
    public long countByShopId(Long shopId) {
        return this.baseMapper.countByShopId(shopId);
    }

    /**
     * 获取商家分类的最大排序值
     * @param shopId 商家ID
     * @return 最大排序值
     */
    public Integer getMaxSortOrder(Long shopId) {
        return this.baseMapper.getMaxSortOrder(shopId);
    }
}
