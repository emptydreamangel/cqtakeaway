package com.blue.jitian.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blue.jitian.Entity.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;


@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {

    /**
     * 统计用户未读消息数量
     * @param userId 用户ID
     * @return 未读消息数量
     */
    @Select("SELECT COUNT(*) FROM notifications WHERE user_id = #{userId} AND is_read = 0")
    int countUnreadByUserId(@Param("userId") Long userId);

    /**
     * 统计用户各类型未读消息数量
     * @param userId 用户ID
     * @return 统计结果
     */
    @Select("SELECT notification_type, COUNT(*) as count FROM notifications " +
            "WHERE user_id = #{userId} AND is_read = 0 GROUP BY notification_type")
    List<Map<String, Object>> countUnreadByType(@Param("userId") Long userId);

    /**
     * 批量标记用户所有消息为已读
     * @param userId 用户ID
     * @return 更新数量
     */
    @Update("UPDATE notifications SET is_read = 1 WHERE user_id = #{userId} AND is_read = 0")
    int markAllAsReadByUserId(@Param("userId") Long userId);

    /**
     * 批量标记指定类型的消息为已读
     * @param userId 用户ID
     * @param notificationType 消息类型
     * @return 更新数量
     */
    @Update("UPDATE notifications SET is_read = 1 WHERE user_id = #{userId} " +
            "AND notification_type = #{notificationType} AND is_read = 0")
    int markAsReadByType(@Param("userId") Long userId, @Param("notificationType") Integer notificationType);

    /**
     * 查询用户最近N条未读消息
     * @param userId 用户ID
     * @param limit 数量限制
     * @return 消息列表
     */
    @Select("SELECT * FROM notifications WHERE user_id = #{userId} AND is_read = 0 " +
            "ORDER BY created_at DESC LIMIT #{limit}")
    List<Notification> findRecentUnread(@Param("userId") Long userId, @Param("limit") int limit);

    /**
     * 统计各消息类型的总数
     * @return 统计结果
     */
    @Select("SELECT notification_type, COUNT(*) as count FROM notifications GROUP BY notification_type")
    List<Map<String, Object>> countByType();

    /**
     * 删除指定天数之前的已读消息
     * @param days 天数
     * @return 删除数量
     */
    @Update("DELETE FROM notifications WHERE is_read = 1 AND created_at < NOW() - INTERVAL '#{days} days'")
    int deleteOldReadMessages(@Param("days") int days);
}
