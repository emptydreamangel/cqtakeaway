package com.blue.jitian.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.jitian.Entity.OrderItem;
import com.blue.jitian.Mapper.OrderItemMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


@Service
public class OrderItemService extends ServiceImpl<OrderItemMapper, OrderItem> {

    /**
     * 根据订单ID查询所有订单明细
     * @param orderId 订单ID
     * @return 订单明细列表
     */
    public List<OrderItem> getItemsByOrderId(Long orderId) {
        return this.baseMapper.findByOrderId(orderId);
    }

    /**
     * 根据商品ID查询订单明细
     * @param productId 商品ID
     * @return 订单明细列表
     */
    public List<OrderItem> getItemsByProductId(Long productId) {
        return this.baseMapper.findByProductId(productId);
    }

    /**
     * 查询指定商品在订单中的明细
     * @param orderId 订单ID
     * @param productId 商品ID
     * @return 订单明细
     */
    public OrderItem getByOrderIdAndProductId(Long orderId, Long productId) {
        return this.baseMapper.findByOrderIdAndProductId(orderId, productId);
    }

    /**
     * 添加订单明细
     * @param item 订单明细对象
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean addOrderItem(OrderItem item) {
        // 计算小计
        if (item.getSubtotal() == null && item.getProduct_price() != null && item.getQuantity() != null) {
            item.setSubtotal(item.getProduct_price().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        return this.save(item);
    }

    /**
     * 批量添加订单明细
     * @param items 订单明细列表
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean batchAddOrderItems(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            return false;
        }
        
        // 为每个明细计算小计
        for (OrderItem item : items) {
            if (item.getSubtotal() == null && item.getProduct_price() != null && item.getQuantity() != null) {
                item.setSubtotal(item.getProduct_price().multiply(BigDecimal.valueOf(item.getQuantity())));
            }
        }
        
        return this.baseMapper.batchInsert(items) > 0;
    }

    /**
     * 更新订单明细
     * @param item 订单明细对象
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrderItem(OrderItem item) {
        if (item.getItem_id() == null) {
            return false;
        }
        
        // 重新计算小计
        if (item.getProduct_price() != null && item.getQuantity() != null) {
            item.setSubtotal(item.getProduct_price().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        
        return this.updateById(item);
    }

    /**
     * 更新订单明细数量和小计
     * @param itemId 明细ID
     * @param quantity 数量
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateQuantity(Long itemId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            return false;
        }
        
        OrderItem item = this.getById(itemId);
        if (item == null) {
            return false;
        }
        
        BigDecimal subtotal = item.getProduct_price().multiply(BigDecimal.valueOf(quantity));
        return this.baseMapper.updateQuantityAndSubtotal(itemId, quantity, subtotal) > 0;
    }

    /**
     * 删除订单明细
     * @param itemId 明细ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteOrderItem(Long itemId) {
        return this.removeById(itemId);
    }

    /**
     * 批量删除订单明细
     * @param itemIds 明细ID列表
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDelete(List<Long> itemIds) {
        return this.removeByIds(itemIds);
    }

    /**
     * 删除订单的所有明细
     * @param orderId 订单ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByOrderId(Long orderId) {
        return this.baseMapper.deleteByOrderId(orderId) > 0;
    }

    /**
     * 统计订单中的商品数量
     * @param orderId 订单ID
     * @return 商品数量
     */
    public long countByOrderId(Long orderId) {
        return this.baseMapper.countByOrderId(orderId);
    }

    /**
     * 计算订单商品总金额
     * @param orderId 订单ID
     * @return 总金额
     */
    public BigDecimal sumSubtotalByOrderId(Long orderId) {
        return this.baseMapper.sumSubtotalByOrderId(orderId);
    }

    /**
     * 统计商品的销售数量
     * @param productId 商品ID
     * @return 销售数量
     */
    public int sumQuantityByProductId(Long productId) {
        return this.baseMapper.sumQuantityByProductId(productId);
    }
}
