package com.blue.jitian.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blue.jitian.Entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;


@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据手机号和状态查询用户
     * @param phone 手机号
     * @param status 状态
     * @return User对象
     */
    @Select("SELECT * FROM users WHERE phone = #{phone} AND status = #{status}")
    User findByPhoneAndStatus(@Param("phone") String phone, @Param("status") int status);

    /**
     * 根据手机号查询用户
     * @param phone 手机号
     * @return User对象
     */
    @Select("SELECT * FROM users WHERE phone = #{phone}")
    User findByPhone(@Param("phone") String phone);

    /**
     * 查询最近登录的用户列表
     * @param limit 限制数量
     * @return 用户列表
     */
    @Select("SELECT * FROM users WHERE last_login_time IS NOT NULL ORDER BY last_login_time DESC LIMIT #{limit}")
    List<User> findRecentLogin(@Param("limit") int limit);

    /**
     * 批量更新用户状态
     * @param userIds 用户ID列表
     * @param status 状态
     * @return 影响行数
     */
    @Update("<script>" +
            "UPDATE users SET status = #{status} " +
            "WHERE user_id IN " +
            "<foreach collection='userIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    int batchUpdateStatus(@Param("userIds") List<Long> userIds, @Param("status") int status);

    /**
     * 查询指定时间范围内注册的用户
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 用户列表
     */
    @Select("SELECT * FROM users WHERE register_time BETWEEN #{startTime} AND #{endTime} ORDER BY register_time DESC")
    List<User> findByRegisterTimeRange(@Param("startTime") LocalDateTime startTime, 
                                        @Param("endTime") LocalDateTime endTime);

    /**
     * 根据性别统计用户数量
     * @return Map结果，key为gender，value为count
     */
    @Select("SELECT gender, COUNT(*) as count FROM users GROUP BY gender")
    List<java.util.Map<String, Object>> countByGender();

    /**
     * 查询活跃用户（最近N天有登录记录）
     * @param days 天数
     * @return 用户列表
     */
    @Select("SELECT * FROM users WHERE last_login_time >= NOW() - INTERVAL '#{days} days' ORDER BY last_login_time DESC")
    List<User> findActiveUsers(@Param("days") int days);

    /**
     * 统计每个状态的用户数量
     * @return Map结果
     */
    @Select("SELECT status, COUNT(*) as count FROM users GROUP BY status")
    List<java.util.Map<String, Object>> countByStatus();
}
