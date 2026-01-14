package com.blue.jitian.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.jitian.Entity.OrderReview;
import com.blue.jitian.Mapper.OrderReviewMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OrderReviewService extends ServiceImpl<OrderReviewMapper, OrderReview> {
    
    /**
     * 创建评价
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean createReview(OrderReview review) {
        // 检查订单是否已评价
        OrderReview existing = this.baseMapper.findByOrderId(review.getOrder_id());
        if (existing != null) {
            log.warn("订单已评价: orderId={}", review.getOrder_id());
            return false;
        }
        
        // 设置默认值
        if (review.getIs_anonymous() == null) {
            review.setIs_anonymous(0);
        }
        if (review.getLike_count() == null) {
            review.setLike_count(0);
        }
        if (review.getStatus() == null) {
            review.setStatus(1); // 默认显示
        }
        
        return this.save(review);
    }
    
    /**
     * 根据订单ID查询评价
     */
    public OrderReview getByOrderId(Long orderId) {
        return this.baseMapper.findByOrderId(orderId);
    }
    
    /**
     * 根据用户ID查询评价列表
     */
    public List<OrderReview> getReviewsByUserId(Long userId) {
        return this.baseMapper.findByUserId(userId);
    }
    
    /**
     * 根据商家ID查询评价列表
     */
    public List<OrderReview> getReviewsByShopId(Long shopId) {
        return this.baseMapper.findByShopId(shopId);
    }
    
    /**
     * 根据骑手ID查询评价列表
     */
    public List<OrderReview> getReviewsByRiderId(Long riderId) {
        return this.baseMapper.findByRiderId(riderId);
    }
    
    /**
     * 查询商家的高分评价
     */
    public List<OrderReview> getGoodReviewsByShopId(Long shopId, Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        return this.baseMapper.findGoodReviewsByShopId(shopId, limit);
    }
    
    /**
     * 查询有图评价
     */
    public List<OrderReview> getReviewsWithImagesByShopId(Long shopId) {
        return this.baseMapper.findReviewsWithImagesByShopId(shopId);
    }
    
    /**
     * 商家回复评价
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean replyReview(Long reviewId, String replyContent) {
        OrderReview review = this.getById(reviewId);
        if (review == null) {
            log.warn("评价不存在: reviewId={}", reviewId);
            return false;
        }
        
        int result = this.baseMapper.replyReview(reviewId, replyContent);
        if (result > 0) {
            log.info("商家回复评价成功: reviewId={}", reviewId);
        }
        return result > 0;
    }
    
    /**
     * 点赞评价
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean likeReview(Long reviewId) {
        int result = this.baseMapper.incrementLikeCount(reviewId);
        return result > 0;
    }
    
    /**
     * 取消点赞
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean unlikeReview(Long reviewId) {
        int result = this.baseMapper.decrementLikeCount(reviewId);
        return result > 0;
    }
    
    /**
     * 更新评价状态（显示/隐藏）
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Long reviewId, Integer status) {
        int result = this.baseMapper.updateStatus(reviewId, status);
        return result > 0;
    }
    
    /**
     * 统计商家的评价数量
     */
    public Integer countByShopId(Long shopId) {
        return this.baseMapper.countByShopId(shopId);
    }
    
    /**
     * 统计商家的平均评分
     */
    public Double getAverageRatingByShopId(Long shopId) {
        Double avgRating = this.baseMapper.getAverageRatingByShopId(shopId);
        return avgRating != null ? avgRating : 0.0;
    }
    
    /**
     * 统计商家的好评率
     */
    public Double getGoodRateByShopId(Long shopId) {
        Double goodRate = this.baseMapper.getGoodRateByShopId(shopId);
        return goodRate != null ? goodRate : 0.0;
    }
    
    /**
     * 统计骑手的配送评分
     */
    public Double getAverageDeliveryRatingByRiderId(Long riderId) {
        Double avgRating = this.baseMapper.getAverageDeliveryRatingByRiderId(riderId);
        return avgRating != null ? avgRating : 0.0;
    }
    
    /**
     * 根据评分范围查询评价
     */
    public List<OrderReview> getReviewsByRatingRange(Long shopId, Integer minRating, Integer maxRating) {
        return this.baseMapper.findByRatingRange(shopId, minRating, maxRating);
    }
    
    /**
     * 查询时间范围内的评价
     */
    public List<OrderReview> getReviewsByTimeRange(Long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        return this.baseMapper.findByTimeRange(shopId, startTime, endTime);
    }
    
    /**
     * 统计各评分的数量
     */
    public List<Map<String, Object>> countByRating(Long shopId) {
        return this.baseMapper.countByRating(shopId);
    }
    
    /**
     * 查询待回复的评价
     */
    public List<OrderReview> getUnrepliedReviewsByShopId(Long shopId) {
        return this.baseMapper.findUnrepliedByShopId(shopId);
    }
    
    /**
     * 根据订单ID列表批量查询评价
     */
    public List<OrderReview> getReviewsByOrderIds(List<Long> orderIds) {
        if (orderIds == null || orderIds.isEmpty()) {
            return List.of();
        }
        return this.baseMapper.findByOrderIds(orderIds);
    }
    
    /**
     * 分页查询评价
     */
    public Page<OrderReview> getReviewsPage(Integer pageNum, Integer pageSize, Long shopId, Long userId, Integer status) {
        Page<OrderReview> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<OrderReview> queryWrapper = new LambdaQueryWrapper<>();
        
        if (shopId != null) {
            queryWrapper.eq(OrderReview::getShop_id, shopId);
        }
        if (userId != null) {
            queryWrapper.eq(OrderReview::getUser_id, userId);
        }
        if (status != null) {
            queryWrapper.eq(OrderReview::getStatus, status);
        }
        
        queryWrapper.orderByDesc(OrderReview::getCreated_at);
        
        return this.page(page, queryWrapper);
    }
    
    /**
     * 更新评价信息
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateReview(OrderReview review) {
        return this.updateById(review);
    }
    
    /**
     * 删除评价
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteReview(Long reviewId) {
        OrderReview review = this.getById(reviewId);
        if (review == null) {
            log.warn("评价不存在: reviewId={}", reviewId);
            return false;
        }
        
        return this.removeById(reviewId);
    }
}
