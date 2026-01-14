package com.blue.jitian.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blue.jitian.Entity.ProductCategory;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {

    /**
     * 根据商家ID查询所有分类
     * @param shopId 商家ID
     * @return 分类列表
     */
    @Select("SELECT * FROM product_categories WHERE shop_id = #{shopId} ORDER BY sort_order ASC, category_id ASC")
    List<ProductCategory> findByShopId(@Param("shopId") Long shopId);

    /**
     * 根据商家ID和分类名称查询
     * @param shopId 商家ID
     * @param categoryName 分类名称
     * @return 分类对象
     */
    @Select("SELECT * FROM product_categories WHERE shop_id = #{shopId} AND category_name = #{categoryName}")
    ProductCategory findByShopIdAndName(@Param("shopId") Long shopId, @Param("categoryName") String categoryName);

    /**
     * 检查分类名称是否存在（同一商家下）
     * @param shopId 商家ID
     * @param categoryName 分类名称
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM product_categories WHERE shop_id = #{shopId} AND category_name = #{categoryName}")
    long countByShopIdAndName(@Param("shopId") Long shopId, @Param("categoryName") String categoryName);

    /**
     * 检查分类名称是否存在（排除指定ID）
     * @param shopId 商家ID
     * @param categoryName 分类名称
     * @param categoryId 要排除的分类ID
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM product_categories WHERE shop_id = #{shopId} AND category_name = #{categoryName} AND category_id != #{categoryId}")
    long countByShopIdAndNameExcludeId(@Param("shopId") Long shopId, @Param("categoryName") String categoryName, @Param("categoryId") Long categoryId);

    /**
     * 统计商家的分类数量
     * @param shopId 商家ID
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM product_categories WHERE shop_id = #{shopId}")
    long countByShopId(@Param("shopId") Long shopId);

    /**
     * 删除商家的所有分类
     * @param shopId 商家ID
     * @return 影响行数
     */
    @Delete("DELETE FROM product_categories WHERE shop_id = #{shopId}")
    int deleteByShopId(@Param("shopId") Long shopId);

    /**
     * 更新排序
     * @param categoryId 分类ID
     * @param sortOrder 排序值
     * @return 影响行数
     */
    @Update("UPDATE product_categories SET sort_order = #{sortOrder} WHERE category_id = #{categoryId}")
    int updateSortOrder(@Param("categoryId") Long categoryId, @Param("sortOrder") Integer sortOrder);

    /**
     * 获取商家分类的最大排序值
     * @param shopId 商家ID
     * @return 最大排序值
     */
    @Select("SELECT COALESCE(MAX(sort_order), 0) FROM product_categories WHERE shop_id = #{shopId}")
    Integer getMaxSortOrder(@Param("shopId") Long shopId);

    /**
     * 批量插入分类
     * @param categories 分类列表
     * @return 影响行数
     */
    @Insert("<script>" +
            "INSERT INTO product_categories (shop_id, category_name, sort_order) VALUES " +
            "<foreach collection='categories' item='category' separator=','>" +
            "(#{category.shop_id}, #{category.category_name}, #{category.sort_order})" +
            "</foreach>" +
            "</script>")
    int batchInsert(@Param("categories") List<ProductCategory> categories);
}
