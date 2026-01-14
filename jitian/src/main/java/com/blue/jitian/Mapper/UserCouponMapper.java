package com.blue.jitian.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blue.jitian.Entity.UserCoupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


@Mapper
public interface UserCouponMapper extends BaseMapper<UserCoupon> {

    /**
     * 查询用户的可用优惠券列表
     * @param userId 用户ID
     * @return 可用优惠券列表
     */
    @Select("SELECT uc.*, c.coupon_name, c.coupon_type, c.min_amount, c.discount_amount, c.discount_rate, c.max_discount " +
            "FROM user_coupons uc " +
            "LEFT JOIN coupons c ON uc.coupon_id = c.coupon_id " +
            "WHERE uc.user_id = #{userId} AND uc.status = 0 AND uc.expire_time > NOW() " +
            "ORDER BY uc.expire_time ASC")
    List<Map<String, Object>> findAvailableCouponsByUserId(@Param("userId") Long userId);

    /**
     * 统计用户各状态优惠券数量
     * @param userId 用户ID
     * @return 统计结果
     */
    @Select("SELECT status, COUNT(*) as count FROM user_coupons WHERE user_id = #{userId} GROUP BY status")
    List<Map<String, Object>> countByStatusForUser(@Param("userId") Long userId);

    /**
     * 查询即将过期的优惠券（未来N天内过期）
     * @param userId 用户ID
     * @param days 天数
     * @return 即将过期的优惠券列表
     */
    @Select("SELECT uc.*, c.coupon_name FROM user_coupons uc " +
            "LEFT JOIN coupons c ON uc.coupon_id = c.coupon_id " +
            "WHERE uc.user_id = #{userId} AND uc.status = 0 " +
            "AND uc.expire_time > NOW() AND uc.expire_time < NOW() + INTERVAL '#{days} days' " +
            "ORDER BY uc.expire_time ASC")
    List<Map<String, Object>> findExpiringCoupons(@Param("userId") Long userId, @Param("days") int days);

    /**
     * 统计每个优惠券的使用情况
     * @param couponId 优惠券ID
     * @return 统计结果
     */
    @Select("SELECT status, COUNT(*) as count FROM user_coupons WHERE coupon_id = #{couponId} GROUP BY status")
    List<Map<String, Object>> countByCouponId(@Param("couponId") Long couponId);
}
