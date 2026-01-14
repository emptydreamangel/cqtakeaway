package com.blue.jitian.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.jitian.Entity.Order;
import com.blue.jitian.Mapper.OrderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;


@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> {

    /**
     * 生成订单号
     * 格式：yyyyMMddHHmmss + 6位随机数
     * @return 订单号
     */
    public String generateOrderNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomNum = String.format("%06d", new Random().nextInt(1000000));
        return timestamp + randomNum;
    }

    /**
     * 根据订单号查询订单
     * @param orderNo 订单号
     * @return 订单对象
     */
    public Order getByOrderNo(String orderNo) {
        return this.baseMapper.findByOrderNo(orderNo);
    }

    /**
     * 根据用户ID查询订单列表
     * @param userId 用户ID
     * @return 订单列表
     */
    public List<Order> getOrdersByUserId(Long userId) {
        return this.baseMapper.findByUserId(userId);
    }

    /**
     * 根据用户ID分页查询订单
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    public IPage<Order> getOrdersByUserIdPage(Long userId, int pageNum, int pageSize) {
        Page<Order> page = new Page<>(pageNum, pageSize);
        return this.baseMapper.findByUserIdPage(page, userId);
    }

    /**
     * 根据商家ID查询订单列表
     * @param shopId 商家ID
     * @return 订单列表
     */
    public List<Order> getOrdersByShopId(Long shopId) {
        return this.baseMapper.findByShopId(shopId);
    }

    /**
     * 根据商家ID分页查询订单
     * @param shopId 商家ID
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    public IPage<Order> getOrdersByShopIdPage(Long shopId, int pageNum, int pageSize) {
        Page<Order> page = new Page<>(pageNum, pageSize);
        return this.baseMapper.findByShopIdPage(page, shopId);
    }

    /**
     * 根据状态查询订单列表
     * @param status 订单状态
     * @return 订单列表
     */
    public List<Order> getOrdersByStatus(Integer status) {
        return this.baseMapper.findByStatus(status);
    }

    /**
     * 根据用户ID和状态查询订单列表
     * @param userId 用户ID
     * @param status 订单状态
     * @return 订单列表
     */
    public List<Order> getOrdersByUserIdAndStatus(Long userId, Integer status) {
        return this.baseMapper.findByUserIdAndStatus(userId, status);
    }

    /**
     * 根据用户ID和状态分页查询订单
     * @param userId 用户ID
     * @param status 订单状态
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    public IPage<Order> getOrdersByUserIdAndStatusPage(Long userId, Integer status, int pageNum, int pageSize) {
        Page<Order> page = new Page<>(pageNum, pageSize);
        return this.baseMapper.findByUserIdAndStatusPage(page, userId, status);
    }

    /**
     * 根据商家ID和状态查询订单列表
     * @param shopId 商家ID
     * @param status 订单状态
     * @return 订单列表
     */
    public List<Order> getOrdersByShopIdAndStatus(Long shopId, Integer status) {
        return this.baseMapper.findByShopIdAndStatus(shopId, status);
    }

    /**
     * 根据商家ID和状态分页查询订单
     * @param shopId 商家ID
     * @param status 订单状态
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    public IPage<Order> getOrdersByShopIdAndStatusPage(Long shopId, Integer status, int pageNum, int pageSize) {
        Page<Order> page = new Page<>(pageNum, pageSize);
        return this.baseMapper.findByShopIdAndStatusPage(page, shopId, status);
    }

    /**
     * 创建订单
     * @param order 订单对象
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean createOrder(Order order) {
        // 生成订单号
        if (order.getOrder_no() == null || order.getOrder_no().isEmpty()) {
            order.setOrder_no(generateOrderNo());
        }
        
        // 设置默认值
        if (order.getStatus() == null) {
            order.setStatus(0);  // 默认待支付
        }
        if (order.getPay_status() == null) {
            order.setPay_status(0);  // 默认未支付
        }
        if (order.getDelivery_fee() == null) {
            order.setDelivery_fee(BigDecimal.ZERO);
        }
        if (order.getPacking_fee() == null) {
            order.setPacking_fee(BigDecimal.ZERO);
        }
        if (order.getDiscount_amount() == null) {
            order.setDiscount_amount(BigDecimal.ZERO);
        }
        if (order.getCoupon_discount() == null) {
            order.setCoupon_discount(BigDecimal.ZERO);
        }
        
        return this.save(order);
    }

    /**
     * 更新订单状态
     * @param orderId 订单ID
     * @param status 订单状态
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrderStatus(Long orderId, Integer status) {
        return this.baseMapper.updateStatus(orderId, status) > 0;
    }

    /**
     * 更新支付状态
     * @param orderId 订单ID
     * @param payStatus 支付状态
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePayStatus(Long orderId, Integer payStatus) {
        return this.baseMapper.updatePayStatus(orderId, payStatus) > 0;
    }

    /**
     * 支付订单
     * @param orderId 订单ID
     * @param payMethod 支付方式
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean payOrder(Long orderId, Integer payMethod) {
        return this.baseMapper.payOrder(orderId, payMethod, LocalDateTime.now()) > 0;
    }

    /**
     * 商家接单
     * @param orderId 订单ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean acceptOrder(Long orderId) {
        Order order = this.getById(orderId);
        if (order == null || order.getStatus() != 1) {
            return false;  // 订单不存在或状态不是待接单
        }
        return this.baseMapper.acceptOrder(orderId, LocalDateTime.now()) > 0;
    }

    /**
     * 配送员接单
     * @param orderId 订单ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean dispatchOrder(Long orderId) {
        Order order = this.getById(orderId);
        if (order == null || order.getStatus() != 2) {
            return false;  // 订单不存在或状态不是待配送
        }
        return this.baseMapper.dispatchOrder(orderId, LocalDateTime.now()) > 0;
    }

    /**
     * 完成订单
     * @param orderId 订单ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean completeOrder(Long orderId) {
        Order order = this.getById(orderId);
        if (order == null || order.getStatus() != 3) {
            return false;  // 订单不存在或状态不是配送中
        }
        return this.baseMapper.completeOrder(orderId, LocalDateTime.now()) > 0;
    }

    /**
     * 取消订单
     * @param orderId 订单ID
     * @param cancelReason 取消原因
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelOrder(Long orderId, String cancelReason) {
        Order order = this.getById(orderId);
        if (order == null || order.getStatus() >= 4) {
            return false;  // 订单不存在或已完成/已取消
        }
        return this.baseMapper.cancelOrder(orderId, LocalDateTime.now(), cancelReason) > 0;
    }

    /**
     * 统计用户订单数量
     * @param userId 用户ID
     * @return 数量
     */
    public long countByUserId(Long userId) {
        return this.baseMapper.countByUserId(userId);
    }

    /**
     * 统计用户指定状态的订单数量
     * @param userId 用户ID
     * @param status 订单状态
     * @return 数量
     */
    public long countByUserIdAndStatus(Long userId, Integer status) {
        return this.baseMapper.countByUserIdAndStatus(userId, status);
    }

    /**
     * 统计商家订单数量
     * @param shopId 商家ID
     * @return 数量
     */
    public long countByShopId(Long shopId) {
        return this.baseMapper.countByShopId(shopId);
    }

    /**
     * 统计商家指定状态的订单数量
     * @param shopId 商家ID
     * @param status 订单状态
     * @return 数量
     */
    public long countByShopIdAndStatus(Long shopId, Integer status) {
        return this.baseMapper.countByShopIdAndStatus(shopId, status);
    }

    /**
     * 查询时间范围内的订单
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 订单列表
     */
    public List<Order> getOrdersByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return this.baseMapper.findByTimeRange(startTime, endTime);
    }

    /**
     * 查询用户时间范围内的订单
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 订单列表
     */
    public List<Order> getOrdersByUserIdAndTimeRange(Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        return this.baseMapper.findByUserIdAndTimeRange(userId, startTime, endTime);
    }

    /**
     * 查询商家时间范围内的订单
     * @param shopId 商家ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 订单列表
     */
    public List<Order> getOrdersByShopIdAndTimeRange(Long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        return this.baseMapper.findByShopIdAndTimeRange(shopId, startTime, endTime);
    }
}
