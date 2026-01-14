package com.blue.jitian.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.jitian.Entity.Coupon;
import com.blue.jitian.Entity.UserCoupon;
import com.blue.jitian.Mapper.UserCouponMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Service
public class UserCouponService extends ServiceImpl<UserCouponMapper, UserCoupon> {

    @Autowired
    private CouponService couponService;

    /**
     * 用户领取优惠券
     * @param userId 用户ID
     * @param couponId 优惠券ID
     * @return 领取成功返回UserCoupon对象，否则返回null
     */
    @Transactional
    public UserCoupon receiveCoupon(Long userId, Long couponId) {
        // 查询优惠券信息
        Coupon coupon = couponService.getById(couponId);
        if (coupon == null || coupon.getStatus() != 1) {
            return null; // 优惠券不存在或已结束
        }

        // 检查是否还有剩余数量
        if (coupon.getReceived_count() >= coupon.getTotal_count()) {
            return null; // 已领完
        }

        // 计算过期时间
        LocalDateTime expireTime;
        if (coupon.getValid_type() == 1) {
            // 固定天数
            expireTime = LocalDateTime.now().plusDays(coupon.getValid_days());
        } else {
            // 固定日期
            expireTime = coupon.getEnd_time();
        }

        // 创建用户优惠券记录
        UserCoupon userCoupon = UserCoupon.builder()
                .userId(userId)
                .couponId(couponId)
                .status(0)
                .expireTime(expireTime)
                .build();

        if (this.save(userCoupon)) {
            // 更新优惠券已领取数量
            couponService.lambdaUpdate()
                    .eq(Coupon::getCoupon_id, couponId)
                    .setSql("received_count = received_count + 1")
                    .update();
            return userCoupon;
        }
        return null;
    }

    /**
     * 使用优惠券
     * @param userCouponId 用户优惠券ID
     * @param orderId 订单ID
     * @return 使用是否成功
     */
    @Transactional
    public boolean useCoupon(Long userCouponId, Long orderId) {
        UserCoupon userCoupon = this.getById(userCouponId);
        if (userCoupon == null || userCoupon.getStatus() != 0) {
            return false; // 优惠券不存在或已使用
        }

        // 检查是否过期
        if (userCoupon.getExpireTime().isBefore(LocalDateTime.now())) {
            // 自动更新为过期状态
            this.updateStatus(userCouponId, 2);
            return false;
        }

        // 更新优惠券状态为已使用
        LambdaUpdateWrapper<UserCoupon> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserCoupon::getUserCouponId, userCouponId)
                .set(UserCoupon::getStatus, 1)
                .set(UserCoupon::getOrderId, orderId)
                .set(UserCoupon::getUseTime, LocalDateTime.now());

        if (this.update(wrapper)) {
            // 更新优惠券已使用数量
            couponService.lambdaUpdate()
                    .eq(Coupon::getCoupon_id, userCoupon.getCouponId())
                    .setSql("used_count = used_count + 1")
                    .update();
            return true;
        }
        return false;
    }

    /**
     * 查询用户的优惠券列表
     * @param userId 用户ID
     * @param status 状态（可选）
     * @return 优惠券列表
     */
    public List<UserCoupon> getUserCoupons(Long userId, Integer status) {
        LambdaQueryWrapper<UserCoupon> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserCoupon::getUserId, userId);
        if (status != null) {
            wrapper.eq(UserCoupon::getStatus, status);
        }
        wrapper.orderByDesc(UserCoupon::getReceiveTime);
        return this.list(wrapper);
    }

    /**
     * 查询用户的可用优惠券列表（包含优惠券详情）
     * @param userId 用户ID
     * @return 可用优惠券列表
     */
    public List<Map<String, Object>> getAvailableCoupons(Long userId) {
        return this.baseMapper.findAvailableCouponsByUserId(userId);
    }

    /**
     * 分页查询用户优惠券
     * @param current 当前页
     * @param size 每页大小
     * @param userId 用户ID
     * @param status 状态（可选）
     * @param couponId 优惠券ID（可选）
     * @return 分页结果
     */
    public Page<UserCoupon> getUserCouponPage(long current, long size, Long userId, 
                                               Integer status, Long couponId) {
        Page<UserCoupon> page = new Page<>(current, size);
        LambdaQueryWrapper<UserCoupon> wrapper = new LambdaQueryWrapper<>();
        
        if (userId != null) {
            wrapper.eq(UserCoupon::getUserId, userId);
        }
        if (status != null) {
            wrapper.eq(UserCoupon::getStatus, status);
        }
        if (couponId != null) {
            wrapper.eq(UserCoupon::getCouponId, couponId);
        }
        
        wrapper.orderByDesc(UserCoupon::getReceiveTime);
        return this.page(page, wrapper);
    }

    /**
     * 更新优惠券状态
     * @param userCouponId 用户优惠券ID
     * @param status 状态
     * @return 更新是否成功
     */
    public boolean updateStatus(Long userCouponId, int status) {
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUserCouponId(userCouponId);
        userCoupon.setStatus(status);
        return this.updateById(userCoupon);
    }

    /**
     * 批量更新过期优惠券状态
     * @return 更新数量
     */
    @Transactional
    public int updateExpiredCoupons() {
        LambdaUpdateWrapper<UserCoupon> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserCoupon::getStatus, 0)
                .lt(UserCoupon::getExpireTime, LocalDateTime.now())
                .set(UserCoupon::getStatus, 2);
        return this.baseMapper.update(null, wrapper);
    }

    /**
     * 统计用户各状态优惠券数量
     * @param userId 用户ID
     * @return 统计结果
     */
    public List<Map<String, Object>> countByStatus(Long userId) {
        return this.baseMapper.countByStatusForUser(userId);
    }

    /**
     * 查询即将过期的优惠券
     * @param userId 用户ID
     * @param days 天数
     * @return 即将过期的优惠券列表
     */
    public List<Map<String, Object>> getExpiringCoupons(Long userId, int days) {
        return this.baseMapper.findExpiringCoupons(userId, days);
    }

    /**
     * 统计指定优惠券的使用情况
     * @param couponId 优惠券ID
     * @return 统计结果
     */
    public List<Map<String, Object>> getCouponUsageStats(Long couponId) {
        return this.baseMapper.countByCouponId(couponId);
    }

    /**
     * 检查用户是否已领取指定优惠券
     * @param userId 用户ID
     * @param couponId 优惠券ID
     * @return 已领取返回true，否则返回false
     */
    public boolean hasReceivedCoupon(Long userId, Long couponId) {
        return this.lambdaQuery()
                .eq(UserCoupon::getUserId, userId)
                .eq(UserCoupon::getCouponId, couponId)
                .count() > 0;
    }

    /**
     * 根据订单ID查询使用的优惠券
     * @param orderId 订单ID
     * @return UserCoupon对象
     */
    public UserCoupon getByOrderId(Long orderId) {
        return this.lambdaQuery()
                .eq(UserCoupon::getOrderId, orderId)
                .one();
    }
}
