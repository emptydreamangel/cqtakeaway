package com.blue.jitian.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blue.jitian.Entity.Order;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;


@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 根据订单号查询
     * @param orderNo 订单号
     * @return 订单对象
     */
    @Select("SELECT * FROM orders WHERE order_no = #{orderNo}")
    Order findByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 根据用户ID查询订单列表
     * @param userId 用户ID
     * @return 订单列表
     */
    @Select("SELECT * FROM orders WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<Order> findByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID分页查询订单
     * @param page 分页对象
     * @param userId 用户ID
     * @return 分页结果
     */
    @Select("SELECT * FROM orders WHERE user_id = #{userId} ORDER BY created_at DESC")
    IPage<Order> findByUserIdPage(Page<Order> page, @Param("userId") Long userId);

    /**
     * 根据商家ID查询订单列表
     * @param shopId 商家ID
     * @return 订单列表
     */
    @Select("SELECT * FROM orders WHERE shop_id = #{shopId} ORDER BY created_at DESC")
    List<Order> findByShopId(@Param("shopId") Long shopId);

    /**
     * 根据商家ID分页查询订单
     * @param page 分页对象
     * @param shopId 商家ID
     * @return 分页结果
     */
    @Select("SELECT * FROM orders WHERE shop_id = #{shopId} ORDER BY created_at DESC")
    IPage<Order> findByShopIdPage(Page<Order> page, @Param("shopId") Long shopId);

    /**
     * 根据状态查询订单列表
     * @param status 订单状态
     * @return 订单列表
     */
    @Select("SELECT * FROM orders WHERE status = #{status} ORDER BY created_at DESC")
    List<Order> findByStatus(@Param("status") Integer status);

    /**
     * 根据用户ID和状态查询订单列表
     * @param userId 用户ID
     * @param status 订单状态
     * @return 订单列表
     */
    @Select("SELECT * FROM orders WHERE user_id = #{userId} AND status = #{status} ORDER BY created_at DESC")
    List<Order> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Integer status);

    /**
     * 根据用户ID和状态分页查询订单
     * @param page 分页对象
     * @param userId 用户ID
     * @param status 订单状态
     * @return 分页结果
     */
    @Select("SELECT * FROM orders WHERE user_id = #{userId} AND status = #{status} ORDER BY created_at DESC")
    IPage<Order> findByUserIdAndStatusPage(Page<Order> page, @Param("userId") Long userId, @Param("status") Integer status);

    /**
     * 根据商家ID和状态查询订单列表
     * @param shopId 商家ID
     * @param status 订单状态
     * @return 订单列表
     */
    @Select("SELECT * FROM orders WHERE shop_id = #{shopId} AND status = #{status} ORDER BY created_at DESC")
    List<Order> findByShopIdAndStatus(@Param("shopId") Long shopId, @Param("status") Integer status);

    /**
     * 根据商家ID和状态分页查询订单
     * @param page 分页对象
     * @param shopId 商家ID
     * @param status 订单状态
     * @return 分页结果
     */
    @Select("SELECT * FROM orders WHERE shop_id = #{shopId} AND status = #{status} ORDER BY created_at DESC")
    IPage<Order> findByShopIdAndStatusPage(Page<Order> page, @Param("shopId") Long shopId, @Param("status") Integer status);

    /**
     * 更新订单状态
     * @param orderId 订单ID
     * @param status 订单状态
     * @return 影响行数
     */
    @Update("UPDATE orders SET status = #{status}, updated_at = NOW() WHERE order_id = #{orderId}")
    int updateStatus(@Param("orderId") Long orderId, @Param("status") Integer status);

    /**
     * 更新支付状态
     * @param orderId 订单ID
     * @param payStatus 支付状态
     * @return 影响行数
     */
    @Update("UPDATE orders SET pay_status = #{payStatus}, updated_at = NOW() WHERE order_id = #{orderId}")
    int updatePayStatus(@Param("orderId") Long orderId, @Param("payStatus") Integer payStatus);

    /**
     * 支付订单（更新支付状态、支付时间、支付方式、订单状态）
     * @param orderId 订单ID
     * @param payMethod 支付方式
     * @param payTime 支付时间
     * @return 影响行数
     */
    @Update("UPDATE orders SET pay_status = 1, pay_method = #{payMethod}, pay_time = #{payTime}, status = 1, updated_at = NOW() WHERE order_id = #{orderId}")
    int payOrder(@Param("orderId") Long orderId, @Param("payMethod") Integer payMethod, @Param("payTime") LocalDateTime payTime);

    /**
     * 商家接单
     * @param orderId 订单ID
     * @param acceptTime 接单时间
     * @return 影响行数
     */
    @Update("UPDATE orders SET status = 2, accept_time = #{acceptTime}, updated_at = NOW() WHERE order_id = #{orderId}")
    int acceptOrder(@Param("orderId") Long orderId, @Param("acceptTime") LocalDateTime acceptTime);

    /**
     * 配送员接单
     * @param orderId 订单ID
     * @param dispatchTime 接单时间
     * @return 影响行数
     */
    @Update("UPDATE orders SET status = 3, dispatch_time = #{dispatchTime}, updated_at = NOW() WHERE order_id = #{orderId}")
    int dispatchOrder(@Param("orderId") Long orderId, @Param("dispatchTime") LocalDateTime dispatchTime);

    /**
     * 完成订单
     * @param orderId 订单ID
     * @param completeTime 完成时间
     * @return 影响行数
     */
    @Update("UPDATE orders SET status = 4, complete_time = #{completeTime}, updated_at = NOW() WHERE order_id = #{orderId}")
    int completeOrder(@Param("orderId") Long orderId, @Param("completeTime") LocalDateTime completeTime);

    /**
     * 取消订单
     * @param orderId 订单ID
     * @param cancelTime 取消时间
     * @param cancelReason 取消原因
     * @return 影响行数
     */
    @Update("UPDATE orders SET status = 5, cancel_time = #{cancelTime}, cancel_reason = #{cancelReason}, updated_at = NOW() WHERE order_id = #{orderId}")
    int cancelOrder(@Param("orderId") Long orderId, @Param("cancelTime") LocalDateTime cancelTime, @Param("cancelReason") String cancelReason);

    /**
     * 统计用户订单数量
     * @param userId 用户ID
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM orders WHERE user_id = #{userId}")
    long countByUserId(@Param("userId") Long userId);

    /**
     * 统计用户指定状态的订单数量
     * @param userId 用户ID
     * @param status 订单状态
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM orders WHERE user_id = #{userId} AND status = #{status}")
    long countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Integer status);

    /**
     * 统计商家订单数量
     * @param shopId 商家ID
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM orders WHERE shop_id = #{shopId}")
    long countByShopId(@Param("shopId") Long shopId);

    /**
     * 统计商家指定状态的订单数量
     * @param shopId 商家ID
     * @param status 订单状态
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM orders WHERE shop_id = #{shopId} AND status = #{status}")
    long countByShopIdAndStatus(@Param("shopId") Long shopId, @Param("status") Integer status);

    /**
     * 查询时间范围内的订单
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 订单列表
     */
    @Select("SELECT * FROM orders WHERE created_at >= #{startTime} AND created_at <= #{endTime} ORDER BY created_at DESC")
    List<Order> findByTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 查询用户时间范围内的订单
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 订单列表
     */
    @Select("SELECT * FROM orders WHERE user_id = #{userId} AND created_at >= #{startTime} AND created_at <= #{endTime} ORDER BY created_at DESC")
    List<Order> findByUserIdAndTimeRange(@Param("userId") Long userId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 查询商家时间范围内的订单
     * @param shopId 商家ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 订单列表
     */
    @Select("SELECT * FROM orders WHERE shop_id = #{shopId} AND created_at >= #{startTime} AND created_at <= #{endTime} ORDER BY created_at DESC")
    List<Order> findByShopIdAndTimeRange(@Param("shopId") Long shopId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
