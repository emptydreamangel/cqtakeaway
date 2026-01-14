package com.blue.jitian.Controller.delivery;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blue.jitian.Entity.DeliveryRider;
import com.blue.jitian.Service.DeliveryRiderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/delivery-rider")
@Slf4j
public class DeliveryRiderController {
    
    @Autowired
    private DeliveryRiderService deliveryRiderService;
    
    /**
     * 骑手注册
     */
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody DeliveryRider rider) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = deliveryRiderService.register(rider);
            if (success) {
                result.put("code", 200);
                result.put("message", "注册成功");
                result.put("data", rider);
            } else {
                result.put("code", 400);
                result.put("message", "注册失败，手机号或身份证号已存在");
            }
        } catch (Exception e) {
            log.error("骑手注册失败", e);
            result.put("code", 500);
            result.put("message", "注册失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 骑手登录
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestParam String phone, @RequestParam String passwordHash) {
        Map<String, Object> result = new HashMap<>();
        try {
            DeliveryRider rider = deliveryRiderService.login(phone, passwordHash);
            if (rider != null) {
                result.put("code", 200);
                result.put("message", "登录成功");
                result.put("data", rider);
            } else {
                result.put("code", 401);
                result.put("message", "手机号或密码错误");
            }
        } catch (Exception e) {
            log.error("骑手登录失败", e);
            result.put("code", 500);
            result.put("message", "登录失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据ID获取骑手信息
     */
    @GetMapping("/{riderId}")
    public Map<String, Object> getRider(@PathVariable Long riderId) {
        Map<String, Object> result = new HashMap<>();
        try {
            DeliveryRider rider = deliveryRiderService.getById(riderId);
            if (rider != null) {
                result.put("code", 200);
                result.put("data", rider);
            } else {
                result.put("code", 404);
                result.put("message", "骑手不存在");
            }
        } catch (Exception e) {
            log.error("查询骑手失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据手机号获取骑手信息
     */
    @GetMapping("/phone/{phone}")
    public Map<String, Object> getRiderByPhone(@PathVariable String phone) {
        Map<String, Object> result = new HashMap<>();
        try {
            DeliveryRider rider = deliveryRiderService.getByPhone(phone);
            if (rider != null) {
                result.put("code", 200);
                result.put("data", rider);
            } else {
                result.put("code", 404);
                result.put("message", "骑手不存在");
            }
        } catch (Exception e) {
            log.error("查询骑手失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 分页查询骑手
     */
    @GetMapping("/page")
    public Map<String, Object> getRidersPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String riderName,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer isOnline) {
        Map<String, Object> result = new HashMap<>();
        try {
            Page<DeliveryRider> page = deliveryRiderService.getRidersPage(pageNum, pageSize, riderName, status, isOnline);
            result.put("code", 200);
            result.put("data", page);
        } catch (Exception e) {
            log.error("分页查询骑手失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 获取所有可接单的骑手
     */
    @GetMapping("/available")
    public Map<String, Object> getAvailableRiders() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<DeliveryRider> riders = deliveryRiderService.getAvailableRiders();
            result.put("code", 200);
            result.put("data", riders);
        } catch (Exception e) {
            log.error("查询可接单骑手失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据状态查询骑手
     */
    @GetMapping("/status/{status}")
    public Map<String, Object> getRidersByStatus(@PathVariable Integer status) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<DeliveryRider> riders = deliveryRiderService.getRidersByStatus(status);
            result.put("code", 200);
            result.put("data", riders);
        } catch (Exception e) {
            log.error("根据状态查询骑手失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据在线状态查询骑手
     */
    @GetMapping("/online/{isOnline}")
    public Map<String, Object> getRidersByOnlineStatus(@PathVariable Integer isOnline) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<DeliveryRider> riders = deliveryRiderService.getRidersByOnlineStatus(isOnline);
            result.put("code", 200);
            result.put("data", riders);
        } catch (Exception e) {
            log.error("根据在线状态查询骑手失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据车辆类型查询骑手
     */
    @GetMapping("/vehicle-type/{vehicleType}")
    public Map<String, Object> getRidersByVehicleType(@PathVariable Integer vehicleType) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<DeliveryRider> riders = deliveryRiderService.getRidersByVehicleType(vehicleType);
            result.put("code", 200);
            result.put("data", riders);
        } catch (Exception e) {
            log.error("根据车辆类型查询骑手失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 查找附近的可接单骑手
     */
    @GetMapping("/nearby")
    public Map<String, Object> findNearbyRiders(
            @RequestParam BigDecimal longitude,
            @RequestParam BigDecimal latitude,
            @RequestParam(defaultValue = "0.05") BigDecimal range) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<DeliveryRider> riders = deliveryRiderService.findNearbyAvailableRiders(longitude, latitude, range);
            result.put("code", 200);
            result.put("data", riders);
        } catch (Exception e) {
            log.error("查找附近骑手失败", e);
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 更新骑手在线状态
     */
    @PutMapping("/{riderId}/online-status")
    public Map<String, Object> updateOnlineStatus(@PathVariable Long riderId, @RequestParam Integer isOnline) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = deliveryRiderService.updateOnlineStatus(riderId, isOnline);
            if (success) {
                result.put("code", 200);
                result.put("message", "在线状态更新成功");
            } else {
                result.put("code", 400);
                result.put("message", "更新失败");
            }
        } catch (Exception e) {
            log.error("更新骑手在线状态失败", e);
            result.put("code", 500);
            result.put("message", "更新失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 更新骑手状态
     */
    @PutMapping("/{riderId}/status")
    public Map<String, Object> updateStatus(@PathVariable Long riderId, @RequestParam Integer status) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = deliveryRiderService.updateStatus(riderId, status);
            if (success) {
                result.put("code", 200);
                result.put("message", "状态更新成功");
            } else {
                result.put("code", 400);
                result.put("message", "更新失败");
            }
        } catch (Exception e) {
            log.error("更新骑手状态失败", e);
            result.put("code", 500);
            result.put("message", "更新失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 更新骑手位置
     */
    @PutMapping("/{riderId}/location")
    public Map<String, Object> updateLocation(
            @PathVariable Long riderId,
            @RequestParam BigDecimal longitude,
            @RequestParam BigDecimal latitude) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = deliveryRiderService.updateLocation(riderId, longitude, latitude);
            if (success) {
                result.put("code", 200);
                result.put("message", "位置更新成功");
            } else {
                result.put("code", 400);
                result.put("message", "更新失败");
            }
        } catch (Exception e) {
            log.error("更新骑手位置失败", e);
            result.put("code", 500);
            result.put("message", "更新失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 更新骑手余额
     */
    @PutMapping("/{riderId}/balance")
    public Map<String, Object> updateBalance(@PathVariable Long riderId, @RequestParam BigDecimal amount) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = deliveryRiderService.updateBalance(riderId, amount);
            if (success) {
                result.put("code", 200);
                result.put("message", "余额更新成功");
            } else {
                result.put("code", 400);
                result.put("message", "更新失败，可能余额不足");
            }
        } catch (Exception e) {
            log.error("更新骑手余额失败", e);
            result.put("code", 500);
            result.put("message", "更新失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 更新骑手信息
     */
    @PutMapping("/{riderId}")
    public Map<String, Object> updateRider(@PathVariable Long riderId, @RequestBody DeliveryRider rider) {
        Map<String, Object> result = new HashMap<>();
        try {
            rider.setRider_id(riderId);
            boolean success = deliveryRiderService.updateRiderInfo(rider);
            if (success) {
                result.put("code", 200);
                result.put("message", "更新成功");
                result.put("data", rider);
            } else {
                result.put("code", 400);
                result.put("message", "更新失败");
            }
        } catch (Exception e) {
            log.error("更新骑手信息失败", e);
            result.put("code", 500);
            result.put("message", "更新失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 删除骑手
     */
    @DeleteMapping("/{riderId}")
    public Map<String, Object> deleteRider(@PathVariable Long riderId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = deliveryRiderService.deleteRider(riderId);
            if (success) {
                result.put("code", 200);
                result.put("message", "删除成功");
            } else {
                result.put("code", 404);
                result.put("message", "骑手不存在");
            }
        } catch (Exception e) {
            log.error("删除骑手失败", e);
            result.put("code", 500);
            result.put("message", "删除失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 批量更新骑手在线状态
     */
    @PutMapping("/batch/online-status")
    public Map<String, Object> batchUpdateOnlineStatus(@RequestParam List<Long> riderIds, @RequestParam Integer isOnline) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = deliveryRiderService.batchUpdateOnlineStatus(riderIds, isOnline);
            if (success) {
                result.put("code", 200);
                result.put("message", "批量更新成功");
            } else {
                result.put("code", 400);
                result.put("message", "更新失败");
            }
        } catch (Exception e) {
            log.error("批量更新骑手在线状态失败", e);
            result.put("code", 500);
            result.put("message", "更新失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 统计在线骑手数量
     */
    @GetMapping("/stats/online-count")
    public Map<String, Object> countOnlineRiders() {
        Map<String, Object> result = new HashMap<>();
        try {
            Integer count = deliveryRiderService.countOnlineRiders();
            result.put("code", 200);
            result.put("data", count);
        } catch (Exception e) {
            log.error("统计在线骑手失败", e);
            result.put("code", 500);
            result.put("message", "统计失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 统计可接单骑手数量
     */
    @GetMapping("/stats/available-count")
    public Map<String, Object> countAvailableRiders() {
        Map<String, Object> result = new HashMap<>();
        try {
            Integer count = deliveryRiderService.countAvailableRiders();
            result.put("code", 200);
            result.put("data", count);
        } catch (Exception e) {
            log.error("统计可接单骑手失败", e);
            result.put("code", 500);
            result.put("message", "统计失败：" + e.getMessage());
        }
        return result;
    }
}
