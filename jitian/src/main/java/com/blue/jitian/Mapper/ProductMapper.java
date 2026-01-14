package com.blue.jitian.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blue.jitian.Entity.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    /**
     * 根据商家ID查询商品
     * @param shopId 商家ID
     * @return 商品列表
     */
    @Select("SELECT * FROM products WHERE shop_id = #{shopId} AND status = 1 ORDER BY sort_order ASC, product_id DESC")
    List<Product> findByShopId(@Param("shopId") Long shopId);

    /**
     * 根据分类ID查询商品
     * @param categoryId 分类ID
     * @return 商品列表
     */
    @Select("SELECT * FROM products WHERE category_id = #{categoryId} AND status = 1 ORDER BY sort_order ASC, product_id DESC")
    List<Product> findByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 根据商家ID和分类ID查询商品
     * @param shopId 商家ID
     * @param categoryId 分类ID
     * @return 商品列表
     */
    @Select("SELECT * FROM products WHERE shop_id = #{shopId} AND category_id = #{categoryId} AND status = 1 ORDER BY sort_order ASC, product_id DESC")
    List<Product> findByShopIdAndCategoryId(@Param("shopId") Long shopId, @Param("categoryId") Long categoryId);

    /**
     * 根据状态查询商品
     * @param status 状态
     * @return 商品列表
     */
    @Select("SELECT * FROM products WHERE status = #{status} ORDER BY created_at DESC")
    List<Product> findByStatus(@Param("status") Integer status);

    /**
     * 根据商家ID和状态查询商品
     * @param shopId 商家ID
     * @param status 状态
     * @return 商品列表
     */
    @Select("SELECT * FROM products WHERE shop_id = #{shopId} AND status = #{status} ORDER BY sort_order ASC, product_id DESC")
    List<Product> findByShopIdAndStatus(@Param("shopId") Long shopId, @Param("status") Integer status);

    /**
     * 根据商品名称模糊查询
     * @param productName 商品名称
     * @return 商品列表
     */
    @Select("SELECT * FROM products WHERE product_name LIKE CONCAT('%', #{productName}, '%') AND status = 1 ORDER BY sales_count DESC")
    List<Product> findByNameLike(@Param("productName") String productName);

    /**
     * 根据商家ID和名称模糊查询
     * @param shopId 商家ID
     * @param productName 商品名称
     * @return 商品列表
     */
    @Select("SELECT * FROM products WHERE shop_id = #{shopId} AND product_name LIKE CONCAT('%', #{productName}, '%') ORDER BY sales_count DESC")
    List<Product> findByShopIdAndNameLike(@Param("shopId") Long shopId, @Param("productName") String productName);

    /**
     * 更新商品状态
     * @param productId 商品ID
     * @param status 状态
     * @return 影响行数
     */
    @Update("UPDATE products SET status = #{status}, updated_at = NOW() WHERE product_id = #{productId}")
    int updateStatus(@Param("productId") Long productId, @Param("status") Integer status);

    /**
     * 增加销量
     * @param productId 商品ID
     * @param increment 增加的数量
     * @return 影响行数
     */
    @Update("UPDATE products SET sales_count = sales_count + #{increment}, updated_at = NOW() WHERE product_id = #{productId}")
    int incrementSalesCount(@Param("productId") Long productId, @Param("increment") Integer increment);

    /**
     * 减少库存
     * @param productId 商品ID
     * @param decrement 减少的数量
     * @return 影响行数
     */
    @Update("UPDATE products SET stock = stock - #{decrement}, updated_at = NOW() WHERE product_id = #{productId} AND (stock >= #{decrement} OR stock = -1)")
    int decrementStock(@Param("productId") Long productId, @Param("decrement") Integer decrement);

    /**
     * 增加库存
     * @param productId 商品ID
     * @param increment 增加的数量
     * @return 影响行数
     */
    @Update("UPDATE products SET stock = stock + #{increment}, updated_at = NOW() WHERE product_id = #{productId}")
    int incrementStock(@Param("productId") Long productId, @Param("increment") Integer increment);

    /**
     * 更新排序
     * @param productId 商品ID
     * @param sortOrder 排序值
     * @return 影响行数
     */
    @Update("UPDATE products SET sort_order = #{sortOrder}, updated_at = NOW() WHERE product_id = #{productId}")
    int updateSortOrder(@Param("productId") Long productId, @Param("sortOrder") Integer sortOrder);

    /**
     * 统计商家的商品数量
     * @param shopId 商家ID
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM products WHERE shop_id = #{shopId}")
    long countByShopId(@Param("shopId") Long shopId);

    /**
     * 统计分类下的商品数量
     * @param categoryId 分类ID
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM products WHERE category_id = #{categoryId}")
    long countByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 统计上架商品数量
     * @param shopId 商家ID
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM products WHERE shop_id = #{shopId} AND status = 1")
    long countOnSaleByShopId(@Param("shopId") Long shopId);

    /**
     * 查询商家的热销商品
     * @param shopId 商家ID
     * @param limit 限制数量
     * @return 商品列表
     */
    @Select("SELECT * FROM products WHERE shop_id = #{shopId} AND status = 1 ORDER BY sales_count DESC LIMIT #{limit}")
    List<Product> findHotProductsByShopId(@Param("shopId") Long shopId, @Param("limit") Integer limit);

    /**
     * 查询商家的新品
     * @param shopId 商家ID
     * @param limit 限制数量
     * @return 商品列表
     */
    @Select("SELECT * FROM products WHERE shop_id = #{shopId} AND status = 1 ORDER BY created_at DESC LIMIT #{limit}")
    List<Product> findNewProductsByShopId(@Param("shopId") Long shopId, @Param("limit") Integer limit);

    /**
     * 删除商家的所有商品
     * @param shopId 商家ID
     * @return 影响行数
     */
    @Delete("DELETE FROM products WHERE shop_id = #{shopId}")
    int deleteByShopId(@Param("shopId") Long shopId);

    /**
     * 删除分类下的所有商品
     * @param categoryId 分类ID
     * @return 影响行数
     */
    @Delete("DELETE FROM products WHERE category_id = #{categoryId}")
    int deleteByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 获取商家商品的最大排序值
     * @param shopId 商家ID
     * @return 最大排序值
     */
    @Select("SELECT COALESCE(MAX(sort_order), 0) FROM products WHERE shop_id = #{shopId}")
    Integer getMaxSortOrder(@Param("shopId") Long shopId);
}
