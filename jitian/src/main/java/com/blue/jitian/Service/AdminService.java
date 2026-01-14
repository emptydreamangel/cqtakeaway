package com.blue.jitian.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.jitian.Entity.Admin;
import com.blue.jitian.Mapper.AdminMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class AdminService extends ServiceImpl<AdminMapper, Admin> {

    /**
     * 根据用户名查询管理员
     * @param username 用户名
     * @return Admin对象，如果不存在则返回null
     */
    public Admin getByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            return null;
        }
        return this.lambdaQuery()
                .eq(Admin::getUsername, username)
                .one();
    }

    /**
     * 验证管理员登录
     * @param username 用户名
     * @param passwordHash 密码哈希值
     * @return 验证成功返回Admin对象，否则返回null
     */
    public Admin validateLogin(String username, String passwordHash) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(passwordHash)) {
            return null;
        }
        return this.lambdaQuery()
                .eq(Admin::getUsername, username)
                .eq(Admin::getPassword_hash, passwordHash)
                .eq(Admin::getStatus, 1) // 只查询状态为激活的用户
                .one();
    }

    /**
     * 更新最后登录时间
     * @param adminId 管理员ID
     * @return 更新是否成功
     */
    public boolean updateLastLoginTime(int adminId) {
        Admin admin = new Admin();
        admin.setAdmin_id(adminId);
        admin.setLast_login_time(LocalDateTime.now());
        return this.updateById(admin);
    }

    /**
     * 根据角色ID查询管理员列表
     * @param roleId 角色ID
     * @return 管理员列表
     */
    public List<Admin> getByRoleId(int roleId) {
        return this.lambdaQuery()
                .eq(Admin::getRole_id, roleId)
                .list();
    }

    /**
     * 根据状态查询管理员列表
     * @param status 状态（1：激活，0：禁用）
     * @return 管理员列表
     */
    public List<Admin> getByStatus(int status) {
        return this.lambdaQuery()
                .eq(Admin::getStatus, status)
                .list();
    }

    /**
     * 分页查询管理员
     * @param current 当前页
     * @param size 每页大小
     * @param username 用户名（模糊查询，可选）
     * @param realName 真实姓名（模糊查询，可选）
     * @param roleId 角色ID（可选）
     * @param status 状态（可选）
     * @return 分页结果
     */
    public Page<Admin> getAdminPage(long current, long size, String username, 
                                     String realName, Integer roleId, Integer status) {
        Page<Admin> page = new Page<>(current, size);
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(username)) {
            wrapper.like(Admin::getUsername, username);
        }
        if (StringUtils.hasText(realName)) {
            wrapper.like(Admin::getReal_name, realName);
        }
        if (roleId != null) {
            wrapper.eq(Admin::getRole_id, roleId);
        }
        if (status != null) {
            wrapper.eq(Admin::getStatus, status);
        }
        
        wrapper.orderByDesc(Admin::getCreated_at);
        return this.page(page, wrapper);
    }

    /**
     * 根据手机号查询管理员
     * @param phone 手机号
     * @return Admin对象，如果不存在则返回null
     */
    public Admin getByPhone(String phone) {
        if (!StringUtils.hasText(phone)) {
            return null;
        }
        return this.lambdaQuery()
                .eq(Admin::getPhone, phone)
                .one();
    }

    /**
     * 检查用户名是否已存在
     * @param username 用户名
     * @return 存在返回true，否则返回false
     */
    public boolean isUsernameExist(String username) {
        if (!StringUtils.hasText(username)) {
            return false;
        }
        return this.lambdaQuery()
                .eq(Admin::getUsername, username)
                .count() > 0;
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
                .eq(Admin::getPhone, phone)
                .count() > 0;
    }

    /**
     * 更新管理员状态
     * @param adminId 管理员ID
     * @param status 状态（1：激活，0：禁用）
     * @return 更新是否成功
     */
    public boolean updateStatus(int adminId, int status) {
        Admin admin = new Admin();
        admin.setAdmin_id(adminId);
        admin.setStatus(status);
        return this.updateById(admin);
    }

    /**
     * 修改密码
     * @param adminId 管理员ID
     * @param newPasswordHash 新密码哈希值
     * @return 更新是否成功
     */
    public boolean updatePassword(int adminId, String newPasswordHash) {
        if (!StringUtils.hasText(newPasswordHash)) {
            return false;
        }
        Admin admin = new Admin();
        admin.setAdmin_id(adminId);
        admin.setPassword_hash(newPasswordHash);
        return this.updateById(admin);
    }
}
