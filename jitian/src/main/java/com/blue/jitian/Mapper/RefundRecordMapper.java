package com.blue.jitian.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blue.jitian.Entity.RefundRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Mapper
public interface RefundRecordMapper extends BaseMapper<RefundRecord> {

    /**
     * 统计各退款状态的记录数量
     * @return 统计结果
     */
    @Select("SELECT status, COUNT(*) as count FROM refund_records GROUP BY status")
    List<Map<String, Object>> countByStatus();

    /**
     * 查询用户的退款记录统计
     * @param userId 用户ID
     * @return 统计结果
     */
    @Select("SELECT status, COUNT(*) as count, COALESCE(SUM(refund_amount), 0) as total_amount " +
            "FROM refund_records WHERE user_id = #{userId} GROUP BY status")
    List<Map<String, Object>> countByUserIdAndStatus(@Param("userId") Long userId);

    /**
     * 查询指定时间范围内的退款成功总金额
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 总金额
     */
    @Select("SELECT COALESCE(SUM(refund_amount), 0) FROM refund_records " +
            "WHERE status = 1 AND success_time BETWEEN #{startTime} AND #{endTime}")
    BigDecimal sumSuccessAmountByTimeRange(@Param("startTime") String startTime, 
                                           @Param("endTime") String endTime);

    /**
     * 按日期统计退款成功的订单数和金额
     * @param days 统计最近N天
     * @return 统计结果
     */
    @Select("SELECT DATE(success_time) as date, COUNT(*) as count, COALESCE(SUM(refund_amount), 0) as total_amount " +
            "FROM refund_records WHERE status = 1 AND success_time >= NOW() - INTERVAL '#{days} days' " +
            "GROUP BY DATE(success_time) ORDER BY date DESC")
    List<Map<String, Object>> dailySuccessStats(@Param("days") int days);

    /**
     * 查询退款中超时的记录（创建超过指定小时数仍在退款中）
     * @param hours 超时小时数
     * @return 超时记录列表
     */
    @Select("SELECT * FROM refund_records WHERE status = 0 " +
            "AND created_at < NOW() - INTERVAL '#{hours} hours'")
    List<RefundRecord> findTimeoutRefunds(@Param("hours") int hours);

    /**
     * 统计退款原因分布
     * @return 统计结果
     */
    @Select("SELECT refund_reason, COUNT(*) as count FROM refund_records " +
            "WHERE refund_reason IS NOT NULL AND refund_reason != '' " +
            "GROUP BY refund_reason ORDER BY count DESC LIMIT 10")
    List<Map<String, Object>> countByRefundReason();
}
