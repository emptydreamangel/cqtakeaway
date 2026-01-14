package com.blue.jitian.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blue.jitian.Entity.UserAddress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


@Mapper
public interface UserAddressMapper extends BaseMapper<UserAddress> {

    /**
     * 根据用户ID查询所有地址
     * @param userId 用户ID
     * @return 地址列表
     */
    @Select("SELECT * FROM user_addresses WHERE user_id = #{userId} ORDER BY is_default DESC, created_at DESC")
    List<UserAddress> findByUserId(@Param("userId") Long userId);

    /**
     * 查询用户的默认地址
     * @param userId 用户ID
     * @return 默认地址
     */
    @Select("SELECT * FROM user_addresses WHERE user_id = #{userId} AND is_default = 1")
    UserAddress findDefaultByUserId(@Param("userId") Long userId);

    /**
     * 取消用户的所有默认地址
     * @param userId 用户ID
     * @return 影响行数
     */
    @Update("UPDATE user_addresses SET is_default = 0, updated_at = NOW() WHERE user_id = #{userId}")
    int clearDefaultByUserId(@Param("userId") Long userId);

    /**
     * 设置默认地址
     * @param addressId 地址ID
     * @return 影响行数
     */
    @Update("UPDATE user_addresses SET is_default = 1, updated_at = NOW() WHERE address_id = #{addressId}")
    int setDefault(@Param("addressId") Long addressId);

    /**
     * 根据用户ID统计地址数量
     * @param userId 用户ID
     * @return 地址数量
     */
    @Select("SELECT COUNT(*) FROM user_addresses WHERE user_id = #{userId}")
    long countByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID和地址ID查询地址（用于验证地址是否属于该用户）
     * @param addressId 地址ID
     * @param userId 用户ID
     * @return 地址对象
     */
    @Select("SELECT * FROM user_addresses WHERE address_id = #{addressId} AND user_id = #{userId}")
    UserAddress findByIdAndUserId(@Param("addressId") Long addressId, @Param("userId") Long userId);

    /**
     * 根据省市区查询地址
     * @param userId 用户ID
     * @param province 省份
     * @param city 城市
     * @param district 区县
     * @return 地址列表
     */
    @Select("SELECT * FROM user_addresses WHERE user_id = #{userId} AND province = #{province} " +
            "AND city = #{city} AND district = #{district} ORDER BY created_at DESC")
    List<UserAddress> findByRegion(@Param("userId") Long userId, 
                                    @Param("province") String province,
                                    @Param("city") String city, 
                                    @Param("district") String district);

    /**
     * 批量删除地址
     * @param addressIds 地址ID列表
     * @param userId 用户ID（用于安全验证）
     * @return 影响行数
     */
    @Update("<script>" +
            "DELETE FROM user_addresses WHERE user_id = #{userId} AND address_id IN " +
            "<foreach collection='addressIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    int batchDelete(@Param("addressIds") List<Long> addressIds, @Param("userId") Long userId);
}
