package com.blue.jitian.Controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blue.jitian.Entity.Admin;
import com.blue.jitian.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 管理员控制器
 * 提供管理员相关的REST API接口
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * 获取所有管理员列表
     * @return 管理员列表
     */
    @GetMapping("/list")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        List<Admin> admins = adminService.list();
        return ResponseEntity.ok(admins);
    }

    /**
     * 根据ID获取管理员信息
     * @param id 管理员ID
     * @return 管理员信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getAdminById(@PathVariable("id") Integer id) {
        Admin admin = adminService.getById(id);
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "管理员不存在"));
        }
        return ResponseEntity.ok(admin);
    }

    /**
     * 根据用户名获取管理员信息
     * @param username 用户名
     * @return 管理员信息
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<?> getAdminByUsername(@PathVariable("username") String username) {
        Admin admin = adminService.getByUsername(username);
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "管理员不存在"));
        }
        return ResponseEntity.ok(admin);
    }

    /**
     * 分页查询管理员
     * @param current 当前页，默认1
     * @param size 每页大小，默认10
     * @param username 用户名（可选）
     * @param realName 真实姓名（可选）
     * @param roleId 角色ID（可选）
     * @param status 状态（可选）
     * @return 分页结果
     */
    @GetMapping("/page")
    public ResponseEntity<Page<Admin>> getAdminPage(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String realName,
            @RequestParam(required = false) Integer roleId,
            @RequestParam(required = false) Integer status) {
        Page<Admin> page = adminService.getAdminPage(current, size, username, realName, roleId, status);
        return ResponseEntity.ok(page);
    }

    /**
     * 创建管理员
     * @param admin 管理员对象
     * @return 创建结果
     */
    @PostMapping
    public ResponseEntity<?> createAdmin(@RequestBody Admin admin) {
        // 检查用户名是否已存在
        if (adminService.isUsernameExist(admin.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "用户名已存在"));
        }
        
        // 检查手机号是否已存在
        if (admin.getPhone() != null && adminService.isPhoneExist(admin.getPhone())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "手机号已存在"));
        }
        
        boolean saved = adminService.save(admin);
        if (saved) {
            return ResponseEntity.status(HttpStatus.CREATED).body(admin);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "创建失败"));
    }

    /**
     * 更新管理员信息
     * @param id 管理员ID
     * @param admin 管理员对象
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable("id") Integer id, @RequestBody Admin admin) {
        Admin existingAdmin = adminService.getById(id);
        if (existingAdmin == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "管理员不存在"));
        }
        
        admin.setAdmin_id(id);
        boolean updated = adminService.updateById(admin);
        if (updated) {
            return ResponseEntity.ok(admin);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "更新失败"));
    }

    /**
     * 删除管理员
     * @param id 管理员ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable("id") Integer id) {
        Admin admin = adminService.getById(id);
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "管理员不存在"));
        }
        
        boolean deleted = adminService.removeById(id);
        if (deleted) {
            return ResponseEntity.ok(Map.of("message", "删除成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "删除失败"));
    }

    /**
     * 管理员登录
     * @param loginRequest 登录请求（包含username和password）
     * @return 登录结果
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        
        if (username == null || password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "用户名或密码不能为空"));
        }
        
        // 这里应该对密码进行加密处理，此处仅作演示
        Admin admin = adminService.validateLogin(username, password);
        
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "用户名或密码错误，或账号已被禁用"));
        }
        
        // 更新最后登录时间
        adminService.updateLastLoginTime(admin.getAdmin_id());
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "登录成功");
        response.put("admin", admin);
        
        return ResponseEntity.ok(response);
    }

    /**
     * 更新管理员状态
     * @param id 管理员ID
     * @param statusRequest 状态请求（包含status字段）
     * @return 更新结果
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable("id") Integer id, 
                                          @RequestBody Map<String, Integer> statusRequest) {
        Integer status = statusRequest.get("status");
        if (status == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "状态不能为空"));
        }
        
        boolean updated = adminService.updateStatus(id, status);
        if (updated) {
            return ResponseEntity.ok(Map.of("message", "状态更新成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "状态更新失败"));
    }

    /**
     * 修改密码
     * @param id 管理员ID
     * @param passwordRequest 密码请求（包含newPassword字段）
     * @return 更新结果
     */
    @PatchMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(@PathVariable("id") Integer id, 
                                           @RequestBody Map<String, String> passwordRequest) {
        String newPassword = passwordRequest.get("newPassword");
        if (newPassword == null || newPassword.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "新密码不能为空"));
        }
        
        // 这里应该对密码进行加密处理，此处仅作演示
        boolean updated = adminService.updatePassword(id, newPassword);
        if (updated) {
            return ResponseEntity.ok(Map.of("message", "密码修改成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "密码修改失败"));
    }

    /**
     * 根据角色ID查询管理员列表
     * @param roleId 角色ID
     * @return 管理员列表
     */
    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<Admin>> getAdminsByRole(@PathVariable("roleId") Integer roleId) {
        List<Admin> admins = adminService.getByRoleId(roleId);
        return ResponseEntity.ok(admins);
    }

    /**
     * 根据状态查询管理员列表
     * @param status 状态
     * @return 管理员列表
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Admin>> getAdminsByStatus(@PathVariable("status") Integer status) {
        List<Admin> admins = adminService.getByStatus(status);
        return ResponseEntity.ok(admins);
    }

    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return 检查结果
     */
    @GetMapping("/check/username/{username}")
    public ResponseEntity<Map<String, Boolean>> checkUsernameExist(@PathVariable("username") String username) {
        boolean exists = adminService.isUsernameExist(username);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    /**
     * 检查手机号是否存在
     * @param phone 手机号
     * @return 检查结果
     */
    @GetMapping("/check/phone/{phone}")
    public ResponseEntity<Map<String, Boolean>> checkPhoneExist(@PathVariable("phone") String phone) {
        boolean exists = adminService.isPhoneExist(phone);
        return ResponseEntity.ok(Map.of("exists", exists));
    }
}
