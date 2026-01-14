package com.blue.jitian.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.jitian.Entity.User;
import com.blue.jitian.Mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    /**
     * 根据手机号查询用户
     * @param phone 手机号
     * @return User对象，如果不存在则返回null
     */
    public User getByPhone(String phone) {
        if (!StringUtils.hasText(phone)) {
            return null;
        }
        return this.lambdaQuery()
                .eq(User::getPhone, phone)
                .one();
    }

    /**
     * 验证用户登录（通过手机号）
     * @param phone 手机号
     * @param passwordHash 密码哈希值
     * @return 验证成功返回User对象，否则返回null
     */
    public User validateLogin(String phone, String passwordHash) {
        if (!StringUtils.hasText(phone) || !StringUtils.hasText(passwordHash)) {
            return null;
        }
        return this.lambdaQuery()
                .eq(User::getPhone, phone)
                .eq(User::getPassword_hash, passwordHash)
                .eq(User::getStatus, 1) // 只查询状态为正常的用户
                .one();
    }

    /**
     * 更新最后登录时间
     * @param userId 用户ID
     * @return 更新是否成功
     */
    public boolean updateLastLoginTime(Long userId) {
        User user = new User();
        user.setUser_id(userId);
        user.setLast_login_time(LocalDateTime.now());
        return this.updateById(user);
    }

    /**
     * 根据状态查询用户列表
     * @param status 状态（1：正常，0：禁用）
     * @return 用户列表
     */
    public List<User> getByStatus(int status) {
        return this.lambdaQuery()
                .eq(User::getStatus, status)
                .list();
    }

    /**
     * 分页查询用户
     * @param current 当前页
     * @param size 每页大小
     * @param nickname 昵称（模糊查询，可选）
     * @param phone 手机号（可选）
     * @param gender 性别（可选）
     * @param status 状态（可选）
     * @return 分页结果
     */
    public Page<User> getUserPage(long current, long size, String nickname, 
                                   String phone, Integer gender, Integer status) {
        Page<User> page = new Page<>(current, size);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(nickname)) {
            wrapper.like(User::getNickname, nickname);
        }
        if (StringUtils.hasText(phone)) {
            wrapper.eq(User::getPhone, phone);
        }
        if (gender != null) {
            wrapper.eq(User::getGender, gender);
        }
        if (status != null) {
            wrapper.eq(User::getStatus, status);
        }
        
        wrapper.orderByDesc(User::getRegister_time);
        return this.page(page, wrapper);
    }

    /**
     * 检查手机号是否已存在
     * @param phone 手机号
     * @return 存在返回true，否则返回false
     */
    public boolean isPhoneExist(String phone) {
        if (!StringUtils.hasText(phone)) {
            return false;
        }
        return this.lambdaQuery()
                .eq(User::getPhone, phone)
                .count() > 0;
    }

    /**
     * 更新用户状态
     * @param userId 用户ID
     * @param status 状态（1：正常，0：禁用）
     * @return 更新是否成功
     */
    public boolean updateStatus(Long userId, int status) {
        User user = new User();
        user.setUser_id(userId);
        user.setStatus(status);
        return this.updateById(user);
    }

    /**
     * 修改密码
     * @param userId 用户ID
     * @param newPasswordHash 新密码哈希值
     * @return 更新是否成功
     */
    public boolean updatePassword(Long userId, String newPasswordHash) {
        if (!StringUtils.hasText(newPasswordHash)) {
            return false;
        }
        User user = new User();
        user.setUser_id(userId);
        user.setPassword_hash(newPasswordHash);
        return this.updateById(user);
    }

    /**
     * 根据性别查询用户列表
     * @param gender 性别（0：未知，1：男，2：女）
     * @return 用户列表
     */
    public List<User> getByGender(int gender) {
        return this.lambdaQuery()
                .eq(User::getGender, gender)
                .list();
    }

    /**
     * 查询最近登录的用户
     * @param limit 限制数量
     * @return 用户列表
     */
    public List<User> getRecentLoginUsers(int limit) {
        return this.baseMapper.findRecentLogin(limit);
    }

    /**
     * 查询活跃用户（最近N天有登录记录）
     * @param days 天数
     * @return 用户列表
     */
    public List<User> getActiveUsers(int days) {
        return this.baseMapper.findActiveUsers(days);
    }

    /**
     * 统计每个性别的用户数量
     * @return Map结果
     */
    public List<java.util.Map<String, Object>> countByGender() {
        return this.baseMapper.countByGender();
    }

    /**
     * 统计每个状态的用户数量
     * @return Map结果
     */
    public List<java.util.Map<String, Object>> countByStatus() {
        return this.baseMapper.countByStatus();
    }
}
