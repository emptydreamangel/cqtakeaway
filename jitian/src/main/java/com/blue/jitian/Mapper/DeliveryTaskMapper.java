package com.blue.jitian.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blue.jitian.Entity.DeliveryTask;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface DeliveryTaskMapper extends BaseMapper<DeliveryTask> {
    
    /**
     * 根据订单ID查询配送任务
     */
    @Select("SELECT * FROM delivery_tasks WHERE order_id = #{orderId}")
    DeliveryTask findByOrderId(@Param("orderId") Long orderId);
    
    /**
     * 根据骑手ID查询配送任务列表
     */
    @Select("SELECT * FROM delivery_tasks WHERE rider_id = #{riderId} ORDER BY created_at DESC")
    List<DeliveryTask> findByRiderId(@Param("riderId") Long riderId);
    
    /**
     * 根据状态查询配送任务
     */
    @Select("SELECT * FROM delivery_tasks WHERE status = #{status} ORDER BY created_at DESC")
    List<DeliveryTask> findByStatus(@Param("status") Integer status);
    
    /**
     * 查询待接单的配送任务
     */
    @Select("SELECT * FROM delivery_tasks WHERE status = 0 ORDER BY created_at ASC")
    List<DeliveryTask> findPendingTasks();
    
    /**
     * 根据商家ID查询配送任务
     */
    @Select("SELECT * FROM delivery_tasks WHERE shop_id = #{shopId} ORDER BY created_at DESC")
    List<DeliveryTask> findByShopId(@Param("shopId") Long shopId);
    
    /**
     * 根据用户ID查询配送任务
     */
    @Select("SELECT * FROM delivery_tasks WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<DeliveryTask> findByUserId(@Param("userId") Long userId);
    
    /**
     * 更新配送任务状态
     */
    @Update("UPDATE delivery_tasks SET status = #{status}, updated_at = CURRENT_TIMESTAMP WHERE task_id = #{taskId}")
    int updateStatus(@Param("taskId") Long taskId, @Param("status") Integer status);
    
    /**
     * 骑手接单
     */
    @Update("UPDATE delivery_tasks SET rider_id = #{riderId}, status = 1, accept_time = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP WHERE task_id = #{taskId} AND status = 0")
    int acceptTask(@Param("taskId") Long taskId, @Param("riderId") Long riderId);
    
    /**
     * 确认取餐
     */
    @Update("UPDATE delivery_tasks SET status = 2, pickup_time = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP WHERE task_id = #{taskId} AND status = 1")
    int pickupTask(@Param("taskId") Long taskId);
    
    /**
     * 开始配送
     */
    @Update("UPDATE delivery_tasks SET status = 3, updated_at = CURRENT_TIMESTAMP WHERE task_id = #{taskId} AND status = 2")
    int startDelivery(@Param("taskId") Long taskId);
    
    /**
     * 完成配送
     */
    @Update("UPDATE delivery_tasks SET status = 4, complete_time = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP WHERE task_id = #{taskId} AND status = 3")
    int completeTask(@Param("taskId") Long taskId);
    
    /**
     * 取消配送任务
     */
    @Update("UPDATE delivery_tasks SET status = -1, cancel_time = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP WHERE task_id = #{taskId}")
    int cancelTask(@Param("taskId") Long taskId);
    
    /**
     * 查询骑手进行中的任务
     */
    @Select("SELECT * FROM delivery_tasks WHERE rider_id = #{riderId} AND status IN (1, 2, 3)")
    List<DeliveryTask> findActiveTasksByRiderId(@Param("riderId") Long riderId);
    
    /**
     * 统计骑手完成的任务数量
     */
    @Select("SELECT COUNT(*) FROM delivery_tasks WHERE rider_id = #{riderId} AND status = 4")
    Integer countCompletedTasksByRiderId(@Param("riderId") Long riderId);
    
    /**
     * 统计商家的配送任务数量
     */
    @Select("SELECT COUNT(*) FROM delivery_tasks WHERE shop_id = #{shopId}")
    Integer countTasksByShopId(@Param("shopId") Long shopId);
    
    /**
     * 根据时间范围查询配送任务
     */
    @Select("SELECT * FROM delivery_tasks WHERE created_at BETWEEN #{startTime} AND #{endTime} ORDER BY created_at DESC")
    List<DeliveryTask> findByTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    
    /**
     * 查询骑手在指定时间范围内完成的任务
     */
    @Select("SELECT * FROM delivery_tasks WHERE rider_id = #{riderId} AND status = 4 AND complete_time BETWEEN #{startTime} AND #{endTime}")
    List<DeliveryTask> findCompletedTasksByRiderAndTime(@Param("riderId") Long riderId, 
                                                         @Param("startTime") LocalDateTime startTime, 
                                                         @Param("endTime") LocalDateTime endTime);
    
    /**
     * 根据订单ID列表批量查询配送任务
     */
    @Select("<script>" +
            "SELECT * FROM delivery_tasks WHERE order_id IN " +
            "<foreach collection='orderIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    List<DeliveryTask> findByOrderIds(@Param("orderIds") List<Long> orderIds);
    
    /**
     * 统计各状态的配送任务数量
     */
    @Select("SELECT status, COUNT(*) as count FROM delivery_tasks GROUP BY status")
    @MapKey("status")
    List<java.util.Map<String, Object>> countByStatus();
    
    /**
     * 查询超时未接单的任务（创建超过指定分钟数仍未接单）
     */
    @Select("SELECT * FROM delivery_tasks WHERE status = 0 AND created_at < (CURRENT_TIMESTAMP - INTERVAL '#{minutes} minutes')")
    List<DeliveryTask> findTimeoutPendingTasks(@Param("minutes") Integer minutes);
}
