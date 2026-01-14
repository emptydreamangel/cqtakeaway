package com.blue.jitian.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blue.jitian.Entity.UserFavorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Delete;

import java.util.List;


@Mapper
public interface UserFavoriteMapper extends BaseMapper<UserFavorite> {

    /**
     * 根据用户ID查询所有收藏
     * @param userId 用户ID
     * @return 收藏列表
     */
    @Select("SELECT * FROM user_favorites WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<UserFavorite> findByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID和类型查询收藏
     * @param userId 用户ID
     * @param type 类型（1:店铺，2:商品）
     * @return 收藏列表
     */
    @Select("SELECT * FROM user_favorites WHERE user_id = #{userId} AND type = #{type} ORDER BY created_at DESC")
    List<UserFavorite> findByUserIdAndType(@Param("userId") Long userId, @Param("type") Integer type);

    /**
     * 查询用户收藏的店铺列表
     * @param userId 用户ID
     * @return 收藏列表
     */
    @Select("SELECT * FROM user_favorites WHERE user_id = #{userId} AND type = 1 ORDER BY created_at DESC")
    List<UserFavorite> findShopsByUserId(@Param("userId") Long userId);

    /**
     * 查询用户收藏的商品列表
     * @param userId 用户ID
     * @return 收藏列表
     */
    @Select("SELECT * FROM user_favorites WHERE user_id = #{userId} AND type = 2 ORDER BY created_at DESC")
    List<UserFavorite> findProductsByUserId(@Param("userId") Long userId);

    /**
     * 检查用户是否收藏了店铺
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @return 收藏记录
     */
    @Select("SELECT * FROM user_favorites WHERE user_id = #{userId} AND shop_id = #{shopId} AND type = 1")
    UserFavorite findShopFavorite(@Param("userId") Long userId, @Param("shopId") Long shopId);

    /**
     * 检查用户是否收藏了商品
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @param productId 商品ID
     * @return 收藏记录
     */
    @Select("SELECT * FROM user_favorites WHERE user_id = #{userId} AND shop_id = #{shopId} " +
            "AND product_id = #{productId} AND type = 2")
    UserFavorite findProductFavorite(@Param("userId") Long userId, 
                                      @Param("shopId") Long shopId, 
                                      @Param("productId") Long productId);

    /**
     * 删除店铺收藏
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @return 影响行数
     */
    @Delete("DELETE FROM user_favorites WHERE user_id = #{userId} AND shop_id = #{shopId} AND type = 1")
    int deleteShopFavorite(@Param("userId") Long userId, @Param("shopId") Long shopId);

    /**
     * 删除商品收藏
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @param productId 商品ID
     * @return 影响行数
     */
    @Delete("DELETE FROM user_favorites WHERE user_id = #{userId} AND shop_id = #{shopId} " +
            "AND product_id = #{productId} AND type = 2")
    int deleteProductFavorite(@Param("userId") Long userId, 
                              @Param("shopId") Long shopId, 
                              @Param("productId") Long productId);

    /**
     * 统计用户收藏数量
     * @param userId 用户ID
     * @return 收藏数量
     */
    @Select("SELECT COUNT(*) FROM user_favorites WHERE user_id = #{userId}")
    long countByUserId(@Param("userId") Long userId);

    /**
     * 统计用户店铺收藏数量
     * @param userId 用户ID
     * @return 收藏数量
     */
    @Select("SELECT COUNT(*) FROM user_favorites WHERE user_id = #{userId} AND type = 1")
    long countShopsByUserId(@Param("userId") Long userId);

    /**
     * 统计用户商品收藏数量
     * @param userId 用户ID
     * @return 收藏数量
     */
    @Select("SELECT COUNT(*) FROM user_favorites WHERE user_id = #{userId} AND type = 2")
    long countProductsByUserId(@Param("userId") Long userId);

    /**
     * 统计店铺被收藏的次数
     * @param shopId 店铺ID
     * @return 收藏次数
     */
    @Select("SELECT COUNT(*) FROM user_favorites WHERE shop_id = #{shopId} AND type = 1")
    long countShopFavorites(@Param("shopId") Long shopId);

    /**
     * 统计商品被收藏的次数
     * @param shopId 店铺ID
     * @param productId 商品ID
     * @return 收藏次数
     */
    @Select("SELECT COUNT(*) FROM user_favorites WHERE shop_id = #{shopId} " +
            "AND product_id = #{productId} AND type = 2")
    long countProductFavorites(@Param("shopId") Long shopId, @Param("productId") Long productId);

    /**
     * 根据店铺ID查询所有收藏了该店铺的用户
     * @param shopId 店铺ID
     * @return 收藏列表
     */
    @Select("SELECT * FROM user_favorites WHERE shop_id = #{shopId} AND type = 1 ORDER BY created_at DESC")
    List<UserFavorite> findUsersByShopId(@Param("shopId") Long shopId);

    /**
     * 删除用户所有收藏
     * @param userId 用户ID
     * @return 影响行数
     */
    @Delete("DELETE FROM user_favorites WHERE user_id = #{userId}")
    int deleteAllByUserId(@Param("userId") Long userId);
}
