package com.blue.jitian.Controller.user;

import com.blue.jitian.Entity.UserAddress;
import com.blue.jitian.Service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 用户地址控制器
 * 提供用户地址相关的REST API接口
 */
@RestController
@RequestMapping("/api/address")
public class UserAddressController {

    @Autowired
    private UserAddressService addressService;

    /**
     * 获取用户的所有地址
     * @param userId 用户ID
     * @return 地址列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserAddress>> getAddressesByUserId(@PathVariable("userId") Long userId) {
        List<UserAddress> addresses = addressService.getByUserId(userId);
        return ResponseEntity.ok(addresses);
    }

    /**
     * 获取用户的默认地址
     * @param userId 用户ID
     * @return 默认地址
     */
    @GetMapping("/user/{userId}/default")
    public ResponseEntity<?> getDefaultAddress(@PathVariable("userId") Long userId) {
        UserAddress address = addressService.getDefaultAddress(userId);
        if (address == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "未找到默认地址"));
        }
        return ResponseEntity.ok(address);
    }

    /**
     * 获取用户的有效地址（优先默认地址）
     * @param userId 用户ID
     * @return 有效地址
     */
    @GetMapping("/user/{userId}/effective")
    public ResponseEntity<?> getEffectiveAddress(@PathVariable("userId") Long userId) {
        UserAddress address = addressService.getEffectiveAddress(userId);
        if (address == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "该用户还没有收货地址"));
        }
        return ResponseEntity.ok(address);
    }

    /**
     * 根据ID获取地址详情
     * @param id 地址ID
     * @return 地址详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getAddressById(@PathVariable("id") Long id) {
        UserAddress address = addressService.getById(id);
        if (address == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "地址不存在"));
        }
        return ResponseEntity.ok(address);
    }

    /**
     * 添加地址
     * @param address 地址对象
     * @return 添加结果
     */
    @PostMapping
    public ResponseEntity<?> addAddress(@RequestBody UserAddress address) {
        // 验证必填字段
        if (address.getUser_id() == null || address.getReceiver_name() == null || 
            address.getReceiver_phone() == null || address.getProvince() == null ||
            address.getCity() == null || address.getDistrict() == null || 
            address.getDetail_address() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "缺少必填字段"));
        }
        
        boolean saved = addressService.addAddress(address);
        if (saved) {
            return ResponseEntity.status(HttpStatus.CREATED).body(address);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "添加地址失败"));
    }

    /**
     * 更新地址
     * @param id 地址ID
     * @param address 地址对象
     * @param userId 用户ID（从请求参数或token中获取）
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable("id") Long id, 
                                           @RequestBody UserAddress address,
                                           @RequestParam("userId") Long userId) {
        address.setAddress_id(id);
        
        boolean updated = addressService.updateAddress(address, userId);
        if (updated) {
            return ResponseEntity.ok(address);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("message", "无权限操作该地址或更新失败"));
    }

    /**
     * 删除地址
     * @param id 地址ID
     * @param userId 用户ID（从请求参数或token中获取）
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable("id") Long id,
                                           @RequestParam("userId") Long userId) {
        boolean deleted = addressService.deleteAddress(id, userId);
        if (deleted) {
            return ResponseEntity.ok(Map.of("message", "删除成功"));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("message", "无权限操作该地址或删除失败"));
    }

    /**
     * 批量删除地址
     * @param request 请求体（包含addressIds和userId）
     * @return 删除结果
     */
    @DeleteMapping("/batch")
    public ResponseEntity<?> batchDeleteAddresses(@RequestBody Map<String, Object> request) {
        @SuppressWarnings("unchecked")
        List<Long> addressIds = (List<Long>) request.get("addressIds");
        Long userId = Long.valueOf(request.get("userId").toString());
        
        if (addressIds == null || addressIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "地址ID列表不能为空"));
        }
        
        int count = addressService.batchDelete(addressIds, userId);
        return ResponseEntity.ok(Map.of("message", "删除成功", "count", count));
    }

    /**
     * 设置默认地址
     * @param id 地址ID
     * @param userId 用户ID（从请求参数或token中获取）
     * @return 设置结果
     */
    @PatchMapping("/{id}/default")
    public ResponseEntity<?> setDefaultAddress(@PathVariable("id") Long id,
                                                @RequestParam("userId") Long userId) {
        boolean success = addressService.setDefaultAddress(id, userId);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "设置默认地址成功"));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("message", "无权限操作该地址或设置失败"));
    }

    /**
     * 取消默认地址
     * @param userId 用户ID
     * @return 取消结果
     */
    @DeleteMapping("/user/{userId}/default")
    public ResponseEntity<?> clearDefaultAddress(@PathVariable("userId") Long userId) {
        boolean success = addressService.clearDefaultAddress(userId);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "取消默认地址成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "取消默认地址失败"));
    }

    /**
     * 统计用户的地址数量
     * @param userId 用户ID
     * @return 地址数量
     */
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Map<String, Long>> countAddresses(@PathVariable("userId") Long userId) {
        long count = addressService.countByUserId(userId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    /**
     * 根据省市区查询地址
     * @param userId 用户ID
     * @param province 省份
     * @param city 城市
     * @param district 区县
     * @return 地址列表
     */
    @GetMapping("/user/{userId}/region")
    public ResponseEntity<List<UserAddress>> getAddressesByRegion(
            @PathVariable("userId") Long userId,
            @RequestParam("province") String province,
            @RequestParam("city") String city,
            @RequestParam("district") String district) {
        List<UserAddress> addresses = addressService.getByRegion(userId, province, city, district);
        return ResponseEntity.ok(addresses);
    }

    /**
     * 验证地址是否属于用户
     * @param id 地址ID
     * @param userId 用户ID
     * @return 验证结果
     */
    @GetMapping("/{id}/verify")
    public ResponseEntity<Map<String, Boolean>> verifyAddress(@PathVariable("id") Long id,
                                                                @RequestParam("userId") Long userId) {
        boolean belongs = addressService.belongsToUser(id, userId);
        return ResponseEntity.ok(Map.of("belongs", belongs));
    }
}
