package com.blue.jitian.Controller.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blue.jitian.Entity.User;
import com.blue.jitian.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 用户控制器
 * 提供用户相关的REST API接口
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取所有用户列表
     * @return 用户列表
     */
    @GetMapping("/list")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.list();
        return ResponseEntity.ok(users);
    }

    /**
     * 根据ID获取用户信息
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "用户不存在"));
        }
        return ResponseEntity.ok(user);
    }

    /**
     * 根据手机号获取用户信息
     * @param phone 手机号
     * @return 用户信息
     */
    @GetMapping("/phone/{phone}")
    public ResponseEntity<?> getUserByPhone(@PathVariable("phone") String phone) {
        User user = userService.getByPhone(phone);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "用户不存在"));
        }
        return ResponseEntity.ok(user);
    }

    /**
     * 分页查询用户
     * @param current 当前页，默认1
     * @param size 每页大小，默认10
     * @param nickname 昵称（可选）
     * @param phone 手机号（可选）
     * @param gender 性别（可选）
     * @param status 状态（可选）
     * @return 分页结果
     */
    @GetMapping("/page")
    public ResponseEntity<Page<User>> getUserPage(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) Integer gender,
            @RequestParam(required = false) Integer status) {
        Page<User> page = userService.getUserPage(current, size, nickname, phone, gender, status);
        return ResponseEntity.ok(page);
    }

    /**
     * 用户注册
     * @param user 用户对象
     * @return 注册结果
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        // 检查手机号是否已存在
        if (userService.isPhoneExist(user.getPhone())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "手机号已存在"));
        }
        
        // 设置默认值
        if (user.getStatus() == null) {
            user.setStatus(1); // 默认状态为正常
        }
        if (user.getGender() == null) {
            user.setGender(0); // 默认性别为未知
        }
        
        boolean saved = userService.save(user);
        if (saved) {
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "注册失败"));
    }

    /**
     * 更新用户信息
     * @param id 用户ID
     * @param user 用户对象
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        User existingUser = userService.getById(id);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "用户不存在"));
        }
        
        user.setUser_id(id);
        boolean updated = userService.updateById(user);
        if (updated) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "更新失败"));
    }

    /**
     * 删除用户
     * @param id 用户ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "用户不存在"));
        }
        
        boolean deleted = userService.removeById(id);
        if (deleted) {
            return ResponseEntity.ok(Map.of("message", "删除成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "删除失败"));
    }

    /**
     * 用户登录（通过手机号）
     * @param loginRequest 登录请求（包含phone和password）
     * @return 登录结果
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String phone = loginRequest.get("phone");
        String password = loginRequest.get("password");
        
        if (phone == null || password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "手机号或密码不能为空"));
        }
        
        // 这里应该对密码进行加密处理，此处仅作演示
        User user = userService.validateLogin(phone, password);
        
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "手机号或密码错误，或账号已被禁用"));
        }
        
        // 更新最后登录时间
        userService.updateLastLoginTime(user.getUser_id());
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "登录成功");
        response.put("user", user);
        
        return ResponseEntity.ok(response);
    }

    /**
     * 更新用户状态
     * @param id 用户ID
     * @param statusRequest 状态请求（包含status字段）
     * @return 更新结果
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable("id") Long id, 
                                          @RequestBody Map<String, Integer> statusRequest) {
        Integer status = statusRequest.get("status");
        if (status == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "状态不能为空"));
        }
        
        boolean updated = userService.updateStatus(id, status);
        if (updated) {
            return ResponseEntity.ok(Map.of("message", "状态更新成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "状态更新失败"));
    }

    /**
     * 修改密码
     * @param id 用户ID
     * @param passwordRequest 密码请求（包含newPassword字段）
     * @return 更新结果
     */
    @PatchMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(@PathVariable("id") Long id, 
                                           @RequestBody Map<String, String> passwordRequest) {
        String newPassword = passwordRequest.get("newPassword");
        if (newPassword == null || newPassword.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "新密码不能为空"));
        }
        
        // 这里应该对密码进行加密处理，此处仅作演示
        boolean updated = userService.updatePassword(id, newPassword);
        if (updated) {
            return ResponseEntity.ok(Map.of("message", "密码修改成功"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "密码修改失败"));
    }

    /**
     * 根据性别查询用户列表
     * @param gender 性别
     * @return 用户列表
     */
    @GetMapping("/gender/{gender}")
    public ResponseEntity<List<User>> getUsersByGender(@PathVariable("gender") Integer gender) {
        List<User> users = userService.getByGender(gender);
        return ResponseEntity.ok(users);
    }

    /**
     * 根据状态查询用户列表
     * @param status 状态
     * @return 用户列表
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<User>> getUsersByStatus(@PathVariable("status") Integer status) {
        List<User> users = userService.getByStatus(status);
        return ResponseEntity.ok(users);
    }

    /**
     * 检查手机号是否存在
     * @param phone 手机号
     * @return 检查结果
     */
    @GetMapping("/check/phone/{phone}")
    public ResponseEntity<Map<String, Boolean>> checkPhoneExist(@PathVariable("phone") String phone) {
        boolean exists = userService.isPhoneExist(phone);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    /**
     * 查询最近登录的用户
     * @param limit 限制数量，默认10
     * @return 用户列表
     */
    @GetMapping("/recent-login")
    public ResponseEntity<List<User>> getRecentLoginUsers(@RequestParam(defaultValue = "10") Integer limit) {
        List<User> users = userService.getRecentLoginUsers(limit);
        return ResponseEntity.ok(users);
    }

    /**
     * 查询活跃用户（最近N天有登录记录）
     * @param days 天数，默认7天
     * @return 用户列表
     */
    @GetMapping("/active")
    public ResponseEntity<List<User>> getActiveUsers(@RequestParam(defaultValue = "7") Integer days) {
        List<User> users = userService.getActiveUsers(days);
        return ResponseEntity.ok(users);
    }

    /**
     * 统计每个性别的用户数量
     * @return 统计结果
     */
    @GetMapping("/stats/gender")
    public ResponseEntity<List<Map<String, Object>>> countByGender() {
        List<Map<String, Object>> stats = userService.countByGender();
        return ResponseEntity.ok(stats);
    }

    /**
     * 统计每个状态的用户数量
     * @return 统计结果
     */
    @GetMapping("/stats/status")
    public ResponseEntity<List<Map<String, Object>>> countByStatus() {
        List<Map<String, Object>> stats = userService.countByStatus();
        return ResponseEntity.ok(stats);
    }
}
