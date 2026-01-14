package com.blue.jitian.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blue.jitian.Entity.Coupon;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CouponMapper extends BaseMapper<Coupon> {
    
    /**
     * 根据商家ID查询优惠券
     */
    @Select("SELECT * FROM coupons WHERE shop_id = #{shopId} AND status = 1 ORDER BY created_at DESC")
    List<Coupon> findByShopId(@Param("shopId") Long shopId);
    
    /**
     * 查询平台优惠券
     */
    @Select("SELECT * FROM coupons WHERE shop_id IS NULL AND status = 1 ORDER BY created_at DESC")
    List<Coupon> findPlatformCoupons();
    
    /**
     * 根据类型查询优惠券
     */
    @Select("SELECT * FROM coupons WHERE coupon_type = #{couponType} AND status = 1 ORDER BY created_at DESC")
    List<Coupon> findByCouponType(@Param("couponType") Integer couponType);
    
    /**
     * 根据状态查询优惠券
     */
    @Select("SELECT * FROM coupons WHERE status = #{status} ORDER BY created_at DESC")
    List<Coupon> findByStatus(@Param("status") Integer status);
    
    /**
     * 查询可领取的优惠券（还有剩余数量）
     */
    @Select("SELECT * FROM coupons WHERE status = 1 AND received_count < total_count ORDER BY created_at DESC")
    List<Coupon> findAvailableCoupons();
    
    /**
     * 查询商家可领取的优惠券
     */
    @Select("SELECT * FROM coupons WHERE shop_id = #{shopId} AND status = 1 AND received_count < total_count ORDER BY created_at DESC")
    List<Coupon> findAvailableCouponsByShopId(@Param("shopId") Long shopId);
    
    /**
     * 增加已领取数量
     */
    @Update("UPDATE coupons SET received_count = received_count + 1 WHERE coupon_id = #{couponId} AND received_count < total_count")
    int incrementReceivedCount(@Param("couponId") Long couponId);
    
    /**
     * 增加已使用数量
     */
    @Update("UPDATE coupons SET used_count = used_count + 1 WHERE coupon_id = #{couponId}")
    int incrementUsedCount(@Param("couponId") Long couponId);
    
    /**
     * 更新优惠券状态
     */
    @Update("UPDATE coupons SET status = #{status} WHERE coupon_id = #{couponId}")
    int updateStatus(@Param("couponId") Long couponId, @Param("status") Integer status);
    
    /**
     * 统计商家的优惠券数量
     */
    @Select("SELECT COUNT(*) FROM coupons WHERE shop_id = #{shopId}")
    Integer countByShopId(@Param("shopId") Long shopId);
    
    /**
     * 统计平台优惠券数量
     */
    @Select("SELECT COUNT(*) FROM coupons WHERE shop_id IS NULL")
    Integer countPlatformCoupons();
    
    /**
     * 查询即将过期的优惠券（固定日期类型）
     */
    @Select("SELECT * FROM coupons WHERE valid_type = 2 AND end_time BETWEEN CURRENT_TIMESTAMP AND #{beforeTime} AND status = 1")
    List<Coupon> findExpiringCoupons(@Param("beforeTime") LocalDateTime beforeTime);
    
    /**
     * 查询已过期的优惠券
     */
    @Select("SELECT * FROM coupons WHERE valid_type = 2 AND end_time < CURRENT_TIMESTAMP AND status = 1")
    List<Coupon> findExpiredCoupons();
    
    /**
     * 批量更新已过期优惠券状态
     */
    @Update("UPDATE coupons SET status = 0 WHERE valid_type = 2 AND end_time < CURRENT_TIMESTAMP AND status = 1")
    int batchUpdateExpiredCouponsStatus();
    
    /**
     * 根据优惠券ID列表批量查询
     */
    @Select("<script>" +
            "SELECT * FROM coupons WHERE coupon_id IN " +
            "<foreach collection='couponIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    List<Coupon> findByCouponIds(@Param("couponIds") List<Long> couponIds);
    
    /**
     * 统计优惠券使用率
     */
    @Select("SELECT coupon_id, coupon_name, total_count, received_count, used_count, " +
            "(used_count * 100.0 / NULLIF(received_count, 0)) as usage_rate " +
            "FROM coupons WHERE coupon_id = #{couponId}")
    java.util.Map<String, Object> getCouponUsageStats(@Param("couponId") Long couponId);
}
