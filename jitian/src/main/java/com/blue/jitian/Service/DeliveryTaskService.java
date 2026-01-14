package com.blue.jitian.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.jitian.Entity.DeliveryTask;
import com.blue.jitian.Mapper.DeliveryTaskMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DeliveryTaskService extends ServiceImpl<DeliveryTaskMapper, DeliveryTask> {
    
    /**
     * 创建配送任务
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean createTask(DeliveryTask task) {
        // 设置初始状态
        if (task.getStatus() == null) {
            task.setStatus(0); // 待接单
        }
        return this.save(task);
    }
    
    /**
     * 根据订单ID查询配送任务
     */
    public DeliveryTask getByOrderId(Long orderId) {
        return this.baseMapper.findByOrderId(orderId);
    }
    
    /**
     * 根据骑手ID查询配送任务列表
     */
    public List<DeliveryTask> getTasksByRiderId(Long riderId) {
        return this.baseMapper.findByRiderId(riderId);
    }
    
    /**
     * 根据状态查询配送任务
     */
    public List<DeliveryTask> getTasksByStatus(Integer status) {
        return this.baseMapper.findByStatus(status);
    }
    
    /**
     * 查询待接单的配送任务
     */
    public List<DeliveryTask> getPendingTasks() {
        return this.baseMapper.findPendingTasks();
    }
    
    /**
     * 根据商家ID查询配送任务
     */
    public List<DeliveryTask> getTasksByShopId(Long shopId) {
        return this.baseMapper.findByShopId(shopId);
    }
    
    /**
     * 根据用户ID查询配送任务
     */
    public List<DeliveryTask> getTasksByUserId(Long userId) {
        return this.baseMapper.findByUserId(userId);
    }
    
    /**
     * 骑手接单
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean acceptTask(Long taskId, Long riderId) {
        // 检查任务是否存在
        DeliveryTask task = this.getById(taskId);
        if (task == null) {
            log.warn("配送任务不存在: taskId={}", taskId);
            return false;
        }
        
        // 检查任务状态
        if (task.getStatus() != 0) {
            log.warn("配送任务状态不正确，无法接单: taskId={}, status={}", taskId, task.getStatus());
            return false;
        }
        
        int result = this.baseMapper.acceptTask(taskId, riderId);
        if (result > 0) {
            log.info("骑手接单成功: taskId={}, riderId={}", taskId, riderId);
            return true;
        }
        return false;
    }
    
    /**
     * 确认取餐
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean pickupTask(Long taskId) {
        DeliveryTask task = this.getById(taskId);
        if (task == null) {
            log.warn("配送任务不存在: taskId={}", taskId);
            return false;
        }
        
        if (task.getStatus() != 1) {
            log.warn("配送任务状态不正确，无法取餐: taskId={}, status={}", taskId, task.getStatus());
            return false;
        }
        
        int result = this.baseMapper.pickupTask(taskId);
        if (result > 0) {
            log.info("取餐成功: taskId={}", taskId);
            return true;
        }
        return false;
    }
    
    /**
     * 开始配送
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean startDelivery(Long taskId) {
        DeliveryTask task = this.getById(taskId);
        if (task == null) {
            log.warn("配送任务不存在: taskId={}", taskId);
            return false;
        }
        
        if (task.getStatus() != 2) {
            log.warn("配送任务状态不正确，无法开始配送: taskId={}, status={}", taskId, task.getStatus());
            return false;
        }
        
        int result = this.baseMapper.startDelivery(taskId);
        if (result > 0) {
            log.info("开始配送: taskId={}", taskId);
            return true;
        }
        return false;
    }
    
    /**
     * 完成配送
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean completeTask(Long taskId) {
        DeliveryTask task = this.getById(taskId);
        if (task == null) {
            log.warn("配送任务不存在: taskId={}", taskId);
            return false;
        }
        
        if (task.getStatus() != 3) {
            log.warn("配送任务状态不正确，无法完成: taskId={}, status={}", taskId, task.getStatus());
            return false;
        }
        
        int result = this.baseMapper.completeTask(taskId);
        if (result > 0) {
            log.info("配送完成: taskId={}", taskId);
            return true;
        }
        return false;
    }
    
    /**
     * 取消配送任务
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelTask(Long taskId) {
        DeliveryTask task = this.getById(taskId);
        if (task == null) {
            log.warn("配送任务不存在: taskId={}", taskId);
            return false;
        }
        
        int result = this.baseMapper.cancelTask(taskId);
        if (result > 0) {
            log.info("配送任务已取消: taskId={}", taskId);
            return true;
        }
        return false;
    }
    
    /**
     * 查询骑手进行中的任务
     */
    public List<DeliveryTask> getActiveTasksByRiderId(Long riderId) {
        return this.baseMapper.findActiveTasksByRiderId(riderId);
    }
    
    /**
     * 统计骑手完成的任务数量
     */
    public Integer countCompletedTasksByRiderId(Long riderId) {
        return this.baseMapper.countCompletedTasksByRiderId(riderId);
    }
    
    /**
     * 统计商家的配送任务数量
     */
    public Integer countTasksByShopId(Long shopId) {
        return this.baseMapper.countTasksByShopId(shopId);
    }
    
    /**
     * 根据时间范围查询配送任务
     */
    public List<DeliveryTask> getTasksByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return this.baseMapper.findByTimeRange(startTime, endTime);
    }
    
    /**
     * 查询骑手在指定时间范围内完成的任务
     */
    public List<DeliveryTask> getCompletedTasksByRiderAndTime(Long riderId, LocalDateTime startTime, LocalDateTime endTime) {
        return this.baseMapper.findCompletedTasksByRiderAndTime(riderId, startTime, endTime);
    }
    
    /**
     * 根据订单ID列表批量查询配送任务
     */
    public List<DeliveryTask> getTasksByOrderIds(List<Long> orderIds) {
        if (orderIds == null || orderIds.isEmpty()) {
            return List.of();
        }
        return this.baseMapper.findByOrderIds(orderIds);
    }
    
    /**
     * 统计各状态的配送任务数量
     */
    public List<Map<String, Object>> countByStatus() {
        return this.baseMapper.countByStatus();
    }
    
    /**
     * 查询超时未接单的任务
     */
    public List<DeliveryTask> getTimeoutPendingTasks(Integer minutes) {
        return this.baseMapper.findTimeoutPendingTasks(minutes);
    }
    
    /**
     * 分页查询配送任务
     */
    public Page<DeliveryTask> getTasksPage(Integer pageNum, Integer pageSize, Integer status, Long riderId, Long shopId, Long userId) {
        Page<DeliveryTask> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<DeliveryTask> queryWrapper = new LambdaQueryWrapper<>();
        
        if (status != null) {
            queryWrapper.eq(DeliveryTask::getStatus, status);
        }
        if (riderId != null) {
            queryWrapper.eq(DeliveryTask::getRider_id, riderId);
        }
        if (shopId != null) {
            queryWrapper.eq(DeliveryTask::getShop_id, shopId);
        }
        if (userId != null) {
            queryWrapper.eq(DeliveryTask::getUser_id, userId);
        }
        
        queryWrapper.orderByDesc(DeliveryTask::getCreated_at);
        
        return this.page(page, queryWrapper);
    }
    
    /**
     * 更新配送任务状态
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Long taskId, Integer status) {
        int result = this.baseMapper.updateStatus(taskId, status);
        return result > 0;
    }
    
    /**
     * 更新配送任务信息
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTask(DeliveryTask task) {
        return this.updateById(task);
    }
    
    /**
     * 删除配送任务
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTask(Long taskId) {
        DeliveryTask task = this.getById(taskId);
        if (task == null) {
            log.warn("配送任务不存在: taskId={}", taskId);
            return false;
        }
        
        return this.removeById(taskId);
    }
}
