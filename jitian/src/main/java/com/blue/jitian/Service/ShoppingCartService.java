package com.blue.jitian.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.jitian.Entity.ShoppingCart;
import com.blue.jitian.Mapper.ShoppingCartMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class ShoppingCartService extends ServiceImpl<ShoppingCartMapper, ShoppingCart> {

    /**
     * 根据用户ID查询购物车列表
     * @param userId 用户ID
     * @return 购物车列表
     */
    public List<ShoppingCart> getCartsByUserId(Long userId) {
        return this.baseMapper.findByUserId(userId);
    }

    /**
     * 根据用户ID和商家ID查询购物车列表
     * @param userId 用户ID
     * @param shopId 商家ID
     * @return 购物车列表
     */
    public List<ShoppingCart> getCartsByUserIdAndShopId(Long userId, Long shopId) {
        return this.baseMapper.findByUserIdAndShopId(userId, shopId);
    }

    /**
     * 查询具体的购物车项
     * @param userId 用户ID
     * @param shopId 商家ID
     * @param productId 商品ID
     * @param specId 规格ID
     * @return 购物车项
     */
    public ShoppingCart getCartByUserProductSpec(Long userId, Long shopId, Long productId, Long specId) {
        return this.baseMapper.findByUserProductSpec(userId, shopId, productId, specId);
    }

    /**
     * 添加商品到购物车
     * @param cart 购物车对象
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean addToCart(ShoppingCart cart) {
        // 检查是否已存在相同的商品+规格
        ShoppingCart existing = this.baseMapper.findByUserProductSpec(
            cart.getUser_id(), 
            cart.getShop_id(), 
            cart.getProduct_id(), 
            cart.getSpec_id()
        );
        
        if (existing != null) {
            // 已存在，增加数量
            int newQuantity = existing.getQuantity() + (cart.getQuantity() != null ? cart.getQuantity() : 1);
            return this.baseMapper.updateQuantity(existing.getCart_id(), newQuantity) > 0;
        } else {
            // 不存在，新增
            if (cart.getQuantity() == null || cart.getQuantity() <= 0) {
                cart.setQuantity(1);
            }
            return this.save(cart);
        }
    }

    /**
     * 更新购物车数量
     * @param cartId 购物车ID
     * @param quantity 数量
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateQuantity(Long cartId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            return false;
        }
        return this.baseMapper.updateQuantity(cartId, quantity) > 0;
    }

    /**
     * 增加购物车数量
     * @param cartId 购物车ID
     * @param increment 增加的数量
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean incrementQuantity(Long cartId, Integer increment) {
        if (increment == null || increment <= 0) {
            return false;
        }
        return this.baseMapper.incrementQuantity(cartId, increment) > 0;
    }

    /**
     * 减少购物车数量
     * @param cartId 购物车ID
     * @param decrement 减少的数量
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean decrementQuantity(Long cartId, Integer decrement) {
        if (decrement == null || decrement <= 0) {
            return false;
        }
        
        ShoppingCart cart = this.getById(cartId);
        if (cart == null) {
            return false;
        }
        
        // 如果减少后数量<=0，直接删除
        if (cart.getQuantity() <= decrement) {
            return this.removeById(cartId);
        }
        
        return this.baseMapper.decrementQuantity(cartId, decrement) > 0;
    }

    /**
     * 删除购物车项
     * @param cartId 购物车ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCart(Long cartId) {
        return this.removeById(cartId);
    }

    /**
     * 批量删除购物车项
     * @param cartIds 购物车ID列表
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDelete(List<Long> cartIds) {
        if (cartIds == null || cartIds.isEmpty()) {
            return false;
        }
        return this.baseMapper.batchDelete(cartIds) > 0;
    }

    /**
     * 清空用户购物车
     * @param userId 用户ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean clearCart(Long userId) {
        return this.baseMapper.deleteByUserId(userId) > 0;
    }

    /**
     * 清空用户在指定商家的购物车
     * @param userId 用户ID
     * @param shopId 商家ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean clearCartByShop(Long userId, Long shopId) {
        return this.baseMapper.deleteByUserIdAndShopId(userId, shopId) > 0;
    }

    /**
     * 统计用户购物车项数量
     * @param userId 用户ID
     * @return 数量
     */
    public long countByUserId(Long userId) {
        return this.baseMapper.countByUserId(userId);
    }

    /**
     * 统计用户在指定商家的购物车项数量
     * @param userId 用户ID
     * @param shopId 商家ID
     * @return 数量
     */
    public long countByUserIdAndShopId(Long userId, Long shopId) {
        return this.baseMapper.countByUserIdAndShopId(userId, shopId);
    }

    /**
     * 获取用户购物车中的商家列表
     * @param userId 用户ID
     * @return 商家ID列表
     */
    public List<Long> getShopIdsByUserId(Long userId) {
        return this.baseMapper.findShopIdsByUserId(userId);
    }

    /**
     * 检查购物车项是否存在
     * @param userId 用户ID
     * @param productId 商品ID
     * @param specId 规格ID
     * @return 是否存在
     */
    public boolean isCartItemExist(Long userId, Long productId, Long specId) {
        return this.baseMapper.countByUserProductSpec(userId, productId, specId) > 0;
    }
}
