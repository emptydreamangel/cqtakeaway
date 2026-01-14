package com.blue.jitian.Controller.review;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blue.jitian.Entity.OrderReview;
import com.blue.jitian.Service.OrderReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order-review")
@Slf4j
public class OrderReviewController {
    
    @Autowired
    private OrderReviewService orderReviewService;
    
    /**
     * 创建评价
     */
    @PostMapping
    public Map<String, Object> createReview(@RequestBody OrderReview review) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = orderReviewService.createReview(review);
            if (success) {
                result.put("code", 200);
                result.put("message", "评价成功");
                result.put("data", review);
            } else {
                result.put("code", 400);
                result.put("message", "评价失败，订单可能已评价");
            }
        } catch (Exception e) {
            log.error("创建评价失败", e);
            result.put("code", 500);
            result.put("message", "评价失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据ID获取评价
     */
    @GetMapping("/{reviewId}")
    public Map<String, Object> getReview(@PathVariable Long reviewId) {
        Map<String, Object> result = new HashMap<>();
        try {
            OrderReview review = orderReviewService.getById(reviewId);
            if (review != null) {
                result.put("code", 200);
                result.put("data", review);
            } else {
                result.put("code", 404);
                result.put("message", "评价不存在");
            }
        } catch (Exception e) {
            log.error("查询评价失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据订单ID获取评价
     */
    @GetMapping("/order/{orderId}")
    public Map<String, Object> getReviewByOrderId(@PathVariable Long orderId) {
        Map<String, Object> result = new HashMap<>();
        try {
            OrderReview review = orderReviewService.getByOrderId(orderId);
            if (review != null) {
                result.put("code", 200);
                result.put("data", review);
            } else {
                result.put("code", 404);
                result.put("message", "订单暂无评价");
            }
        } catch (Exception e) {
            log.error("查询订单评价失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 分页查询评价
     */
    @GetMapping("/page")
    public Map<String, Object> getReviewsPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long shopId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Integer status) {
        Map<String, Object> result = new HashMap<>();
        try {
            Page<OrderReview> page = orderReviewService.getReviewsPage(pageNum, pageSize, shopId, userId, status);
            result.put("code", 200);
            result.put("data", page);
        } catch (Exception e) {
            log.error("分页查询评价失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据用户ID查询评价
     */
    @GetMapping("/user/{userId}")
    public Map<String, Object> getReviewsByUserId(@PathVariable Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<OrderReview> reviews = orderReviewService.getReviewsByUserId(userId);
            result.put("code", 200);
            result.put("data", reviews);
        } catch (Exception e) {
            log.error("查询用户评价失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据商家ID查询评价
     */
    @GetMapping("/shop/{shopId}")
    public Map<String, Object> getReviewsByShopId(@PathVariable Long shopId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<OrderReview> reviews = orderReviewService.getReviewsByShopId(shopId);
            result.put("code", 200);
            result.put("data", reviews);
        } catch (Exception e) {
            log.error("查询商家评价失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据骑手ID查询评价
     */
    @GetMapping("/rider/{riderId}")
    public Map<String, Object> getReviewsByRiderId(@PathVariable Long riderId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<OrderReview> reviews = orderReviewService.getReviewsByRiderId(riderId);
            result.put("code", 200);
            result.put("data", reviews);
        } catch (Exception e) {
            log.error("查询骑手评价失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 查询商家的高分评价
     */
    @GetMapping("/shop/{shopId}/good")
    public Map<String, Object> getGoodReviewsByShopId(
            @PathVariable Long shopId,
            @RequestParam(defaultValue = "10") Integer limit) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<OrderReview> reviews = orderReviewService.getGoodReviewsByShopId(shopId, limit);
            result.put("code", 200);
            result.put("data", reviews);
        } catch (Exception e) {
            log.error("查询好评失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 查询有图评价
     */
    @GetMapping("/shop/{shopId}/with-images")
    public Map<String, Object> getReviewsWithImagesByShopId(@PathVariable Long shopId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<OrderReview> reviews = orderReviewService.getReviewsWithImagesByShopId(shopId);
            result.put("code", 200);
            result.put("data", reviews);
        } catch (Exception e) {
            log.error("查询有图评价失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 商家回复评价
     */
    @PutMapping("/{reviewId}/reply")
    public Map<String, Object> replyReview(@PathVariable Long reviewId, @RequestParam String replyContent) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = orderReviewService.replyReview(reviewId, replyContent);
            if (success) {
                result.put("code", 200);
                result.put("message", "回复成功");
            } else {
                result.put("code", 400);
                result.put("message", "回复失败");
            }
        } catch (Exception e) {
            log.error("回复评价失败", e);
            result.put("code", 500);
            result.put("message", "回复失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 点赞评价
     */
    @PutMapping("/{reviewId}/like")
    public Map<String, Object> likeReview(@PathVariable Long reviewId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = orderReviewService.likeReview(reviewId);
            if (success) {
                result.put("code", 200);
                result.put("message", "点赞成功");
            } else {
                result.put("code", 400);
                result.put("message", "点赞失败");
            }
        } catch (Exception e) {
            log.error("点赞评价失败", e);
            result.put("code", 500);
            result.put("message", "点赞失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 取消点赞
     */
    @PutMapping("/{reviewId}/unlike")
    public Map<String, Object> unlikeReview(@PathVariable Long reviewId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = orderReviewService.unlikeReview(reviewId);
            if (success) {
                result.put("code", 200);
                result.put("message", "取消点赞成功");
            } else {
                result.put("code", 400);
                result.put("message", "取消点赞失败");
            }
        } catch (Exception e) {
            log.error("取消点赞失败", e);
            result.put("code", 500);
            result.put("message", "操作失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 更新评价状态
     */
    @PutMapping("/{reviewId}/status")
    public Map<String, Object> updateStatus(@PathVariable Long reviewId, @RequestParam Integer status) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = orderReviewService.updateStatus(reviewId, status);
            if (success) {
                result.put("code", 200);
                result.put("message", "状态更新成功");
            } else {
                result.put("code", 400);
                result.put("message", "更新失败");
            }
        } catch (Exception e) {
            log.error("更新评价状态失败", e);
            result.put("code", 500);
            result.put("message", "更新失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 更新评价信息
     */
    @PutMapping("/{reviewId}")
    public Map<String, Object> updateReview(@PathVariable Long reviewId, @RequestBody OrderReview review) {
        Map<String, Object> result = new HashMap<>();
        try {
            review.setReview_id(reviewId);
            boolean success = orderReviewService.updateReview(review);
            if (success) {
                result.put("code", 200);
                result.put("message", "更新成功");
                result.put("data", review);
            } else {
                result.put("code", 400);
                result.put("message", "更新失败");
            }
        } catch (Exception e) {
            log.error("更新评价失败", e);
            result.put("code", 500);
            result.put("message", "更新失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 删除评价
     */
    @DeleteMapping("/{reviewId}")
    public Map<String, Object> deleteReview(@PathVariable Long reviewId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = orderReviewService.deleteReview(reviewId);
            if (success) {
                result.put("code", 200);
                result.put("message", "删除成功");
            } else {
                result.put("code", 404);
                result.put("message", "评价不存在");
            }
        } catch (Exception e) {
            log.error("删除评价失败", e);
            result.put("code", 500);
            result.put("message", "删除失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 统计商家评价数量
     */
    @GetMapping("/stats/shop/{shopId}/count")
    public Map<String, Object> countByShopId(@PathVariable Long shopId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Integer count = orderReviewService.countByShopId(shopId);
            result.put("code", 200);
            result.put("data", count);
        } catch (Exception e) {
            log.error("统计评价数量失败", e);
            result.put("code", 500);
            result.put("message", "统计失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 统计商家平均评分
     */
    @GetMapping("/stats/shop/{shopId}/average-rating")
    public Map<String, Object> getAverageRatingByShopId(@PathVariable Long shopId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Double avgRating = orderReviewService.getAverageRatingByShopId(shopId);
            result.put("code", 200);
            result.put("data", avgRating);
        } catch (Exception e) {
            log.error("统计平均评分失败", e);
            result.put("code", 500);
            result.put("message", "统计失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 统计商家好评率
     */
    @GetMapping("/stats/shop/{shopId}/good-rate")
    public Map<String, Object> getGoodRateByShopId(@PathVariable Long shopId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Double goodRate = orderReviewService.getGoodRateByShopId(shopId);
            result.put("code", 200);
            result.put("data", goodRate);
        } catch (Exception e) {
            log.error("统计好评率失败", e);
            result.put("code", 500);
            result.put("message", "统计失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 统计骑手配送评分
     */
    @GetMapping("/stats/rider/{riderId}/delivery-rating")
    public Map<String, Object> getAverageDeliveryRatingByRiderId(@PathVariable Long riderId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Double avgRating = orderReviewService.getAverageDeliveryRatingByRiderId(riderId);
            result.put("code", 200);
            result.put("data", avgRating);
        } catch (Exception e) {
            log.error("统计骑手评分失败", e);
            result.put("code", 500);
            result.put("message", "统计失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据评分范围查询评价
     */
    @GetMapping("/shop/{shopId}/rating-range")
    public Map<String, Object> getReviewsByRatingRange(
            @PathVariable Long shopId,
            @RequestParam Integer minRating,
            @RequestParam Integer maxRating) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<OrderReview> reviews = orderReviewService.getReviewsByRatingRange(shopId, minRating, maxRating);
            result.put("code", 200);
            result.put("data", reviews);
        } catch (Exception e) {
            log.error("按评分范围查询失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 查询时间范围内的评价
     */
    @GetMapping("/shop/{shopId}/time-range")
    public Map<String, Object> getReviewsByTimeRange(
            @PathVariable Long shopId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<OrderReview> reviews = orderReviewService.getReviewsByTimeRange(shopId, startTime, endTime);
            result.put("code", 200);
            result.put("data", reviews);
        } catch (Exception e) {
            log.error("按时间范围查询评价失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 统计各评分的数量
     */
    @GetMapping("/stats/shop/{shopId}/rating-distribution")
    public Map<String, Object> countByRating(@PathVariable Long shopId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> stats = orderReviewService.countByRating(shopId);
            result.put("code", 200);
            result.put("data", stats);
        } catch (Exception e) {
            log.error("统计评分分布失败", e);
            result.put("code", 500);
            result.put("message", "统计失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 查询待回复的评价
     */
    @GetMapping("/shop/{shopId}/unreplied")
    public Map<String, Object> getUnrepliedReviewsByShopId(@PathVariable Long shopId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<OrderReview> reviews = orderReviewService.getUnrepliedReviewsByShopId(shopId);
            result.put("code", 200);
            result.put("data", reviews);
        } catch (Exception e) {
            log.error("查询待回复评价失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据订单ID列表批量查询评价
     */
    @PostMapping("/batch/query")
    public Map<String, Object> getReviewsByOrderIds(@RequestBody List<Long> orderIds) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<OrderReview> reviews = orderReviewService.getReviewsByOrderIds(orderIds);
            result.put("code", 200);
            result.put("data", reviews);
        } catch (Exception e) {
            log.error("批量查询评价失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
}
