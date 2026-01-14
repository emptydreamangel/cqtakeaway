package com.blue.jitian.Controller.coupon;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blue.jitian.Entity.Coupon;
import com.blue.jitian.Service.CouponService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/coupon")
@Slf4j
public class CouponController {
    
    @Autowired
    private CouponService couponService;
    
    /**
     * 创建优惠券
     */
    @PostMapping
    public Map<String, Object> createCoupon(@RequestBody Coupon coupon) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = couponService.createCoupon(coupon);
            if (success) {
                result.put("code", 200);
                result.put("message", "创建成功");
                result.put("data", coupon);
            } else {
                result.put("code", 400);
                result.put("message", "创建失败");
            }
        } catch (Exception e) {
            log.error("创建优惠券失败", e);
            result.put("code", 500);
            result.put("message", "创建失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据ID获取优惠券
     */
    @GetMapping("/{couponId}")
    public Map<String, Object> getCoupon(@PathVariable Long couponId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Coupon coupon = couponService.getById(couponId);
            if (coupon != null) {
                result.put("code", 200);
                result.put("data", coupon);
            } else {
                result.put("code", 404);
                result.put("message", "优惠券不存在");
            }
        } catch (Exception e) {
            log.error("查询优惠券失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 分页查询优惠券
     */
    @GetMapping("/page")
    public Map<String, Object> getCouponsPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long shopId,
            @RequestParam(required = false) Integer couponType,
            @RequestParam(required = false) Integer status) {
        Map<String, Object> result = new HashMap<>();
        try {
            Page<Coupon> page = couponService.getCouponsPage(pageNum, pageSize, shopId, couponType, status);
            result.put("code", 200);
            result.put("data", page);
        } catch (Exception e) {
            log.error("分页查询优惠券失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据商家ID查询优惠券
     */
    @GetMapping("/shop/{shopId}")
    public Map<String, Object> getCouponsByShopId(@PathVariable Long shopId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Coupon> coupons = couponService.getCouponsByShopId(shopId);
            result.put("code", 200);
            result.put("data", coupons);
        } catch (Exception e) {
            log.error("查询商家优惠券失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 查询平台优惠券
     */
    @GetMapping("/platform")
    public Map<String, Object> getPlatformCoupons() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Coupon> coupons = couponService.getPlatformCoupons();
            result.put("code", 200);
            result.put("data", coupons);
        } catch (Exception e) {
            log.error("查询平台优惠券失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据类型查询优惠券
     */
    @GetMapping("/type/{couponType}")
    public Map<String, Object> getCouponsByCouponType(@PathVariable Integer couponType) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Coupon> coupons = couponService.getCouponsByCouponType(couponType);
            result.put("code", 200);
            result.put("data", coupons);
        } catch (Exception e) {
            log.error("按类型查询优惠券失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 查询可领取的优惠券
     */
    @GetMapping("/available")
    public Map<String, Object> getAvailableCoupons() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Coupon> coupons = couponService.getAvailableCoupons();
            result.put("code", 200);
            result.put("data", coupons);
        } catch (Exception e) {
            log.error("查询可领取优惠券失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 查询商家可领取的优惠券
     */
    @GetMapping("/shop/{shopId}/available")
    public Map<String, Object> getAvailableCouponsByShopId(@PathVariable Long shopId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Coupon> coupons = couponService.getAvailableCouponsByShopId(shopId);
            result.put("code", 200);
            result.put("data", coupons);
        } catch (Exception e) {
            log.error("查询商家可领取优惠券失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 领取优惠券
     */
    @PutMapping("/{couponId}/receive")
    public Map<String, Object> receiveCoupon(@PathVariable Long couponId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = couponService.receiveCoupon(couponId);
            if (success) {
                result.put("code", 200);
                result.put("message", "领取成功");
            } else {
                result.put("code", 400);
                result.put("message", "领取失败，优惠券可能已领完或已结束");
            }
        } catch (Exception e) {
            log.error("领取优惠券失败", e);
            result.put("code", 500);
            result.put("message", "领取失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 使用优惠券
     */
    @PutMapping("/{couponId}/use")
    public Map<String, Object> useCoupon(@PathVariable Long couponId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = couponService.useCoupon(couponId);
            if (success) {
                result.put("code", 200);
                result.put("message", "使用成功");
            } else {
                result.put("code", 400);
                result.put("message", "使用失败");
            }
        } catch (Exception e) {
            log.error("使用优惠券失败", e);
            result.put("code", 500);
            result.put("message", "使用失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 更新优惠券状态
     */
    @PutMapping("/{couponId}/status")
    public Map<String, Object> updateStatus(@PathVariable Long couponId, @RequestParam Integer status) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = couponService.updateStatus(couponId, status);
            if (success) {
                result.put("code", 200);
                result.put("message", "状态更新成功");
            } else {
                result.put("code", 400);
                result.put("message", "更新失败");
            }
        } catch (Exception e) {
            log.error("更新优惠券状态失败", e);
            result.put("code", 500);
            result.put("message", "更新失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 更新优惠券信息
     */
    @PutMapping("/{couponId}")
    public Map<String, Object> updateCoupon(@PathVariable Long couponId, @RequestBody Coupon coupon) {
        Map<String, Object> result = new HashMap<>();
        try {
            coupon.setCoupon_id(couponId);
            boolean success = couponService.updateCoupon(coupon);
            if (success) {
                result.put("code", 200);
                result.put("message", "更新成功");
                result.put("data", coupon);
            } else {
                result.put("code", 400);
                result.put("message", "更新失败");
            }
        } catch (Exception e) {
            log.error("更新优惠券失败", e);
            result.put("code", 500);
            result.put("message", "更新失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 删除优惠券
     */
    @DeleteMapping("/{couponId}")
    public Map<String, Object> deleteCoupon(@PathVariable Long couponId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = couponService.deleteCoupon(couponId);
            if (success) {
                result.put("code", 200);
                result.put("message", "删除成功");
            } else {
                result.put("code", 404);
                result.put("message", "优惠券不存在");
            }
        } catch (Exception e) {
            log.error("删除优惠券失败", e);
            result.put("code", 500);
            result.put("message", "删除失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 统计商家的优惠券数量
     */
    @GetMapping("/stats/shop/{shopId}/count")
    public Map<String, Object> countByShopId(@PathVariable Long shopId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Integer count = couponService.countByShopId(shopId);
            result.put("code", 200);
            result.put("data", count);
        } catch (Exception e) {
            log.error("统计商家优惠券数量失败", e);
            result.put("code", 500);
            result.put("message", "统计失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 统计平台优惠券数量
     */
    @GetMapping("/stats/platform/count")
    public Map<String, Object> countPlatformCoupons() {
        Map<String, Object> result = new HashMap<>();
        try {
            Integer count = couponService.countPlatformCoupons();
            result.put("code", 200);
            result.put("data", count);
        } catch (Exception e) {
            log.error("统计平台优惠券数量失败", e);
            result.put("code", 500);
            result.put("message", "统计失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 查询即将过期的优惠券
     */
    @GetMapping("/expiring")
    public Map<String, Object> getExpiringCoupons(@RequestParam(defaultValue = "7") Integer days) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Coupon> coupons = couponService.getExpiringCoupons(days);
            result.put("code", 200);
            result.put("data", coupons);
        } catch (Exception e) {
            log.error("查询即将过期优惠券失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 查询已过期的优惠券
     */
    @GetMapping("/expired")
    public Map<String, Object> getExpiredCoupons() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Coupon> coupons = couponService.getExpiredCoupons();
            result.put("code", 200);
            result.put("data", coupons);
        } catch (Exception e) {
            log.error("查询已过期优惠券失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 批量更新已过期优惠券状态
     */
    @PutMapping("/expired/update-status")
    public Map<String, Object> updateExpiredCouponsStatus() {
        Map<String, Object> result = new HashMap<>();
        try {
            int count = couponService.updateExpiredCouponsStatus();
            result.put("code", 200);
            result.put("message", "更新成功");
            result.put("data", count);
        } catch (Exception e) {
            log.error("批量更新已过期优惠券状态失败", e);
            result.put("code", 500);
            result.put("message", "更新失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 统计优惠券使用率
     */
    @GetMapping("/{couponId}/usage-stats")
    public Map<String, Object> getCouponUsageStats(@PathVariable Long couponId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> stats = couponService.getCouponUsageStats(couponId);
            result.put("code", 200);
            result.put("data", stats);
        } catch (Exception e) {
            log.error("统计优惠券使用率失败", e);
            result.put("code", 500);
            result.put("message", "统计失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据ID列表批量查询优惠券
     */
    @PostMapping("/batch/query")
    public Map<String, Object> getCouponsByIds(@RequestBody List<Long> couponIds) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Coupon> coupons = couponService.getCouponsByIds(couponIds);
            result.put("code", 200);
            result.put("data", coupons);
        } catch (Exception e) {
            log.error("批量查询优惠券失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
}
