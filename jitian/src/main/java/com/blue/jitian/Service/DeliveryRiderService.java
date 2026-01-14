package com.blue.jitian.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.jitian.Entity.DeliveryRider;
import com.blue.jitian.Mapper.DeliveryRiderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class DeliveryRiderService extends ServiceImpl<DeliveryRiderMapper, DeliveryRider> {
    
    /**
     * 根据手机号查询骑手
     */
    public DeliveryRider getByPhone(String phone) {
        return this.baseMapper.findByPhone(phone);
    }
    
    /**
     * 骑手登录验证
     */
    public DeliveryRider login(String phone, String passwordHash) {
        return this.baseMapper.findByPhoneAndPassword(phone, passwordHash);
    }
    
    /**
     * 注册骑手
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean register(DeliveryRider rider) {
        // 检查手机号是否已存在
        DeliveryRider existing = this.baseMapper.findByPhone(rider.getPhone());
        if (existing != null) {
            log.warn("注册失败，手机号已存在: {}", rider.getPhone());
            return false;
        }
        
        // 检查身份证号是否已存在
        if (rider.getId_card() != null) {
            DeliveryRider existingIdCard = this.baseMapper.findByIdCard(rider.getId_card());
            if (existingIdCard != null) {
                log.warn("注册失败，身份证号已存在: {}", rider.getId_card());
                return false;
            }
        }
        
        // 设置默认值
        if (rider.getBalance() == null) {
            rider.setBalance(BigDecimal.ZERO);
        }
        if (rider.getStatus() == null) {
            rider.setStatus(0); // 默认休息状态
        }
        if (rider.getIs_online() == null) {
            rider.setIs_online(0); // 默认离线
        }
        
        return this.save(rider);
    }
    
    /**
     * 更新骑手在线状态
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOnlineStatus(Long riderId, Integer isOnline) {
        int result = this.baseMapper.updateOnlineStatus(riderId, isOnline);
        if (result > 0) {
            log.info("骑手 {} 在线状态更新为: {}", riderId, isOnline);
            // 如果设置为离线，同时更新状态为休息
            if (isOnline == 0) {
                this.baseMapper.updateStatus(riderId, 0);
            }
        }
        return result > 0;
    }
    
    /**
     * 更新骑手状态
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Long riderId, Integer status) {
        int result = this.baseMapper.updateStatus(riderId, status);
        if (result > 0) {
            log.info("骑手 {} 状态更新为: {}", riderId, status);
        }
        return result > 0;
    }
    
    /**
     * 更新骑手位置
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateLocation(Long riderId, BigDecimal longitude, BigDecimal latitude) {
        int result = this.baseMapper.updateLocation(riderId, longitude, latitude);
        return result > 0;
    }
    
    /**
     * 更新骑手余额（增加或减少）
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBalance(Long riderId, BigDecimal amount) {
        // 如果是扣款，先检查余额是否足够
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            DeliveryRider rider = this.getById(riderId);
            if (rider == null || rider.getBalance().add(amount).compareTo(BigDecimal.ZERO) < 0) {
                log.warn("余额不足，无法扣款: riderId={}, amount={}", riderId, amount);
                return false;
            }
        }
        
        int result = this.baseMapper.updateBalance(riderId, amount);
        if (result > 0) {
            log.info("骑手 {} 余额变动: {}", riderId, amount);
        }
        return result > 0;
    }
    
    /**
     * 获取所有可接单的骑手
     */
    public List<DeliveryRider> getAvailableRiders() {
        return this.baseMapper.findAvailableRiders();
    }
    
    /**
     * 根据状态查询骑手
     */
    public List<DeliveryRider> getRidersByStatus(Integer status) {
        return this.baseMapper.findByStatus(status);
    }
    
    /**
     * 根据在线状态查询骑手
     */
    public List<DeliveryRider> getRidersByOnlineStatus(Integer isOnline) {
        return this.baseMapper.findByOnlineStatus(isOnline);
    }
    
    /**
     * 根据车辆类型查询骑手
     */
    public List<DeliveryRider> getRidersByVehicleType(Integer vehicleType) {
        return this.baseMapper.findByVehicleType(vehicleType);
    }
    
    /**
     * 查找附近的可接单骑手
     */
    public List<DeliveryRider> findNearbyAvailableRiders(BigDecimal longitude, BigDecimal latitude, BigDecimal range) {
        // 简单的矩形范围查询（实际应使用PostGIS的地理位置函数）
        BigDecimal minLon = longitude.subtract(range);
        BigDecimal maxLon = longitude.add(range);
        BigDecimal minLat = latitude.subtract(range);
        BigDecimal maxLat = latitude.add(range);
        
        return this.baseMapper.findNearbyRiders(minLon, maxLon, minLat, maxLat);
    }
    
    /**
     * 统计在线骑手数量
     */
    public Integer countOnlineRiders() {
        return this.baseMapper.countOnlineRiders();
    }
    
    /**
     * 统计可接单骑手数量
     */
    public Integer countAvailableRiders() {
        return this.baseMapper.countAvailableRiders();
    }
    
    /**
     * 分页查询骑手
     */
    public Page<DeliveryRider> getRidersPage(Integer pageNum, Integer pageSize, String riderName, Integer status, Integer isOnline) {
        Page<DeliveryRider> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<DeliveryRider> queryWrapper = new LambdaQueryWrapper<>();
        
        if (riderName != null && !riderName.isEmpty()) {
            queryWrapper.like(DeliveryRider::getRider_name, riderName);
        }
        if (status != null) {
            queryWrapper.eq(DeliveryRider::getStatus, status);
        }
        if (isOnline != null) {
            queryWrapper.eq(DeliveryRider::getIs_online, isOnline);
        }
        
        queryWrapper.orderByDesc(DeliveryRider::getCreated_at);
        
        return this.page(page, queryWrapper);
    }
    
    /**
     * 批量更新骑手在线状态
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateOnlineStatus(List<Long> riderIds, Integer isOnline) {
        if (riderIds == null || riderIds.isEmpty()) {
            return false;
        }
        
        int result = this.baseMapper.batchUpdateOnlineStatus(riderIds, isOnline);
        if (result > 0) {
            log.info("批量更新骑手在线状态: count={}, isOnline={}", result, isOnline);
        }
        return result > 0;
    }
    
    /**
     * 更新骑手个人信息
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRiderInfo(DeliveryRider rider) {
        return this.updateById(rider);
    }
    
    /**
     * 删除骑手
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRider(Long riderId) {
        DeliveryRider rider = this.getById(riderId);
        if (rider == null) {
            log.warn("骑手不存在: {}", riderId);
            return false;
        }
        
        return this.removeById(riderId);
    }
}
