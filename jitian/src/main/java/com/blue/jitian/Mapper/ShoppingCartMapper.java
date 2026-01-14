package com.blue.jitian.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blue.jitian.Entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

    /**
     * 根据用户ID查询购物车列表
     * @param userId 用户ID
     * @return 购物车列表
     */
    @Select("SELECT * FROM shopping_carts WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<ShoppingCart> findByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID和商家ID查询购物车列表
     * @param userId 用户ID
     * @param shopId 商家ID
     * @return 购物车列表
     */
    @Select("SELECT * FROM shopping_carts WHERE user_id = #{userId} AND shop_id = #{shopId} ORDER BY created_at DESC")
    List<ShoppingCart> findByUserIdAndShopId(@Param("userId") Long userId, @Param("shopId") Long shopId);

    /**
     * 查询具体的购物车项（用户+商家+商品+规格）
     * @param userId 用户ID
     * @param shopId 商家ID
     * @param productId 商品ID
     * @param specId 规格ID（可为null）
     * @return 购物车项
     */
    @Select("<script>" +
            "SELECT * FROM shopping_carts WHERE user_id = #{userId} AND shop_id = #{shopId} AND product_id = #{productId} " +
            "<if test='specId != null'> AND spec_id = #{specId} </if>" +
            "<if test='specId == null'> AND spec_id IS NULL </if>" +
            "</script>")
    ShoppingCart findByUserProductSpec(@Param("userId") Long userId, 
                                       @Param("shopId") Long shopId,
                                       @Param("productId") Long productId, 
                                       @Param("specId") Long specId);

    /**
     * 更新购物车数量
     * @param cartId 购物车ID
     * @param quantity 数量
     * @return 影响行数
     */
    @Update("UPDATE shopping_carts SET quantity = #{quantity}, updated_at = NOW() WHERE cart_id = #{cartId}")
    int updateQuantity(@Param("cartId") Long cartId, @Param("quantity") Integer quantity);

    /**
     * 增加购物车数量
     * @param cartId 购物车ID
     * @param increment 增加的数量
     * @return 影响行数
     */
    @Update("UPDATE shopping_carts SET quantity = quantity + #{increment}, updated_at = NOW() WHERE cart_id = #{cartId}")
    int incrementQuantity(@Param("cartId") Long cartId, @Param("increment") Integer increment);

    /**
     * 减少购物车数量
     * @param cartId 购物车ID
     * @param decrement 减少的数量
     * @return 影响行数
     */
    @Update("UPDATE shopping_carts SET quantity = quantity - #{decrement}, updated_at = NOW() WHERE cart_id = #{cartId} AND quantity >= #{decrement}")
    int decrementQuantity(@Param("cartId") Long cartId, @Param("decrement") Integer decrement);

    /**
     * 删除用户的所有购物车项
     * @param userId 用户ID
     * @return 影响行数
     */
    @Delete("DELETE FROM shopping_carts WHERE user_id = #{userId}")
    int deleteByUserId(@Param("userId") Long userId);

    /**
     * 删除用户在指定商家的购物车项
     * @param userId 用户ID
     * @param shopId 商家ID
     * @return 影响行数
     */
    @Delete("DELETE FROM shopping_carts WHERE user_id = #{userId} AND shop_id = #{shopId}")
    int deleteByUserIdAndShopId(@Param("userId") Long userId, @Param("shopId") Long shopId);

    /**
     * 批量删除购物车项
     * @param cartIds 购物车ID列表
     * @return 影响行数
     */
    @Delete("<script>" +
            "DELETE FROM shopping_carts WHERE cart_id IN " +
            "<foreach collection='cartIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    int batchDelete(@Param("cartIds") List<Long> cartIds);

    /**
     * 统计用户购物车项数量
     * @param userId 用户ID
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM shopping_carts WHERE user_id = #{userId}")
    long countByUserId(@Param("userId") Long userId);

    /**
     * 统计用户在指定商家的购物车项数量
     * @param userId 用户ID
     * @param shopId 商家ID
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM shopping_carts WHERE user_id = #{userId} AND shop_id = #{shopId}")
    long countByUserIdAndShopId(@Param("userId") Long userId, @Param("shopId") Long shopId);

    /**
     * 获取用户购物车中的商家列表
     * @param userId 用户ID
     * @return 商家ID列表
     */
    @Select("SELECT DISTINCT shop_id FROM shopping_carts WHERE user_id = #{userId}")
    List<Long> findShopIdsByUserId(@Param("userId") Long userId);

    /**
     * 检查购物车项是否存在
     * @param userId 用户ID
     * @param productId 商品ID
     * @param specId 规格ID
     * @return 数量
     */
    @Select("<script>" +
            "SELECT COUNT(*) FROM shopping_carts WHERE user_id = #{userId} AND product_id = #{productId} " +
            "<if test='specId != null'> AND spec_id = #{specId} </if>" +
            "<if test='specId == null'> AND spec_id IS NULL </if>" +
            "</script>")
    long countByUserProductSpec(@Param("userId") Long userId, 
                                @Param("productId") Long productId, 
                                @Param("specId") Long specId);
}
