package com.blue.jitian.Controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blue.jitian.Entity.Notification;
import com.blue.jitian.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /**
     * 发送通知给用户
     * @param userId 用户ID
     * @param notificationType 消息类型
     * @param title 标题
     * @param content 内容
     * @param linkUrl 链接地址
     * @return 发送结果
     */
    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendNotification(@RequestParam Long userId,
                                                                  @RequestParam Integer notificationType,
                                                                  @RequestParam String title,
                                                                  @RequestParam String content,
                                                                  @RequestParam(required = false) String linkUrl) {
        Map<String, Object> response = new HashMap<>();
        Notification notification = notificationService.sendNotification(userId, notificationType, 
                                                                          title, content, linkUrl);
        if (notification != null) {
            response.put("success", true);
            response.put("message", "通知发送成功");
            response.put("data", notification);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "通知发送失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 批量发送通知给多个用户
     * @param userIds 用户ID列表
     * @param notificationType 消息类型
     * @param title 标题
     * @param content 内容
     * @param linkUrl 链接地址
     * @return 发送结果
     */
    @PostMapping("/send-batch")
    public ResponseEntity<Map<String, Object>> sendBatchNotification(@RequestBody List<Long> userIds,
                                                                       @RequestParam Integer notificationType,
                                                                       @RequestParam String title,
                                                                       @RequestParam String content,
                                                                       @RequestParam(required = false) String linkUrl) {
        Map<String, Object> response = new HashMap<>();
        int count = notificationService.sendBatchNotification(userIds, notificationType, title, content, linkUrl);
        response.put("success", true);
        response.put("message", "批量通知发送成功");
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    /**
     * 标记消息为已读
     * @param notificationId 消息ID
     * @return 更新结果
     */
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Map<String, Object>> markAsRead(@PathVariable Long notificationId) {
        Map<String, Object> response = new HashMap<>();
        boolean success = notificationService.markAsRead(notificationId);
        if (success) {
            response.put("success", true);
            response.put("message", "已标记为已读");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "标记失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 批量标记消息为已读
     * @param notificationIds 消息ID列表
     * @return 更新结果
     */
    @PutMapping("/read-batch")
    public ResponseEntity<Map<String, Object>> markBatchAsRead(@RequestBody List<Long> notificationIds) {
        Map<String, Object> response = new HashMap<>();
        boolean success = notificationService.markBatchAsRead(notificationIds);
        if (success) {
            response.put("success", true);
            response.put("message", "批量标记成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "批量标记失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 标记用户所有消息为已读
     * @param userId 用户ID
     * @return 更新结果
     */
    @PutMapping("/user/{userId}/read-all")
    public ResponseEntity<Map<String, Object>> markAllAsRead(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        int count = notificationService.markAllAsRead(userId);
        response.put("success", true);
        response.put("message", "已全部标记为已读");
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    /**
     * 标记指定类型的消息为已读
     * @param userId 用户ID
     * @param notificationType 消息类型
     * @return 更新结果
     */
    @PutMapping("/user/{userId}/read-by-type")
    public ResponseEntity<Map<String, Object>> markAsReadByType(@PathVariable Long userId,
                                                                  @RequestParam Integer notificationType) {
        Map<String, Object> response = new HashMap<>();
        int count = notificationService.markAsReadByType(userId, notificationType);
        response.put("success", true);
        response.put("message", "已标记该类型消息为已读");
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    /**
     * 查询用户的通知列表
     * @param userId 用户ID
     * @param isRead 是否已读（可选）
     * @param notificationType 消息类型（可选）
     * @return 通知列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable Long userId,
                                                                     @RequestParam(required = false) Integer isRead,
                                                                     @RequestParam(required = false) Integer notificationType) {
        List<Notification> notifications = notificationService.getUserNotifications(userId, isRead, notificationType);
        return ResponseEntity.ok(notifications);
    }

    /**
     * 分页查询通知
     * @param current 当前页
     * @param size 每页大小
     * @param userId 用户ID（可选）
     * @param isRead 是否已读（可选）
     * @param notificationType 消息类型（可选）
     * @return 分页结果
     */
    @GetMapping("/page")
    public ResponseEntity<Page<Notification>> getNotificationPage(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Integer isRead,
            @RequestParam(required = false) Integer notificationType) {
        Page<Notification> page = notificationService.getNotificationPage(current, size, userId, isRead, notificationType);
        return ResponseEntity.ok(page);
    }

    /**
     * 根据ID查询通知详情
     * @param id 通知ID
     * @return Notification对象
     */
    @GetMapping("/{id}")
    public ResponseEntity<Notification> getById(@PathVariable Long id) {
        Notification notification = notificationService.getById(id);
        if (notification != null) {
            return ResponseEntity.ok(notification);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 统计用户未读消息数量
     * @param userId 用户ID
     * @return 未读消息数量
     */
    @GetMapping("/user/{userId}/unread-count")
    public ResponseEntity<Map<String, Object>> countUnread(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        int count = notificationService.countUnread(userId);
        response.put("userId", userId);
        response.put("unreadCount", count);
        return ResponseEntity.ok(response);
    }

    /**
     * 统计用户各类型未读消息数量
     * @param userId 用户ID
     * @return 统计结果
     */
    @GetMapping("/user/{userId}/unread-by-type")
    public ResponseEntity<List<Map<String, Object>>> countUnreadByType(@PathVariable Long userId) {
        List<Map<String, Object>> stats = notificationService.countUnreadByType(userId);
        return ResponseEntity.ok(stats);
    }

    /**
     * 查询用户最近N条未读消息
     * @param userId 用户ID
     * @param limit 数量限制
     * @return 消息列表
     */
    @GetMapping("/user/{userId}/recent-unread")
    public ResponseEntity<List<Notification>> getRecentUnread(@PathVariable Long userId,
                                                                @RequestParam(defaultValue = "5") int limit) {
        List<Notification> notifications = notificationService.getRecentUnread(userId, limit);
        return ResponseEntity.ok(notifications);
    }

    /**
     * 统计各消息类型的总数
     * @return 统计结果
     */
    @GetMapping("/stats/type")
    public ResponseEntity<List<Map<String, Object>>> countByType() {
        List<Map<String, Object>> stats = notificationService.countByType();
        return ResponseEntity.ok(stats);
    }

    /**
     * 删除通知
     * @param id 通知ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        boolean success = notificationService.removeById(id);
        if (success) {
            response.put("success", true);
            response.put("message", "删除成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "删除失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 删除用户的所有消息
     * @param userId 用户ID
     * @return 删除结果
     */
    @DeleteMapping("/user/{userId}/all")
    public ResponseEntity<Map<String, Object>> deleteUserAllMessages(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        int count = notificationService.deleteUserAllMessages(userId);
        response.put("success", true);
        response.put("message", "已删除所有消息");
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    /**
     * 删除用户的已读消息
     * @param userId 用户ID
     * @return 删除结果
     */
    @DeleteMapping("/user/{userId}/read")
    public ResponseEntity<Map<String, Object>> deleteUserReadMessages(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        int count = notificationService.deleteUserReadMessages(userId);
        response.put("success", true);
        response.put("message", "已删除已读消息");
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    /**
     * 删除指定天数之前的已读消息
     * @param days 天数
     * @return 删除结果
     */
    @DeleteMapping("/clean-old")
    public ResponseEntity<Map<String, Object>> deleteOldReadMessages(
            @RequestParam(defaultValue = "30") int days) {
        Map<String, Object> response = new HashMap<>();
        int count = notificationService.deleteOldReadMessages(days);
        response.put("success", true);
        response.put("message", "已清理" + days + "天前的已读消息");
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    /**
     * 发送订单通知
     * @param userId 用户ID
     * @param title 标题
     * @param content 内容
     * @param orderId 订单ID
     * @return 发送结果
     */
    @PostMapping("/send-order")
    public ResponseEntity<Map<String, Object>> sendOrderNotification(@RequestParam Long userId,
                                                                       @RequestParam String title,
                                                                       @RequestParam String content,
                                                                       @RequestParam Long orderId) {
        Map<String, Object> response = new HashMap<>();
        Notification notification = notificationService.sendOrderNotification(userId, title, content, orderId);
        if (notification != null) {
            response.put("success", true);
            response.put("message", "订单通知发送成功");
            response.put("data", notification);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "订单通知发送失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 发送活动通知
     * @param userId 用户ID
     * @param title 标题
     * @param content 内容
     * @param activityUrl 活动链接
     * @return 发送结果
     */
    @PostMapping("/send-activity")
    public ResponseEntity<Map<String, Object>> sendActivityNotification(@RequestParam Long userId,
                                                                          @RequestParam String title,
                                                                          @RequestParam String content,
                                                                          @RequestParam(required = false) String activityUrl) {
        Map<String, Object> response = new HashMap<>();
        Notification notification = notificationService.sendActivityNotification(userId, title, content, activityUrl);
        if (notification != null) {
            response.put("success", true);
            response.put("message", "活动通知发送成功");
            response.put("data", notification);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "活动通知发送失败");
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 发送系统通知
     * @param userId 用户ID
     * @param title 标题
     * @param content 内容
     * @return 发送结果
     */
    @PostMapping("/send-system")
    public ResponseEntity<Map<String, Object>> sendSystemNotification(@RequestParam Long userId,
                                                                        @RequestParam String title,
                                                                        @RequestParam String content) {
        Map<String, Object> response = new HashMap<>();
        Notification notification = notificationService.sendSystemNotification(userId, title, content);
        if (notification != null) {
            response.put("success", true);
            response.put("message", "系统通知发送成功");
            response.put("data", notification);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "系统通知发送失败");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
