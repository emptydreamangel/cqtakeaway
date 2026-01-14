package com.blue.jitian.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.jitian.Entity.ShopCategory;
import com.blue.jitian.Mapper.ShopCategoryMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;


@Service
public class ShopCategoryService extends ServiceImpl<ShopCategoryMapper, ShopCategory> {

    /**
     * 获取所有启用的分类（按排序）
     * @return 分类列表
     */
    public List<ShopCategory> getAllEnabledCategories() {
        return this.baseMapper.findAllEnabled();
    }

    /**
     * 根据状态获取分类
     * @param status 状态（0:禁用，1:启用）
     * @return 分类列表
     */
    public List<ShopCategory> getCategoriesByStatus(Integer status) {
        return this.baseMapper.findByStatus(status);
    }

    /**
     * 获取所有分类（按排序）
     * @return 分类列表
     */
    public List<ShopCategory> getAllCategoriesOrdered() {
        return this.baseMapper.findAllOrdered();
    }

    /**
     * 根据名称查询分类
     * @param categoryName 分类名称
     * @return 分类对象
     */
    public ShopCategory getByName(String categoryName) {
        if (!StringUtils.hasText(categoryName)) {
            return null;
        }
        return this.baseMapper.findByName(categoryName);
    }

    /**
     * 检查分类名称是否存在
     * @param categoryName 分类名称
     * @return 是否存在
     */
    public boolean isNameExist(String categoryName) {
        if (!StringUtils.hasText(categoryName)) {
            return false;
        }
        return this.baseMapper.countByName(categoryName) > 0;
    }

    /**
     * 检查分类名称是否存在（排除指定ID）
     * @param categoryName 分类名称
     * @param categoryId 要排除的分类ID
     * @return 是否存在
     */
    public boolean isNameExistExcludeId(String categoryName, Integer categoryId) {
        if (!StringUtils.hasText(categoryName) || categoryId == null) {
            return false;
        }
        return this.baseMapper.countByNameExcludeId(categoryName, categoryId) > 0;
    }

    /**
     * 添加分类
     * @param category 分类对象
     * @return 是否成功
     */
    public boolean addCategory(ShopCategory category) {
        // 检查名称是否已存在
        if (isNameExist(category.getCategory_name())) {
            return false;
        }
        
        // 如果没有设置排序，自动设置为最大值+1
        if (category.getSort_order() == null) {
            Integer maxSort = this.baseMapper.getMaxSortOrder();
            category.setSort_order(maxSort + 1);
        }
        
        // 如果没有设置状态，默认为启用
        if (category.getStatus() == null) {
            category.setStatus(1);
        }
        
        return this.save(category);
    }

    /**
     * 更新分类
     * @param category 分类对象
     * @return 是否成功
     */
    public boolean updateCategory(ShopCategory category) {
        if (category.getCategory_id() == null) {
            return false;
        }
        
        // 检查名称是否与其他分类重复
        if (category.getCategory_name() != null && 
            isNameExistExcludeId(category.getCategory_name(), category.getCategory_id())) {
            return false;
        }
        
        return this.updateById(category);
    }

    /**
     * 更新分类状态
     * @param categoryId 分类ID
     * @param status 状态
     * @return 是否成功
     */
    public boolean updateStatus(Integer categoryId, Integer status) {
        return this.baseMapper.updateStatus(categoryId, status) > 0;
    }

    /**
     * 启用分类
     * @param categoryId 分类ID
     * @return 是否成功
     */
    public boolean enableCategory(Integer categoryId) {
        return updateStatus(categoryId, 1);
    }

    /**
     * 禁用分类
     * @param categoryId 分类ID
     * @return 是否成功
     */
    public boolean disableCategory(Integer categoryId) {
        return updateStatus(categoryId, 0);
    }

    /**
     * 更新排序
     * @param categoryId 分类ID
     * @param sortOrder 排序值
     * @return 是否成功
     */
    public boolean updateSortOrder(Integer categoryId, Integer sortOrder) {
        return this.baseMapper.updateSortOrder(categoryId, sortOrder) > 0;
    }

    /**
     * 删除分类
     * @param categoryId 分类ID
     * @return 是否成功
     */
    public boolean deleteCategory(Integer categoryId) {
        return this.removeById(categoryId);
    }

    /**
     * 批量删除分类
     * @param categoryIds 分类ID列表
     * @return 是否成功
     */
    public boolean batchDelete(List<Integer> categoryIds) {
        return this.removeByIds(categoryIds);
    }

    /**
     * 获取最大排序值
     * @return 最大排序值
     */
    public Integer getMaxSortOrder() {
        return this.baseMapper.getMaxSortOrder();
    }

    /**
     * 统计启用的分类数量
     * @return 数量
     */
    public long countEnabled() {
        return this.baseMapper.countEnabled();
    }

    /**
     * 统计禁用的分类数量
     * @return 数量
     */
    public long countDisabled() {
        return this.baseMapper.countDisabled();
    }

    /**
     * 统计总数
     * @return 总数
     */
    public long countTotal() {
        return this.count();
    }

    /**
     * 批量更新排序
     * @param sortUpdates Map<分类ID, 新排序值>
     * @return 更新成功的数量
     */
    public int batchUpdateSortOrder(List<ShopCategory> categories) {
        int count = 0;
        for (ShopCategory category : categories) {
            if (updateSortOrder(category.getCategory_id(), category.getSort_order())) {
                count++;
            }
        }
        return count;
    }
}
