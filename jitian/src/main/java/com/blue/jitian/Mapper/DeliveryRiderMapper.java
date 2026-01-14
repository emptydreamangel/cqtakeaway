package com.blue.jitian.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blue.jitian.Entity.DeliveryRider;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface DeliveryRiderMapper extends BaseMapper<DeliveryRider> {
    
    /**
     * 根据手机号查询骑手
     */
    @Select("SELECT * FROM delivery_riders WHERE phone = #{phone}")
    DeliveryRider findByPhone(@Param("phone") String phone);
    
    /**
     * 根据手机号和密码验证骑手登录
     */
    @Select("SELECT * FROM delivery_riders WHERE phone = #{phone} AND password_hash = #{passwordHash}")
    DeliveryRider findByPhoneAndPassword(@Param("phone") String phone, @Param("passwordHash") String passwordHash);
    
    /**
     * 更新骑手在线状态
     */
    @Update("UPDATE delivery_riders SET is_online = #{isOnline}, updated_at = CURRENT_TIMESTAMP WHERE rider_id = #{riderId}")
    int updateOnlineStatus(@Param("riderId") Long riderId, @Param("isOnline") Integer isOnline);
    
    /**
     * 更新骑手状态
     */
    @Update("UPDATE delivery_riders SET status = #{status}, updated_at = CURRENT_TIMESTAMP WHERE rider_id = #{riderId}")
    int updateStatus(@Param("riderId") Long riderId, @Param("status") Integer status);
    
    /**
     * 更新骑手位置
     */
    @Update("UPDATE delivery_riders SET current_longitude = #{longitude}, current_latitude = #{latitude}, updated_at = CURRENT_TIMESTAMP WHERE rider_id = #{riderId}")
    int updateLocation(@Param("riderId") Long riderId, @Param("longitude") BigDecimal longitude, @Param("latitude") BigDecimal latitude);
    
    /**
     * 更新骑手余额
     */
    @Update("UPDATE delivery_riders SET balance = balance + #{amount}, updated_at = CURRENT_TIMESTAMP WHERE rider_id = #{riderId}")
    int updateBalance(@Param("riderId") Long riderId, @Param("amount") BigDecimal amount);
    
    /**
     * 获取所有在线且接单中的骑手
     */
    @Select("SELECT * FROM delivery_riders WHERE is_online = 1 AND status = 1")
    List<DeliveryRider> findAvailableRiders();
    
    /**
     * 根据状态查询骑手
     */
    @Select("SELECT * FROM delivery_riders WHERE status = #{status}")
    List<DeliveryRider> findByStatus(@Param("status") Integer status);
    
    /**
     * 根据在线状态查询骑手
     */
    @Select("SELECT * FROM delivery_riders WHERE is_online = #{isOnline}")
    List<DeliveryRider> findByOnlineStatus(@Param("isOnline") Integer isOnline);
    
    /**
     * 根据车辆类型查询骑手
     */
    @Select("SELECT * FROM delivery_riders WHERE vehicle_type = #{vehicleType}")
    List<DeliveryRider> findByVehicleType(@Param("vehicleType") Integer vehicleType);
    
    /**
     * 根据身份证号查询骑手
     */
    @Select("SELECT * FROM delivery_riders WHERE id_card = #{idCard}")
    DeliveryRider findByIdCard(@Param("idCard") String idCard);
    
    /**
     * 统计在线骑手数量
     */
    @Select("SELECT COUNT(*) FROM delivery_riders WHERE is_online = 1")
    Integer countOnlineRiders();
    
    /**
     * 统计可接单骑手数量
     */
    @Select("SELECT COUNT(*) FROM delivery_riders WHERE is_online = 1 AND status = 1")
    Integer countAvailableRiders();
    
    /**
     * 根据位置范围查找附近的骑手（简单示例，实际应使用PostGIS）
     */
    @Select("SELECT * FROM delivery_riders WHERE is_online = 1 AND status = 1 " +
            "AND current_longitude BETWEEN #{minLon} AND #{maxLon} " +
            "AND current_latitude BETWEEN #{minLat} AND #{maxLat}")
    List<DeliveryRider> findNearbyRiders(@Param("minLon") BigDecimal minLon, 
                                         @Param("maxLon") BigDecimal maxLon,
                                         @Param("minLat") BigDecimal minLat, 
                                         @Param("maxLat") BigDecimal maxLat);
    
    /**
     * 批量更新骑手在线状态
     */
    @Update("<script>" +
            "UPDATE delivery_riders SET is_online = #{isOnline}, updated_at = CURRENT_TIMESTAMP " +
            "WHERE rider_id IN " +
            "<foreach collection='riderIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    int batchUpdateOnlineStatus(@Param("riderIds") List<Long> riderIds, @Param("isOnline") Integer isOnline);
}
