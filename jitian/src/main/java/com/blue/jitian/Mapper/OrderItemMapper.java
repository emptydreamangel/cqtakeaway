package com.blue.jitian.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blue.jitian.Entity.OrderItem;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;


@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {

    /**
     * 根据订单ID查询所有订单明细
     * @param orderId 订单ID
     * @return 订单明细列表
     */
    @Select("SELECT * FROM order_items WHERE order_id = #{orderId} ORDER BY created_at ASC")
    List<OrderItem> findByOrderId(@Param("orderId") Long orderId);

    /**
     * 根据商品ID查询订单明细
     * @param productId 商品ID
     * @return 订单明细列表
     */
    @Select("SELECT * FROM order_items WHERE product_id = #{productId} ORDER BY created_at DESC")
    List<OrderItem> findByProductId(@Param("productId") Long productId);

    /**
     * 统计订单中的商品数量
     * @param orderId 订单ID
     * @return 商品数量
     */
    @Select("SELECT COUNT(*) FROM order_items WHERE order_id = #{orderId}")
    long countByOrderId(@Param("orderId") Long orderId);

    /**
     * 计算订单商品总金额
     * @param orderId 订单ID
     * @return 总金额
     */
    @Select("SELECT COALESCE(SUM(subtotal), 0) FROM order_items WHERE order_id = #{orderId}")
    BigDecimal sumSubtotalByOrderId(@Param("orderId") Long orderId);

    /**
     * 删除订单的所有明细
     * @param orderId 订单ID
     * @return 影响行数
     */
    @Delete("DELETE FROM order_items WHERE order_id = #{orderId}")
    int deleteByOrderId(@Param("orderId") Long orderId);

    /**
     * 批量插入订单明细
     * @param items 订单明细列表
     * @return 影响行数
     */
    @Insert("<script>" +
            "INSERT INTO order_items (order_id, product_id, product_name, product_image, product_price, quantity, spec_name, options, subtotal) VALUES " +
            "<foreach collection='items' item='item' separator=','>" +
            "(#{item.order_id}, #{item.product_id}, #{item.product_name}, #{item.product_image}, #{item.product_price}, #{item.quantity}, #{item.spec_name}, #{item.options}, #{item.subtotal})" +
            "</foreach>" +
            "</script>")
    int batchInsert(@Param("items") List<OrderItem> items);

    /**
     * 统计商品的销售数量
     * @param productId 商品ID
     * @return 销售数量
     */
    @Select("SELECT COALESCE(SUM(quantity), 0) FROM order_items WHERE product_id = #{productId}")
    int sumQuantityByProductId(@Param("productId") Long productId);

    /**
     * 查询指定商品在订单中的明细
     * @param orderId 订单ID
     * @param productId 商品ID
     * @return 订单明细
     */
    @Select("SELECT * FROM order_items WHERE order_id = #{orderId} AND product_id = #{productId}")
    OrderItem findByOrderIdAndProductId(@Param("orderId") Long orderId, @Param("productId") Long productId);

    /**
     * 更新订单明细数量和小计
     * @param itemId 明细ID
     * @param quantity 数量
     * @param subtotal 小计
     * @return 影响行数
     */
    @Update("UPDATE order_items SET quantity = #{quantity}, subtotal = #{subtotal} WHERE item_id = #{itemId}")
    int updateQuantityAndSubtotal(@Param("itemId") Long itemId, @Param("quantity") Integer quantity, @Param("subtotal") BigDecimal subtotal);
}
