package com.blue.jitian.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blue.jitian.Entity.ShopImage;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface ShopImageMapper extends BaseMapper<ShopImage> {

    /**
     * 根据商家ID查询所有图片
     * @param shopId 商家ID
     * @return 图片列表
     */
    @Select("SELECT * FROM shop_images WHERE shop_id = #{shopId} ORDER BY sort_order ASC, image_id ASC")
    List<ShopImage> findByShopId(@Param("shopId") Long shopId);

    /**
     * 根据商家ID和图片类型查询
     * @param shopId 商家ID
     * @param imageType 图片类型
     * @return 图片列表
     */
    @Select("SELECT * FROM shop_images WHERE shop_id = #{shopId} AND image_type = #{imageType} ORDER BY sort_order ASC, image_id ASC")
    List<ShopImage> findByShopIdAndType(@Param("shopId") Long shopId, @Param("imageType") Integer imageType);

    /**
     * 查询商家的环境图
     * @param shopId 商家ID
     * @return 环境图列表
     */
    @Select("SELECT * FROM shop_images WHERE shop_id = #{shopId} AND image_type = 1 ORDER BY sort_order ASC, image_id ASC")
    List<ShopImage> findEnvironmentImages(@Param("shopId") Long shopId);

    /**
     * 查询商家的菜品图
     * @param shopId 商家ID
     * @return 菜品图列表
     */
    @Select("SELECT * FROM shop_images WHERE shop_id = #{shopId} AND image_type = 2 ORDER BY sort_order ASC, image_id ASC")
    List<ShopImage> findDishImages(@Param("shopId") Long shopId);

    /**
     * 统计商家图片数量
     * @param shopId 商家ID
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM shop_images WHERE shop_id = #{shopId}")
    long countByShopId(@Param("shopId") Long shopId);

    /**
     * 统计商家指定类型的图片数量
     * @param shopId 商家ID
     * @param imageType 图片类型
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM shop_images WHERE shop_id = #{shopId} AND image_type = #{imageType}")
    long countByShopIdAndType(@Param("shopId") Long shopId, @Param("imageType") Integer imageType);

    /**
     * 删除商家的所有图片
     * @param shopId 商家ID
     * @return 影响行数
     */
    @Delete("DELETE FROM shop_images WHERE shop_id = #{shopId}")
    int deleteByShopId(@Param("shopId") Long shopId);

    /**
     * 删除商家指定类型的所有图片
     * @param shopId 商家ID
     * @param imageType 图片类型
     * @return 影响行数
     */
    @Delete("DELETE FROM shop_images WHERE shop_id = #{shopId} AND image_type = #{imageType}")
    int deleteByShopIdAndType(@Param("shopId") Long shopId, @Param("imageType") Integer imageType);

    /**
     * 更新图片排序
     * @param imageId 图片ID
     * @param sortOrder 排序值
     * @return 影响行数
     */
    @Update("UPDATE shop_images SET sort_order = #{sortOrder} WHERE image_id = #{imageId}")
    int updateSortOrder(@Param("imageId") Long imageId, @Param("sortOrder") Integer sortOrder);

    /**
     * 获取商家图片的最大排序值
     * @param shopId 商家ID
     * @return 最大排序值
     */
    @Select("SELECT COALESCE(MAX(sort_order), 0) FROM shop_images WHERE shop_id = #{shopId}")
    Integer getMaxSortOrder(@Param("shopId") Long shopId);

    /**
     * 批量插入图片
     * @param images 图片列表
     * @return 影响行数
     */
    @Insert("<script>" +
            "INSERT INTO shop_images (shop_id, image_url, image_type, sort_order) VALUES " +
            "<foreach collection='images' item='image' separator=','>" +
            "(#{image.shop_id}, #{image.image_url}, #{image.image_type}, #{image.sort_order})" +
            "</foreach>" +
            "</script>")
    int batchInsert(@Param("images") List<ShopImage> images);
}
