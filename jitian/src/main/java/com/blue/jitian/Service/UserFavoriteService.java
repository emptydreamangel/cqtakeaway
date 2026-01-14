package com.blue.jitian.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.jitian.Entity.UserFavorite;
import com.blue.jitian.Mapper.UserFavoriteMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class UserFavoriteService extends ServiceImpl<UserFavoriteMapper, UserFavorite> {

    /**
     * 收藏店铺
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean favoriteShop(Long userId, Long shopId) {
        // 检查是否已收藏
        if (isShopFavorited(userId, shopId)) {
            return false;
        }
        
        UserFavorite favorite = UserFavorite.builder()
                .user_id(userId)
                .shop_id(shopId)
                .type(1)
                .build();
        
        return this.save(favorite);
    }

    /**
     * 收藏商品
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @param productId 商品ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean favoriteProduct(Long userId, Long shopId, Long productId) {
        // 检查是否已收藏
        if (isProductFavorited(userId, shopId, productId)) {
            return false;
        }
        
        UserFavorite favorite = UserFavorite.builder()
                .user_id(userId)
                .shop_id(shopId)
                .product_id(productId)
                .type(2)
                .build();
        
        return this.save(favorite);
    }

    /**
     * 取消收藏店铺
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @return 是否成功
     */
    public boolean unfavoriteShop(Long userId, Long shopId) {
        return this.baseMapper.deleteShopFavorite(userId, shopId) > 0;
    }

    /**
     * 取消收藏商品
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @param productId 商品ID
     * @return 是否成功
     */
    public boolean unfavoriteProduct(Long userId, Long shopId, Long productId) {
        return this.baseMapper.deleteProductFavorite(userId, shopId, productId) > 0;
    }

    /**
     * 检查用户是否收藏了店铺
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @return 是否已收藏
     */
    public boolean isShopFavorited(Long userId, Long shopId) {
        return this.baseMapper.findShopFavorite(userId, shopId) != null;
    }

    /**
     * 检查用户是否收藏了商品
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @param productId 商品ID
     * @return 是否已收藏
     */
    public boolean isProductFavorited(Long userId, Long shopId, Long productId) {
        return this.baseMapper.findProductFavorite(userId, shopId, productId) != null;
    }

    /**
     * 获取用户的所有收藏
     * @param userId 用户ID
     * @return 收藏列表
     */
    public List<UserFavorite> getUserFavorites(Long userId) {
        return this.baseMapper.findByUserId(userId);
    }

    /**
     * 根据类型获取用户收藏
     * @param userId 用户ID
     * @param type 类型（1:店铺，2:商品）
     * @return 收藏列表
     */
    public List<UserFavorite> getUserFavoritesByType(Long userId, Integer type) {
        return this.baseMapper.findByUserIdAndType(userId, type);
    }

    /**
     * 获取用户收藏的店铺列表
     * @param userId 用户ID
     * @return 收藏列表
     */
    public List<UserFavorite> getUserFavoriteShops(Long userId) {
        return this.baseMapper.findShopsByUserId(userId);
    }

    /**
     * 获取用户收藏的商品列表
     * @param userId 用户ID
     * @return 收藏列表
     */
    public List<UserFavorite> getUserFavoriteProducts(Long userId) {
        return this.baseMapper.findProductsByUserId(userId);
    }

    /**
     * 统计用户收藏总数
     * @param userId 用户ID
     * @return 收藏数量
     */
    public long countUserFavorites(Long userId) {
        return this.baseMapper.countByUserId(userId);
    }

    /**
     * 统计用户店铺收藏数量
     * @param userId 用户ID
     * @return 收藏数量
     */
    public long countUserShopFavorites(Long userId) {
        return this.baseMapper.countShopsByUserId(userId);
    }

    /**
     * 统计用户商品收藏数量
     * @param userId 用户ID
     * @return 收藏数量
     */
    public long countUserProductFavorites(Long userId) {
        return this.baseMapper.countProductsByUserId(userId);
    }

    /**
     * 统计店铺被收藏的次数
     * @param shopId 店铺ID
     * @return 收藏次数
     */
    public long countShopFavorites(Long shopId) {
        return this.baseMapper.countShopFavorites(shopId);
    }

    /**
     * 统计商品被收藏的次数
     * @param shopId 店铺ID
     * @param productId 商品ID
     * @return 收藏次数
     */
    public long countProductFavorites(Long shopId, Long productId) {
        return this.baseMapper.countProductFavorites(shopId, productId);
    }

    /**
     * 获取收藏了某店铺的所有用户
     * @param shopId 店铺ID
     * @return 收藏列表
     */
    public List<UserFavorite> getShopFans(Long shopId) {
        return this.baseMapper.findUsersByShopId(shopId);
    }

    /**
     * 删除用户所有收藏
     * @param userId 用户ID
     * @return 删除数量
     */
    public int clearUserFavorites(Long userId) {
        return this.baseMapper.deleteAllByUserId(userId);
    }

    /**
     * 切换店铺收藏状态（已收藏则取消，未收藏则添加）
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @return 操作后的收藏状态（true:已收藏，false:未收藏）
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean toggleShopFavorite(Long userId, Long shopId) {
        if (isShopFavorited(userId, shopId)) {
            unfavoriteShop(userId, shopId);
            return false;
        } else {
            favoriteShop(userId, shopId);
            return true;
        }
    }

    /**
     * 切换商品收藏状态（已收藏则取消，未收藏则添加）
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @param productId 商品ID
     * @return 操作后的收藏状态（true:已收藏，false:未收藏）
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean toggleProductFavorite(Long userId, Long shopId, Long productId) {
        if (isProductFavorited(userId, shopId, productId)) {
            unfavoriteProduct(userId, shopId, productId);
            return false;
        } else {
            favoriteProduct(userId, shopId, productId);
            return true;
        }
    }
}
