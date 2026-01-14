package com.blue.jitian.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blue.jitian.Entity.PaymentRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Mapper
public interface PaymentRecordMapper extends BaseMapper<PaymentRecord> {

    /**
     * 统计各支付状态的记录数量
     * @return 统计结果
     */
    @Select("SELECT status, COUNT(*) as count FROM payment_records GROUP BY status")
    List<Map<String, Object>> countByStatus();

    /**
     * 统计各支付方式的记录数量和金额
     * @return 统计结果
     */
    @Select("SELECT pay_method, COUNT(*) as count, COALESCE(SUM(amount), 0) as total_amount " +
            "FROM payment_records WHERE status = 1 GROUP BY pay_method")
    List<Map<String, Object>> countByPayMethod();

    /**
     * 查询用户的支付记录统计
     * @param userId 用户ID
     * @return 统计结果
     */
    @Select("SELECT status, COUNT(*) as count, COALESCE(SUM(amount), 0) as total_amount " +
            "FROM payment_records WHERE user_id = #{userId} GROUP BY status")
    List<Map<String, Object>> countByUserIdAndStatus(@Param("userId") Long userId);

    /**
     * 查询指定时间范围内的支付成功总金额
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 总金额
     */
    @Select("SELECT COALESCE(SUM(amount), 0) FROM payment_records " +
            "WHERE status = 1 AND notify_time BETWEEN #{startTime} AND #{endTime}")
    BigDecimal sumSuccessAmountByTimeRange(@Param("startTime") String startTime, 
                                           @Param("endTime") String endTime);

    /**
     * 查询待支付超时的记录（创建超过指定分钟数仍未支付）
     * @param minutes 超时分钟数
     * @return 超时记录列表
     */
    @Select("SELECT * FROM payment_records WHERE status = 0 " +
            "AND created_at < NOW() - INTERVAL '#{minutes} minutes'")
    List<PaymentRecord> findTimeoutPayments(@Param("minutes") int minutes);

    /**
     * 按日期统计支付成功的订单数和金额
     * @param days 统计最近N天
     * @return 统计结果
     */
    @Select("SELECT DATE(notify_time) as date, COUNT(*) as count, COALESCE(SUM(amount), 0) as total_amount " +
            "FROM payment_records WHERE status = 1 AND notify_time >= NOW() - INTERVAL '#{days} days' " +
            "GROUP BY DATE(notify_time) ORDER BY date DESC")
    List<Map<String, Object>> dailySuccessStats(@Param("days") int days);
}
