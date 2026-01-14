package com.blue.jitian.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.jitian.Entity.Coupon;
import com.blue.jitian.Mapper.CouponMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CouponService extends ServiceImpl<CouponMapper, Coupon> {
    
    /**
     * 创建优惠券
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean createCoupon(Coupon coupon) {
        // 设置默认值
        if (coupon.getReceived_count() == null) {
            coupon.setReceived_count(0);
        }
        if (coupon.getUsed_count() == null) {
            coupon.setUsed_count(0);
        }
        if (coupon.getStatus() == null) {
            coupon.setStatus(1);
        }
        
        return this.save(coupon);
    }
    
    /**
     * 根据商家ID查询优惠券
     */
    public List<Coupon> getCouponsByShopId(Long shopId) {
        return this.baseMapper.findByShopId(shopId);
    }
    
    /**
     * 查询平台优惠券
     */
    public List<Coupon> getPlatformCoupons() {
        return this.baseMapper.findPlatformCoupons();
    }
    
    /**
     * 根据类型查询优惠券
     */
    public List<Coupon> getCouponsByCouponType(Integer couponType) {
        return this.baseMapper.findByCouponType(couponType);
    }
    
    /**
     * 查询可领取的优惠券
     */
    public List<Coupon> getAvailableCoupons() {
        return this.baseMapper.findAvailableCoupons();
    }
    
    /**
     * 查询商家可领取的优惠券
     */
    public List<Coupon> getAvailableCouponsByShopId(Long shopId) {
        return this.baseMapper.findAvailableCouponsByShopId(shopId);
    }
    
    /**
     * 领取优惠券
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean receiveCoupon(Long couponId) {
        Coupon coupon = this.getById(couponId);
        if (coupon == null) {
            log.warn("优惠券不存在: couponId={}", couponId);
            return false;
        }
        
        if (coupon.getStatus() != 1) {
            log.warn("优惠券已结束: couponId={}", couponId);
            return false;
        }
        
        if (coupon.getReceived_count() >= coupon.getTotal_count()) {
            log.warn("优惠券已领完: couponId={}", couponId);
            return false;
        }
        
        int result = this.baseMapper.incrementReceivedCount(couponId);
        if (result > 0) {
            log.info("领取优惠券成功: couponId={}", couponId);
        }
        return result > 0;
    }
    
    /**
     * 使用优惠券
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean useCoupon(Long couponId) {
        int result = this.baseMapper.incrementUsedCount(couponId);
        if (result > 0) {
            log.info("使用优惠券: couponId={}", couponId);
        }
        return result > 0;
    }
    
    /**
     * 更新优惠券状态
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Long couponId, Integer status) {
        int result = this.baseMapper.updateStatus(couponId, status);
        return result > 0;
    }
    
    /**
     * 统计商家的优惠券数量
     */
    public Integer countByShopId(Long shopId) {
        return this.baseMapper.countByShopId(shopId);
    }
    
    /**
     * 统计平台优惠券数量
     */
    public Integer countPlatformCoupons() {
        return this.baseMapper.countPlatformCoupons();
    }
    
    /**
     * 查询即将过期的优惠券
     */
    public List<Coupon> getExpiringCoupons(Integer days) {
        LocalDateTime beforeTime = LocalDateTime.now().plusDays(days);
        return this.baseMapper.findExpiringCoupons(beforeTime);
    }
    
    /**
     * 查询已过期的优惠券
     */
    public List<Coupon> getExpiredCoupons() {
        return this.baseMapper.findExpiredCoupons();
    }
    
    /**
     * 批量更新已过期优惠券状态
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateExpiredCouponsStatus() {
        return this.baseMapper.batchUpdateExpiredCouponsStatus();
    }
    
    /**
     * 根据优惠券ID列表批量查询
     */
    public List<Coupon> getCouponsByIds(List<Long> couponIds) {
        if (couponIds == null || couponIds.isEmpty()) {
            return List.of();
        }
        return this.baseMapper.findByCouponIds(couponIds);
    }
    
    /**
     * 统计优惠券使用率
     */
    public Map<String, Object> getCouponUsageStats(Long couponId) {
        return this.baseMapper.getCouponUsageStats(couponId);
    }
    
    /**
     * 分页查询优惠券
     */
    public Page<Coupon> getCouponsPage(Integer pageNum, Integer pageSize, Long shopId, Integer couponType, Integer status) {
        Page<Coupon> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Coupon> queryWrapper = new LambdaQueryWrapper<>();
        
        if (shopId != null) {
            queryWrapper.eq(Coupon::getShop_id, shopId);
        }
        if (couponType != null) {
            queryWrapper.eq(Coupon::getCoupon_type, couponType);
        }
        if (status != null) {
            queryWrapper.eq(Coupon::getStatus, status);
        }
        
        queryWrapper.orderByDesc(Coupon::getCreated_at);
        
        return this.page(page, queryWrapper);
    }
    
    /**
     * 更新优惠券信息
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCoupon(Coupon coupon) {
        return this.updateById(coupon);
    }
    
    /**
     * 删除优惠券
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCoupon(Long couponId) {
        Coupon coupon = this.getById(couponId);
        if (coupon == null) {
            log.warn("优惠券不存在: couponId={}", couponId);
            return false;
        }
        
        return this.removeById(couponId);
    }
}
