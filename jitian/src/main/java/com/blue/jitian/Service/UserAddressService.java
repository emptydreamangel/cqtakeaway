package com.blue.jitian.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.jitian.Entity.UserAddress;
import com.blue.jitian.Mapper.UserAddressMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class UserAddressService extends ServiceImpl<UserAddressMapper, UserAddress> {

    /**
     * 根据用户ID查询所有地址
     * @param userId 用户ID
     * @return 地址列表
     */
    public List<UserAddress> getByUserId(Long userId) {
        return this.baseMapper.findByUserId(userId);
    }

    /**
     * 查询用户的默认地址
     * @param userId 用户ID
     * @return 默认地址
     */
    public UserAddress getDefaultAddress(Long userId) {
        return this.baseMapper.findDefaultByUserId(userId);
    }

    /**
     * 设置默认地址
     * @param addressId 地址ID
     * @param userId 用户ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean setDefaultAddress(Long addressId, Long userId) {
        // 验证地址是否属于该用户
        UserAddress address = this.baseMapper.findByIdAndUserId(addressId, userId);
        if (address == null) {
            return false;
        }
        
        // 先取消该用户的所有默认地址
        this.baseMapper.clearDefaultByUserId(userId);
        
        // 再设置新的默认地址
        return this.baseMapper.setDefault(addressId) > 0;
    }

    /**
     * 根据用户ID统计地址数量
     * @param userId 用户ID
     * @return 地址数量
     */
    public long countByUserId(Long userId) {
        return this.baseMapper.countByUserId(userId);
    }

    /**
     * 验证地址是否属于该用户
     * @param addressId 地址ID
     * @param userId 用户ID
     * @return 是否属于
     */
    public boolean belongsToUser(Long addressId, Long userId) {
        return this.baseMapper.findByIdAndUserId(addressId, userId) != null;
    }

    /**
     * 添加地址
     * @param address 地址对象
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean addAddress(UserAddress address) {
        // 如果设置为默认地址，需要先取消其他默认地址
        if (address.getIs_default() != null && address.getIs_default() == 1) {
            this.baseMapper.clearDefaultByUserId(address.getUser_id());
        }
        
        // 如果没有设置is_default，默认为0
        if (address.getIs_default() == null) {
            address.setIs_default(0);
        }
        
        return this.save(address);
    }

    /**
     * 更新地址
     * @param address 地址对象
     * @param userId 用户ID（用于安全验证）
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAddress(UserAddress address, Long userId) {
        // 验证地址是否属于该用户
        if (!belongsToUser(address.getAddress_id(), userId)) {
            return false;
        }
        
        // 如果设置为默认地址，需要先取消其他默认地址
        if (address.getIs_default() != null && address.getIs_default() == 1) {
            this.baseMapper.clearDefaultByUserId(userId);
        }
        
        return this.updateById(address);
    }

    /**
     * 删除地址
     * @param addressId 地址ID
     * @param userId 用户ID（用于安全验证）
     * @return 是否成功
     */
    public boolean deleteAddress(Long addressId, Long userId) {
        // 验证地址是否属于该用户
        if (!belongsToUser(addressId, userId)) {
            return false;
        }
        
        return this.removeById(addressId);
    }

    /**
     * 批量删除地址
     * @param addressIds 地址ID列表
     * @param userId 用户ID（用于安全验证）
     * @return 删除的数量
     */
    public int batchDelete(List<Long> addressIds, Long userId) {
        return this.baseMapper.batchDelete(addressIds, userId);
    }

    /**
     * 根据省市区查询地址
     * @param userId 用户ID
     * @param province 省份
     * @param city 城市
     * @param district 区县
     * @return 地址列表
     */
    public List<UserAddress> getByRegion(Long userId, String province, String city, String district) {
        return this.baseMapper.findByRegion(userId, province, city, district);
    }

    /**
     * 取消默认地址
     * @param userId 用户ID
     * @return 是否成功
     */
    public boolean clearDefaultAddress(Long userId) {
        return this.baseMapper.clearDefaultByUserId(userId) > 0;
    }

    /**
     * 获取用户的第一个地址（如果没有默认地址）
     * @param userId 用户ID
     * @return 地址对象
     */
    public UserAddress getFirstAddress(Long userId) {
        List<UserAddress> addresses = getByUserId(userId);
        return addresses.isEmpty() ? null : addresses.get(0);
    }

    /**
     * 获取用户的有效地址（优先返回默认地址，否则返回第一个）
     * @param userId 用户ID
     * @return 地址对象
     */
    public UserAddress getEffectiveAddress(Long userId) {
        UserAddress defaultAddress = getDefaultAddress(userId);
        return defaultAddress != null ? defaultAddress : getFirstAddress(userId);
    }
}
