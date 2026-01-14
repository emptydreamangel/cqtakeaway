package com.blue.jitian.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blue.jitian.Entity.Shop;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


@Mapper
public interface ShopMapper extends BaseMapper<Shop> {

    /**
     * 根据分类ID查询商家
     * @param categoryId 分类ID
     * @return 商家列表
     */
    @Select("SELECT * FROM shops WHERE category_id = #{categoryId} AND status = 1 ORDER BY rating DESC, sales_count DESC")
    List<Shop> findByCategoryId(@Param("categoryId") Integer categoryId);

    /**
     * 根据状态查询商家
     * @param status 状态
     * @return 商家列表
     */
    @Select("SELECT * FROM shops WHERE status = #{status} ORDER BY created_at DESC")
    List<Shop> findByStatus(@Param("status") Integer status);

    /**
     * 查询营业中的商家
     * @return 商家列表
     */
    @Select("SELECT * FROM shops WHERE status = 1 ORDER BY rating DESC, sales_count DESC")
    List<Shop> findBusinessShops();

    /**
     * 根据名称模糊查询商家
     * @param shopName 商家名称
     * @return 商家列表
     */
    @Select("SELECT * FROM shops WHERE shop_name LIKE CONCAT('%', #{shopName}, '%') ORDER BY rating DESC")
    List<Shop> findByNameLike(@Param("shopName") String shopName);

    /**
     * 根据地区查询商家
     * @param province 省份
     * @param city 城市
     * @param district 区县
     * @return 商家列表
     */
    @Select("SELECT * FROM shops WHERE province = #{province} AND city = #{city} AND district = #{district} " +
            "AND status = 1 ORDER BY rating DESC, sales_count DESC")
    List<Shop> findByRegion(@Param("province") String province,
                            @Param("city") String city,
                            @Param("district") String district);

    /**
     * 更新商家状态
     * @param shopId 商家ID
     * @param status 状态
     * @return 影响行数
     */
    @Update("UPDATE shops SET status = #{status}, updated_at = NOW() WHERE shop_id = #{shopId}")
    int updateStatus(@Param("shopId") Long shopId, @Param("status") Integer status);

    /**
     * 更新商家认证状态
     * @param shopId 商家ID
     * @param isAuth 认证状态
     * @return 影响行数
     */
    @Update("UPDATE shops SET is_auth = #{isAuth}, updated_at = NOW() WHERE shop_id = #{shopId}")
    int updateAuthStatus(@Param("shopId") Long shopId, @Param("isAuth") Integer isAuth);

    /**
     * 增加销量
     * @param shopId 商家ID
     * @param increment 增加的数量
     * @return 影响行数
     */
    @Update("UPDATE shops SET sales_count = sales_count + #{increment}, updated_at = NOW() WHERE shop_id = #{shopId}")
    int incrementSalesCount(@Param("shopId") Long shopId, @Param("increment") Integer increment);

    /**
     * 统计分类下的商家数量
     * @param categoryId 分类ID
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM shops WHERE category_id = #{categoryId}")
    long countByCategoryId(@Param("categoryId") Integer categoryId);

    /**
     * 统计营业中的商家数量
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM shops WHERE status = 1")
    long countBusinessShops();

    /**
     * 统计已认证的商家数量
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM shops WHERE is_auth = 1")
    long countAuthShops();

    /**
     * 查询评分最高的商家
     * @param limit 限制数量
     * @return 商家列表
     */
    @Select("SELECT * FROM shops WHERE status = 1 ORDER BY rating DESC, sales_count DESC LIMIT #{limit}")
    List<Shop> findTopRatedShops(@Param("limit") Integer limit);

    /**
     * 查询销量最高的商家
     * @param limit 限制数量
     * @return 商家列表
     */
    @Select("SELECT * FROM shops WHERE status = 1 ORDER BY sales_count DESC, rating DESC LIMIT #{limit}")
    List<Shop> findTopSalesShops(@Param("limit") Integer limit);

    /**
     * 根据位置范围查询商家（简单矩形范围）
     * @param minLongitude 最小经度
     * @param maxLongitude 最大经度
     * @param minLatitude 最小纬度
     * @param maxLatitude 最大纬度
     * @return 商家列表
     */
    @Select("SELECT * FROM shops WHERE longitude BETWEEN #{minLongitude} AND #{maxLongitude} " +
            "AND latitude BETWEEN #{minLatitude} AND #{maxLatitude} AND status = 1 " +
            "ORDER BY rating DESC")
    List<Shop> findByLocationRange(@Param("minLongitude") java.math.BigDecimal minLongitude,
                                    @Param("maxLongitude") java.math.BigDecimal maxLongitude,
                                    @Param("minLatitude") java.math.BigDecimal minLatitude,
                                    @Param("maxLatitude") java.math.BigDecimal maxLatitude);
}
