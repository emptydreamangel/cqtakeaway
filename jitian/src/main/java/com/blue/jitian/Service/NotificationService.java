package com.blue.jitian.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.jitian.Entity.Notification;
import com.blue.jitian.Mapper.NotificationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
public class NotificationService extends ServiceImpl<NotificationMapper, Notification> {

    /**
     * 发送通知给用户
     * @param userId 用户ID
     * @param notificationType 消息类型
     * @param title 标题
     * @param content 内容
     * @param linkUrl 链接地址
     * @return Notification对象
     */
    @Transactional
    public Notification sendNotification(Long userId, Integer notificationType, 
                                          String title, String content, String linkUrl) {
        Notification notification = Notification.builder()
                .userId(userId)
                .notificationType(notificationType)
                .title(title)
                .content(content)
                .linkUrl(linkUrl)
                .isRead(0)
                .build();
        
        if (this.save(notification)) {
            return notification;
        }
        return null;
    }

    /**
     * 批量发送通知给多个用户
     * @param userIds 用户ID列表
     * @param notificationType 消息类型
     * @param title 标题
     * @param content 内容
     * @param linkUrl 链接地址
     * @return 发送成功数量
     */
    @Transactional
    public int sendBatchNotification(List<Long> userIds, Integer notificationType, 
                                      String title, String content, String linkUrl) {
        if (userIds == null || userIds.isEmpty()) {
            return 0;
        }
        
        List<Notification> notifications = userIds.stream()
                .map(userId -> Notification.builder()
                        .userId(userId)
                        .notificationType(notificationType)
                        .title(title)
                        .content(content)
                        .linkUrl(linkUrl)
                        .isRead(0)
                        .build())
                .toList();
        
        return this.saveBatch(notifications) ? notifications.size() : 0;
    }

    /**
     * 标记消息为已读
     * @param notificationId 消息ID
     * @return 更新是否成功
     */
    @Transactional
    public boolean markAsRead(Long notificationId) {
        Notification notification = new Notification();
        notification.setNotificationId(notificationId);
        notification.setIsRead(1);
        return this.updateById(notification);
    }

    /**
     * 批量标记消息为已读
     * @param notificationIds 消息ID列表
     * @return 更新是否成功
     */
    @Transactional
    public boolean markBatchAsRead(List<Long> notificationIds) {
        if (notificationIds == null || notificationIds.isEmpty()) {
            return false;
        }
        
        LambdaUpdateWrapper<Notification> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(Notification::getNotificationId, notificationIds)
                .set(Notification::getIsRead, 1);
        return this.update(wrapper);
    }

    /**
     * 标记用户所有消息为已读
     * @param userId 用户ID
     * @return 更新数量
     */
    @Transactional
    public int markAllAsRead(Long userId) {
        return this.baseMapper.markAllAsReadByUserId(userId);
    }

    /**
     * 标记指定类型的消息为已读
     * @param userId 用户ID
     * @param notificationType 消息类型
     * @return 更新数量
     */
    @Transactional
    public int markAsReadByType(Long userId, Integer notificationType) {
        return this.baseMapper.markAsReadByType(userId, notificationType);
    }

    /**
     * 查询用户的通知列表
     * @param userId 用户ID
     * @param isRead 是否已读（可选）
     * @param notificationType 消息类型（可选）
     * @return 通知列表
     */
    public List<Notification> getUserNotifications(Long userId, Integer isRead, Integer notificationType) {
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getUserId, userId);
        if (isRead != null) {
            wrapper.eq(Notification::getIsRead, isRead);
        }
        if (notificationType != null) {
            wrapper.eq(Notification::getNotificationType, notificationType);
        }
        wrapper.orderByDesc(Notification::getCreatedAt);
        return this.list(wrapper);
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
    public Page<Notification> getNotificationPage(long current, long size, Long userId, 
                                                    Integer isRead, Integer notificationType) {
        Page<Notification> page = new Page<>(current, size);
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        
        if (userId != null) {
            wrapper.eq(Notification::getUserId, userId);
        }
        if (isRead != null) {
            wrapper.eq(Notification::getIsRead, isRead);
        }
        if (notificationType != null) {
            wrapper.eq(Notification::getNotificationType, notificationType);
        }
        
        wrapper.orderByDesc(Notification::getCreatedAt);
        return this.page(page, wrapper);
    }

    /**
     * 统计用户未读消息数量
     * @param userId 用户ID
     * @return 未读消息数量
     */
    public int countUnread(Long userId) {
        return this.baseMapper.countUnreadByUserId(userId);
    }

    /**
     * 统计用户各类型未读消息数量
     * @param userId 用户ID
     * @return 统计结果
     */
    public List<Map<String, Object>> countUnreadByType(Long userId) {
        return this.baseMapper.countUnreadByType(userId);
    }

    /**
     * 查询用户最近N条未读消息
     * @param userId 用户ID
     * @param limit 数量限制
     * @return 消息列表
     */
    public List<Notification> getRecentUnread(Long userId, int limit) {
        return this.baseMapper.findRecentUnread(userId, limit);
    }

    /**
     * 统计各消息类型的总数
     * @return 统计结果
     */
    public List<Map<String, Object>> countByType() {
        return this.baseMapper.countByType();
    }

    /**
     * 删除指定天数之前的已读消息
     * @param days 天数
     * @return 删除数量
     */
    @Transactional
    public int deleteOldReadMessages(int days) {
        return this.baseMapper.deleteOldReadMessages(days);
    }

    /**
     * 删除用户的所有消息
     * @param userId 用户ID
     * @return 删除数量
     */
    @Transactional
    public int deleteUserAllMessages(Long userId) {
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getUserId, userId);
        return this.baseMapper.delete(wrapper);
    }

    /**
     * 删除用户的已读消息
     * @param userId 用户ID
     * @return 删除数量
     */
    @Transactional
    public int deleteUserReadMessages(Long userId) {
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getUserId, userId)
                .eq(Notification::getIsRead, 1);
        return this.baseMapper.delete(wrapper);
    }

    /**
     * 发送订单通知
     * @param userId 用户ID
     * @param title 标题
     * @param content 内容
     * @param orderId 订单ID
     * @return Notification对象
     */
    @Transactional
    public Notification sendOrderNotification(Long userId, String title, String content, Long orderId) {
        String linkUrl = "/orders/" + orderId;
        return sendNotification(userId, 1, title, content, linkUrl);
    }

    /**
     * 发送活动通知
     * @param userId 用户ID
     * @param title 标题
     * @param content 内容
     * @param activityUrl 活动链接
     * @return Notification对象
     */
    @Transactional
    public Notification sendActivityNotification(Long userId, String title, String content, String activityUrl) {
        return sendNotification(userId, 2, title, content, activityUrl);
    }

    /**
     * 发送系统通知
     * @param userId 用户ID
     * @param title 标题
     * @param content 内容
     * @return Notification对象
     */
    @Transactional
    public Notification sendSystemNotification(Long userId, String title, String content) {
        return sendNotification(userId, 3, title, content, null);
    }
}
