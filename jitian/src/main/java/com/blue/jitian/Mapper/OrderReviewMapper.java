package com.blue.jitian.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blue.jitian.Entity.OrderReview;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderReviewMapper extends BaseMapper<OrderReview> {
    
    /**
     * 根据订单ID查询评价
     */
    @Select("SELECT * FROM order_reviews WHERE order_id = #{orderId}")
    OrderReview findByOrderId(@Param("orderId") Long orderId);
    
    /**
     * 根据用户ID查询评价列表
     */
    @Select("SELECT * FROM order_reviews WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<OrderReview> findByUserId(@Param("userId") Long userId);
    
    /**
     * 根据商家ID查询评价列表
     */
    @Select("SELECT * FROM order_reviews WHERE shop_id = #{shopId} AND status = 1 ORDER BY created_at DESC")
    List<OrderReview> findByShopId(@Param("shopId") Long shopId);
    
    /**
     * 根据骑手ID查询评价列表
     */
    @Select("SELECT * FROM order_reviews WHERE rider_id = #{riderId} AND status = 1 ORDER BY created_at DESC")
    List<OrderReview> findByRiderId(@Param("riderId") Long riderId);
    
    /**
     * 根据状态查询评价列表
     */
    @Select("SELECT * FROM order_reviews WHERE status = #{status} ORDER BY created_at DESC")
    List<OrderReview> findByStatus(@Param("status") Integer status);
    
    /**
     * 查询商家的高分评价（>=4分）
     */
    @Select("SELECT * FROM order_reviews WHERE shop_id = #{shopId} AND shop_rating >= 4 AND status = 1 ORDER BY created_at DESC LIMIT #{limit}")
    List<OrderReview> findGoodReviewsByShopId(@Param("shopId") Long shopId, @Param("limit") Integer limit);
    
    /**
     * 查询有图评价
     */
    @Select("SELECT * FROM order_reviews WHERE shop_id = #{shopId} AND images IS NOT NULL AND images != '[]' AND status = 1 ORDER BY created_at DESC")
    List<OrderReview> findReviewsWithImagesByShopId(@Param("shopId") Long shopId);
    
    /**
     * 商家回复评价
     */
    @Update("UPDATE order_reviews SET reply_content = #{replyContent}, reply_time = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP WHERE review_id = #{reviewId}")
    int replyReview(@Param("reviewId") Long reviewId, @Param("replyContent") String replyContent);
    
    /**
     * 增加点赞数
     */
    @Update("UPDATE order_reviews SET like_count = like_count + 1, updated_at = CURRENT_TIMESTAMP WHERE review_id = #{reviewId}")
    int incrementLikeCount(@Param("reviewId") Long reviewId);
    
    /**
     * 减少点赞数
     */
    @Update("UPDATE order_reviews SET like_count = like_count - 1, updated_at = CURRENT_TIMESTAMP WHERE review_id = #{reviewId} AND like_count > 0")
    int decrementLikeCount(@Param("reviewId") Long reviewId);
    
    /**
     * 更新评价状态
     */
    @Update("UPDATE order_reviews SET status = #{status}, updated_at = CURRENT_TIMESTAMP WHERE review_id = #{reviewId}")
    int updateStatus(@Param("reviewId") Long reviewId, @Param("status") Integer status);
    
    /**
     * 统计商家的评价数量
     */
    @Select("SELECT COUNT(*) FROM order_reviews WHERE shop_id = #{shopId} AND status = 1")
    Integer countByShopId(@Param("shopId") Long shopId);
    
    /**
     * 统计商家的平均评分
     */
    @Select("SELECT AVG((shop_rating + delivery_rating + taste_rating) / 3.0) FROM order_reviews WHERE shop_id = #{shopId} AND status = 1")
    Double getAverageRatingByShopId(@Param("shopId") Long shopId);
    
    /**
     * 统计商家的好评率（>=4分）
     */
    @Select("SELECT COUNT(*) * 100.0 / NULLIF((SELECT COUNT(*) FROM order_reviews WHERE shop_id = #{shopId} AND status = 1), 0) " +
            "FROM order_reviews WHERE shop_id = #{shopId} AND shop_rating >= 4 AND status = 1")
    Double getGoodRateByShopId(@Param("shopId") Long shopId);
    
    /**
     * 统计骑手的配送评分
     */
    @Select("SELECT AVG(delivery_rating) FROM order_reviews WHERE rider_id = #{riderId} AND delivery_rating IS NOT NULL AND status = 1")
    Double getAverageDeliveryRatingByRiderId(@Param("riderId") Long riderId);
    
    /**
     * 根据评分范围查询评价
     */
    @Select("SELECT * FROM order_reviews WHERE shop_id = #{shopId} AND shop_rating BETWEEN #{minRating} AND #{maxRating} AND status = 1 ORDER BY created_at DESC")
    List<OrderReview> findByRatingRange(@Param("shopId") Long shopId, @Param("minRating") Integer minRating, @Param("maxRating") Integer maxRating);
    
    /**
     * 查询时间范围内的评价
     */
    @Select("SELECT * FROM order_reviews WHERE shop_id = #{shopId} AND created_at BETWEEN #{startTime} AND #{endTime} AND status = 1 ORDER BY created_at DESC")
    List<OrderReview> findByTimeRange(@Param("shopId") Long shopId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    
    /**
     * 统计各评分的数量
     */
    @Select("SELECT shop_rating as rating, COUNT(*) as count FROM order_reviews WHERE shop_id = #{shopId} AND status = 1 GROUP BY shop_rating ORDER BY shop_rating DESC")
    @MapKey("rating")
    List<Map<String, Object>> countByRating(@Param("shopId") Long shopId);
    
    /**
     * 查询待回复的评价
     */
    @Select("SELECT * FROM order_reviews WHERE shop_id = #{shopId} AND reply_content IS NULL AND status = 1 ORDER BY created_at DESC")
    List<OrderReview> findUnrepliedByShopId(@Param("shopId") Long shopId);
    
    /**
     * 根据订单ID列表批量查询评价
     */
    @Select("<script>" +
            "SELECT * FROM order_reviews WHERE order_id IN " +
            "<foreach collection='orderIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    List<OrderReview> findByOrderIds(@Param("orderIds") List<Long> orderIds);
}
