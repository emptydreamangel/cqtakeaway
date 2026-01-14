package com.blue.jitian.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blue.jitian.Entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;


@Mapper
public interface AdminMapper extends BaseMapper<Admin> {

    /**
     * 根据用户名和状态查询管理员
     * @param username 用户名
     * @param status 状态
     * @return Admin对象
     */
    @Select("SELECT * FROM admins WHERE username = #{username} AND status = #{status}")
    Admin findByUsernameAndStatus(@Param("username") String username, @Param("status") int status);

    /**
     * 根据角色ID统计管理员数量
     * @param roleId 角色ID
     * @return 管理员数量
     */
    @Select("SELECT COUNT(*) FROM admins WHERE role_id = #{roleId}")
    long countByRoleId(@Param("roleId") int roleId);

    /**
     * 查询最近登录的管理员列表
     * @param limit 限制数量
     * @return 管理员列表
     */
    @Select("SELECT * FROM admins WHERE last_login_time IS NOT NULL ORDER BY last_login_time DESC LIMIT #{limit}")
    List<Admin> findRecentLogin(@Param("limit") int limit);

    /**
     * 批量更新管理员状态
     * @param adminIds 管理员ID列表
     * @param status 状态
     * @return 影响行数
     */
    @Update("<script>" +
            "UPDATE admins SET status = #{status}, updated_at = NOW() " +
            "WHERE admin_id IN " +
            "<foreach collection='adminIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    int batchUpdateStatus(@Param("adminIds") List<Integer> adminIds, @Param("status") int status);

    /**
     * 查询指定时间范围内创建的管理员
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 管理员列表
     */
    @Select("SELECT * FROM admins WHERE created_at BETWEEN #{startTime} AND #{endTime} ORDER BY created_at DESC")
    List<Admin> findByCreateTimeRange(@Param("startTime") LocalDateTime startTime, 
                                       @Param("endTime") LocalDateTime endTime);

    /**
     * 根据手机号查询管理员（模糊查询）
     * @param phone 手机号
     * @return 管理员列表
     */
    @Select("SELECT * FROM admins WHERE phone LIKE CONCAT('%', #{phone}, '%')")
    List<Admin> findByPhoneLike(@Param("phone") String phone);

    /**
     * 统计每个角色的管理员数量
     * @return Map结果，key为roleId，value为count
     */
    @Select("SELECT role_id, COUNT(*) as count FROM admins GROUP BY role_id")
    List<java.util.Map<String, Object>> countByRole();

    /**
     * 查询活跃管理员（最近N天有登录记录）
     * @param days 天数
     * @return 管理员列表
     */
    @Select("SELECT * FROM admins WHERE last_login_time >= NOW() - INTERVAL '#{days} days' ORDER BY last_login_time DESC")
    List<Admin> findActiveAdmins(@Param("days") int days);
}
