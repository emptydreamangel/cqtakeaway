package com.blue.jitian.Controller.delivery;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blue.jitian.Entity.DeliveryTask;
import com.blue.jitian.Service.DeliveryTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/delivery-task")
@Slf4j
public class DeliveryTaskController {
    
    @Autowired
    private DeliveryTaskService deliveryTaskService;
    
    /**
     * 创建配送任务
     */
    @PostMapping
    public Map<String, Object> createTask(@RequestBody DeliveryTask task) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = deliveryTaskService.createTask(task);
            if (success) {
                result.put("code", 200);
                result.put("message", "配送任务创建成功");
                result.put("data", task);
            } else {
                result.put("code", 400);
                result.put("message", "创建失败");
            }
        } catch (Exception e) {
            log.error("创建配送任务失败", e);
            result.put("code", 500);
            result.put("message", "创建失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据ID获取配送任务
     */
    @GetMapping("/{taskId}")
    public Map<String, Object> getTask(@PathVariable Long taskId) {
        Map<String, Object> result = new HashMap<>();
        try {
            DeliveryTask task = deliveryTaskService.getById(taskId);
            if (task != null) {
                result.put("code", 200);
                result.put("data", task);
            } else {
                result.put("code", 404);
                result.put("message", "配送任务不存在");
            }
        } catch (Exception e) {
            log.error("查询配送任务失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据订单ID获取配送任务
     */
    @GetMapping("/order/{orderId}")
    public Map<String, Object> getTaskByOrderId(@PathVariable Long orderId) {
        Map<String, Object> result = new HashMap<>();
        try {
            DeliveryTask task = deliveryTaskService.getByOrderId(orderId);
            if (task != null) {
                result.put("code", 200);
                result.put("data", task);
            } else {
                result.put("code", 404);
                result.put("message", "配送任务不存在");
            }
        } catch (Exception e) {
            log.error("查询配送任务失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 分页查询配送任务
     */
    @GetMapping("/page")
    public Map<String, Object> getTasksPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long riderId,
            @RequestParam(required = false) Long shopId,
            @RequestParam(required = false) Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Page<DeliveryTask> page = deliveryTaskService.getTasksPage(pageNum, pageSize, status, riderId, shopId, userId);
            result.put("code", 200);
            result.put("data", page);
        } catch (Exception e) {
            log.error("分页查询配送任务失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据骑手ID查询配送任务
     */
    @GetMapping("/rider/{riderId}")
    public Map<String, Object> getTasksByRiderId(@PathVariable Long riderId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<DeliveryTask> tasks = deliveryTaskService.getTasksByRiderId(riderId);
            result.put("code", 200);
            result.put("data", tasks);
        } catch (Exception e) {
            log.error("查询骑手配送任务失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 查询骑手进行中的任务
     */
    @GetMapping("/rider/{riderId}/active")
    public Map<String, Object> getActiveTasksByRiderId(@PathVariable Long riderId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<DeliveryTask> tasks = deliveryTaskService.getActiveTasksByRiderId(riderId);
            result.put("code", 200);
            result.put("data", tasks);
        } catch (Exception e) {
            log.error("查询骑手进行中任务失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据商家ID查询配送任务
     */
    @GetMapping("/shop/{shopId}")
    public Map<String, Object> getTasksByShopId(@PathVariable Long shopId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<DeliveryTask> tasks = deliveryTaskService.getTasksByShopId(shopId);
            result.put("code", 200);
            result.put("data", tasks);
        } catch (Exception e) {
            log.error("查询商家配送任务失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据用户ID查询配送任务
     */
    @GetMapping("/user/{userId}")
    public Map<String, Object> getTasksByUserId(@PathVariable Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<DeliveryTask> tasks = deliveryTaskService.getTasksByUserId(userId);
            result.put("code", 200);
            result.put("data", tasks);
        } catch (Exception e) {
            log.error("查询用户配送任务失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据状态查询配送任务
     */
    @GetMapping("/status/{status}")
    public Map<String, Object> getTasksByStatus(@PathVariable Integer status) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<DeliveryTask> tasks = deliveryTaskService.getTasksByStatus(status);
            result.put("code", 200);
            result.put("data", tasks);
        } catch (Exception e) {
            log.error("根据状态查询配送任务失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 查询待接单的配送任务
     */
    @GetMapping("/pending")
    public Map<String, Object> getPendingTasks() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<DeliveryTask> tasks = deliveryTaskService.getPendingTasks();
            result.put("code", 200);
            result.put("data", tasks);
        } catch (Exception e) {
            log.error("查询待接单任务失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 骑手接单
     */
    @PutMapping("/{taskId}/accept")
    public Map<String, Object> acceptTask(@PathVariable Long taskId, @RequestParam Long riderId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = deliveryTaskService.acceptTask(taskId, riderId);
            if (success) {
                result.put("code", 200);
                result.put("message", "接单成功");
            } else {
                result.put("code", 400);
                result.put("message", "接单失败，任务状态不正确或已被接单");
            }
        } catch (Exception e) {
            log.error("骑手接单失败", e);
            result.put("code", 500);
            result.put("message", "接单失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 确认取餐
     */
    @PutMapping("/{taskId}/pickup")
    public Map<String, Object> pickupTask(@PathVariable Long taskId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = deliveryTaskService.pickupTask(taskId);
            if (success) {
                result.put("code", 200);
                result.put("message", "取餐成功");
            } else {
                result.put("code", 400);
                result.put("message", "取餐失败，任务状态不正确");
            }
        } catch (Exception e) {
            log.error("确认取餐失败", e);
            result.put("code", 500);
            result.put("message", "取餐失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 开始配送
     */
    @PutMapping("/{taskId}/start-delivery")
    public Map<String, Object> startDelivery(@PathVariable Long taskId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = deliveryTaskService.startDelivery(taskId);
            if (success) {
                result.put("code", 200);
                result.put("message", "开始配送");
            } else {
                result.put("code", 400);
                result.put("message", "操作失败，任务状态不正确");
            }
        } catch (Exception e) {
            log.error("开始配送失败", e);
            result.put("code", 500);
            result.put("message", "操作失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 完成配送
     */
    @PutMapping("/{taskId}/complete")
    public Map<String, Object> completeTask(@PathVariable Long taskId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = deliveryTaskService.completeTask(taskId);
            if (success) {
                result.put("code", 200);
                result.put("message", "配送完成");
            } else {
                result.put("code", 400);
                result.put("message", "操作失败，任务状态不正确");
            }
        } catch (Exception e) {
            log.error("完成配送失败", e);
            result.put("code", 500);
            result.put("message", "操作失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 取消配送任务
     */
    @PutMapping("/{taskId}/cancel")
    public Map<String, Object> cancelTask(@PathVariable Long taskId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = deliveryTaskService.cancelTask(taskId);
            if (success) {
                result.put("code", 200);
                result.put("message", "任务已取消");
            } else {
                result.put("code", 400);
                result.put("message", "取消失败");
            }
        } catch (Exception e) {
            log.error("取消配送任务失败", e);
            result.put("code", 500);
            result.put("message", "取消失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 更新配送任务状态
     */
    @PutMapping("/{taskId}/status")
    public Map<String, Object> updateStatus(@PathVariable Long taskId, @RequestParam Integer status) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = deliveryTaskService.updateStatus(taskId, status);
            if (success) {
                result.put("code", 200);
                result.put("message", "状态更新成功");
            } else {
                result.put("code", 400);
                result.put("message", "更新失败");
            }
        } catch (Exception e) {
            log.error("更新配送任务状态失败", e);
            result.put("code", 500);
            result.put("message", "更新失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 更新配送任务信息
     */
    @PutMapping("/{taskId}")
    public Map<String, Object> updateTask(@PathVariable Long taskId, @RequestBody DeliveryTask task) {
        Map<String, Object> result = new HashMap<>();
        try {
            task.setTask_id(taskId);
            boolean success = deliveryTaskService.updateTask(task);
            if (success) {
                result.put("code", 200);
                result.put("message", "更新成功");
                result.put("data", task);
            } else {
                result.put("code", 400);
                result.put("message", "更新失败");
            }
        } catch (Exception e) {
            log.error("更新配送任务失败", e);
            result.put("code", 500);
            result.put("message", "更新失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 删除配送任务
     */
    @DeleteMapping("/{taskId}")
    public Map<String, Object> deleteTask(@PathVariable Long taskId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = deliveryTaskService.deleteTask(taskId);
            if (success) {
                result.put("code", 200);
                result.put("message", "删除成功");
            } else {
                result.put("code", 404);
                result.put("message", "配送任务不存在");
            }
        } catch (Exception e) {
            log.error("删除配送任务失败", e);
            result.put("code", 500);
            result.put("message", "删除失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 统计骑手完成的任务数量
     */
    @GetMapping("/stats/rider/{riderId}/completed")
    public Map<String, Object> countCompletedTasksByRiderId(@PathVariable Long riderId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Integer count = deliveryTaskService.countCompletedTasksByRiderId(riderId);
            result.put("code", 200);
            result.put("data", count);
        } catch (Exception e) {
            log.error("统计骑手完成任务数失败", e);
            result.put("code", 500);
            result.put("message", "统计失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 统计商家的配送任务数量
     */
    @GetMapping("/stats/shop/{shopId}")
    public Map<String, Object> countTasksByShopId(@PathVariable Long shopId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Integer count = deliveryTaskService.countTasksByShopId(shopId);
            result.put("code", 200);
            result.put("data", count);
        } catch (Exception e) {
            log.error("统计商家配送任务数失败", e);
            result.put("code", 500);
            result.put("message", "统计失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 统计各状态的配送任务数量
     */
    @GetMapping("/stats/status-count")
    public Map<String, Object> countByStatus() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> stats = deliveryTaskService.countByStatus();
            result.put("code", 200);
            result.put("data", stats);
        } catch (Exception e) {
            log.error("统计各状态任务数失败", e);
            result.put("code", 500);
            result.put("message", "统计失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据时间范围查询配送任务
     */
    @GetMapping("/time-range")
    public Map<String, Object> getTasksByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<DeliveryTask> tasks = deliveryTaskService.getTasksByTimeRange(startTime, endTime);
            result.put("code", 200);
            result.put("data", tasks);
        } catch (Exception e) {
            log.error("按时间范围查询配送任务失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 查询骑手在指定时间范围内完成的任务
     */
    @GetMapping("/rider/{riderId}/completed/time-range")
    public Map<String, Object> getCompletedTasksByRiderAndTime(
            @PathVariable Long riderId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<DeliveryTask> tasks = deliveryTaskService.getCompletedTasksByRiderAndTime(riderId, startTime, endTime);
            result.put("code", 200);
            result.put("data", tasks);
        } catch (Exception e) {
            log.error("查询骑手完成任务失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据订单ID列表批量查询配送任务
     */
    @PostMapping("/batch/query")
    public Map<String, Object> getTasksByOrderIds(@RequestBody List<Long> orderIds) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<DeliveryTask> tasks = deliveryTaskService.getTasksByOrderIds(orderIds);
            result.put("code", 200);
            result.put("data", tasks);
        } catch (Exception e) {
            log.error("批量查询配送任务失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 查询超时未接单的任务
     */
    @GetMapping("/timeout")
    public Map<String, Object> getTimeoutPendingTasks(@RequestParam(defaultValue = "30") Integer minutes) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<DeliveryTask> tasks = deliveryTaskService.getTimeoutPendingTasks(minutes);
            result.put("code", 200);
            result.put("data", tasks);
        } catch (Exception e) {
            log.error("查询超时未接单任务失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
}
