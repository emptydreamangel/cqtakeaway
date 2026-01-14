package com.blue.jitian.Controller.coupon;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blue.jitian.Entity.UserCoupon;
import com.blue.jitian.Service.UserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/user-coupons")
public class UserCouponController {

    @Autowired
    private UserCouponService userCouponService;

    /**
     * 用户领取优惠券
     * @param userId 用户ID
     * @param couponId 优惠券ID
     * @return 领取结果
     */
    @PostMapping("/receive")
    public ResponseEntity<Map<String, Object>> receiveCoupon(@RequestParam Long userId, 
                                                               @RequestParam Long couponId) {
        Map<String, Object> response = new HashMap<>();
        UserCoupon userCoupon = userCouponService.receiveCoupon(userId, couponId);
        if (userCoupon != null) {
            response.put("success", true);
            response.put("message", "领取成功");
            response.put("data", userCoupon);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "领取失败，优惠券已领完或已结束");
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 使用优惠券
     * @param userCouponId 用户优惠券ID
     * @param orderId 订单ID
     * @return 使用结果
     */
    @PutMapping("/use")
    public ResponseEntity<Map<String, Object>> useCoupon(@RequestParam Long userCouponId,
                                                           @RequestParam Long orderId) {
        Map<String, Object> response = new HashMap<>();
        boolean success = userCouponService.useCoupon(userCouponId, orderId);
        if (success) {
            response.put("success", true);
            response.put("message", "优惠券使用成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "优惠券使用失败，可能已过期或已使用");
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 查询用户的优惠券列表
     * @param userId 用户ID
     * @param status 状态（可选）
     * @return 优惠券列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserCoupon>> getUserCoupons(@PathVariable Long userId,
                                                             @RequestParam(required = false) Integer status) {
        List<UserCoupon> coupons = userCouponService.getUserCoupons(userId, status);
        return ResponseEntity.ok(coupons);
    }

    /**
     * 查询用户的可用优惠券列表（包含优惠券详情）
     * @param userId 用户ID
     * @return 可用优惠券列表
     */
    @GetMapping("/user/{userId}/available")
    public ResponseEntity<List<Map<String, Object>>> getAvailableCoupons(@PathVariable Long userId) {
        List<Map<String, Object>> coupons = userCouponService.getAvailableCoupons(userId);
        return ResponseEntity.ok(coupons);
    }

    /**
     * 分页查询用户优惠券
     * @param current 当前页
     * @param size 每页大小
     * @param userId 用户ID（可选）
     * @param status 状态（可选）
     * @param couponId 优惠券ID（可选）
     * @return 分页结果
     */
    @GetMapping("/page")
    public ResponseEntity<Page<UserCoupon>> getUserCouponPage(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long couponId) {
        Page<UserCoupon> page = userCouponService.getUserCouponPage(current, size, userId, status, couponId);
        return ResponseEntity.ok(page);
    }

    /**
     * 根据ID查询用户优惠券详情
     * @param id 用户优惠券ID
     * @return UserCoupon对象
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserCoupon> getById(@PathVariable Long id) {
        UserCoupon userCoupon = userCouponService.getById(id);
        if (userCoupon != null) {
            return ResponseEntity.ok(userCoupon);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 更新用户优惠券
     * @param id 用户优惠券ID
     * @param userCoupon 更新内容
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, 
                                                        @RequestBody UserCoupon userCoupon) {
        Map<String, Object> response = new HashMap<>();
        userCoupon.setUserCouponId(id);
        boolean success = userCouponService.updateById(userCoupon);
        if (success) {
            response.put("success", true);
            response.put("message", "更新成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "更新失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 删除用户优惠券
     * @param id 用户优惠券ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        boolean success = userCouponService.removeById(id);
        if (success) {
            response.put("success", true);
            response.put("message", "删除成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "删除失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 批量更新过期优惠券状态
     * @return 更新数量
     */
    @PostMapping("/update-expired")
    public ResponseEntity<Map<String, Object>> updateExpiredCoupons() {
        Map<String, Object> response = new HashMap<>();
        int count = userCouponService.updateExpiredCoupons();
        response.put("success", true);
        response.put("message", "已更新" + count + "张过期优惠券");
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    /**
     * 统计用户各状态优惠券数量
     * @param userId 用户ID
     * @return 统计结果
     */
    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<List<Map<String, Object>>> countByStatus(@PathVariable Long userId) {
        List<Map<String, Object>> stats = userCouponService.countByStatus(userId);
        return ResponseEntity.ok(stats);
    }

    /**
     * 查询即将过期的优惠券
     * @param userId 用户ID
     * @param days 天数（默认7天）
     * @return 即将过期的优惠券列表
     */
    @GetMapping("/user/{userId}/expiring")
    public ResponseEntity<List<Map<String, Object>>> getExpiringCoupons(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "7") int days) {
        List<Map<String, Object>> coupons = userCouponService.getExpiringCoupons(userId, days);
        return ResponseEntity.ok(coupons);
    }

    /**
     * 统计指定优惠券的使用情况
     * @param couponId 优惠券ID
     * @return 统计结果
     */
    @GetMapping("/coupon/{couponId}/usage-stats")
    public ResponseEntity<List<Map<String, Object>>> getCouponUsageStats(@PathVariable Long couponId) {
        List<Map<String, Object>> stats = userCouponService.getCouponUsageStats(couponId);
        return ResponseEntity.ok(stats);
    }

    /**
     * 检查用户是否已领取指定优惠券
     * @param userId 用户ID
     * @param couponId 优惠券ID
     * @return 是否已领取
     */
    @GetMapping("/check-received")
    public ResponseEntity<Map<String, Object>> hasReceivedCoupon(@RequestParam Long userId,
                                                                   @RequestParam Long couponId) {
        Map<String, Object> response = new HashMap<>();
        boolean hasReceived = userCouponService.hasReceivedCoupon(userId, couponId);
        response.put("hasReceived", hasReceived);
        return ResponseEntity.ok(response);
    }

    /**
     * 根据订单ID查询使用的优惠券
     * @param orderId 订单ID
     * @return UserCoupon对象
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<UserCoupon> getByOrderId(@PathVariable Long orderId) {
        UserCoupon userCoupon = userCouponService.getByOrderId(orderId);
        if (userCoupon != null) {
            return ResponseEntity.ok(userCoupon);
        }
        return ResponseEntity.notFound().build();
    }
}
