package com.blue.jitian.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blue.jitian.Entity.ShopCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


@Mapper
public interface ShopCategoryMapper extends BaseMapper<ShopCategory> {

    /**
     * 查询所有启用的分类（按排序）
     * @return 分类列表
     */
    @Select("SELECT * FROM shop_categories WHERE status = 1 ORDER BY sort_order ASC, category_id ASC")
    List<ShopCategory> findAllEnabled();

    /**
     * 根据状态查询分类
     * @param status 状态
     * @return 分类列表
     */
    @Select("SELECT * FROM shop_categories WHERE status = #{status} ORDER BY sort_order ASC, category_id ASC")
    List<ShopCategory> findByStatus(@Param("status") Integer status);

    /**
     * 查询所有分类（按排序）
     * @return 分类列表
     */
    @Select("SELECT * FROM shop_categories ORDER BY sort_order ASC, category_id ASC")
    List<ShopCategory> findAllOrdered();

    /**
     * 根据分类名称查询
     * @param categoryName 分类名称
     * @return 分类对象
     */
    @Select("SELECT * FROM shop_categories WHERE category_name = #{categoryName}")
    ShopCategory findByName(@Param("categoryName") String categoryName);

    /**
     * 更新分类状态
     * @param categoryId 分类ID
     * @param status 状态
     * @return 影响行数
     */
    @Update("UPDATE shop_categories SET status = #{status} WHERE category_id = #{categoryId}")
    int updateStatus(@Param("categoryId") Integer categoryId, @Param("status") Integer status);

    /**
     * 更新排序
     * @param categoryId 分类ID
     * @param sortOrder 排序值
     * @return 影响行数
     */
    @Update("UPDATE shop_categories SET sort_order = #{sortOrder} WHERE category_id = #{categoryId}")
    int updateSortOrder(@Param("categoryId") Integer categoryId, @Param("sortOrder") Integer sortOrder);

    /**
     * 检查分类名称是否存在
     * @param categoryName 分类名称
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM shop_categories WHERE category_name = #{categoryName}")
    long countByName(@Param("categoryName") String categoryName);

    /**
     * 检查分类名称是否存在（排除指定ID）
     * @param categoryName 分类名称
     * @param categoryId 要排除的分类ID
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM shop_categories WHERE category_name = #{categoryName} AND category_id != #{categoryId}")
    long countByNameExcludeId(@Param("categoryName") String categoryName, @Param("categoryId") Integer categoryId);

    /**
     * 获取最大排序值
     * @return 最大排序值
     */
    @Select("SELECT COALESCE(MAX(sort_order), 0) FROM shop_categories")
    Integer getMaxSortOrder();

    /**
     * 统计启用的分类数量
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM shop_categories WHERE status = 1")
    long countEnabled();

    /**
     * 统计禁用的分类数量
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM shop_categories WHERE status = 0")
    long countDisabled();
}
