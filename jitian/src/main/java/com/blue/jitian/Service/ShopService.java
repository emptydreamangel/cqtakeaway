package com.blue.jitian.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.jitian.Entity.Shop;
import com.blue.jitian.Mapper.ShopMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;


@Service
public class ShopService extends ServiceImpl<ShopMapper, Shop> {

    /**
     * 根据分类ID查询商家
     * @param categoryId 分类ID
     * @return 商家列表
     */
    public List<Shop> getShopsByCategoryId(Integer categoryId) {
        return this.baseMapper.findByCategoryId(categoryId);
    }

    /**
     * 根据状态查询商家
     * @param status 状态
     * @return 商家列表
     */
    public List<Shop> getShopsByStatus(Integer status) {
        return this.baseMapper.findByStatus(status);
    }

    /**
     * 查询营业中的商家
     * @return 商家列表
     */
    public List<Shop> getBusinessShops() {
        return this.baseMapper.findBusinessShops();
    }

    /**
     * 根据名称模糊查询商家
     * @param shopName 商家名称
     * @return 商家列表
     */
    public List<Shop> searchShopsByName(String shopName) {
        if (!StringUtils.hasText(shopName)) {
            return List.of();
        }
        return this.baseMapper.findByNameLike(shopName);
    }

    /**
     * 根据地区查询商家
     * @param province 省份
     * @param city 城市
     * @param district 区县
     * @return 商家列表
     */
    public List<Shop> getShopsByRegion(String province, String city, String district) {
        return this.baseMapper.findByRegion(province, city, district);
    }

    /**
     * 根据位置范围查询商家
     * @param minLongitude 最小经度
     * @param maxLongitude 最大经度
     * @param minLatitude 最小纬度
     * @param maxLatitude 最大纬度
     * @return 商家列表
     */
    public List<Shop> getShopsByLocationRange(BigDecimal minLongitude, BigDecimal maxLongitude,
                                               BigDecimal minLatitude, BigDecimal maxLatitude) {
        return this.baseMapper.findByLocationRange(minLongitude, maxLongitude, minLatitude, maxLatitude);
    }

    /**
     * 添加商家
     * @param shop 商家对象
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean addShop(Shop shop) {
        // 设置默认值
        if (shop.getRating() == null) {
            shop.setRating(new BigDecimal("5.00"));
        }
        if (shop.getSales_count() == null) {
            shop.setSales_count(0);
        }
        if (shop.getStatus() == null) {
            shop.setStatus(1);  // 默认营业中
        }
        if (shop.getIs_auth() == null) {
            shop.setIs_auth(0);  // 默认未认证
        }
        
        return this.save(shop);
    }

    /**
     * 更新商家
     * @param shop 商家对象
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateShop(Shop shop) {
        if (shop.getShop_id() == null) {
            return false;
        }
        return this.updateById(shop);
    }

    /**
     * 更新商家状态
     * @param shopId 商家ID
     * @param status 状态
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Long shopId, Integer status) {
        return this.baseMapper.updateStatus(shopId, status) > 0;
    }

    /**
     * 设置为营业中
     * @param shopId 商家ID
     * @return 是否成功
     */
    public boolean setBusinessStatus(Long shopId) {
        return updateStatus(shopId, 1);
    }

    /**
     * 设置为休息中
     * @param shopId 商家ID
     * @return 是否成功
     */
    public boolean setRestStatus(Long shopId) {
        return updateStatus(shopId, 0);
    }

    /**
     * 设置为打烊
     * @param shopId 商家ID
     * @return 是否成功
     */
    public boolean setClosedStatus(Long shopId) {
        return updateStatus(shopId, 2);
    }

    /**
     * 更新商家认证状态
     * @param shopId 商家ID
     * @param isAuth 认证状态
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAuthStatus(Long shopId, Integer isAuth) {
        return this.baseMapper.updateAuthStatus(shopId, isAuth) > 0;
    }

    /**
     * 认证商家
     * @param shopId 商家ID
     * @return 是否成功
     */
    public boolean authenticateShop(Long shopId) {
        return updateAuthStatus(shopId, 1);
    }

    /**
     * 取消认证
     * @param shopId 商家ID
     * @return 是否成功
     */
    public boolean unauthenticateShop(Long shopId) {
        return updateAuthStatus(shopId, 0);
    }

    /**
     * 增加销量
     * @param shopId 商家ID
     * @param increment 增加的数量
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean incrementSalesCount(Long shopId, Integer increment) {
        if (increment == null || increment <= 0) {
            return false;
        }
        return this.baseMapper.incrementSalesCount(shopId, increment) > 0;
    }

    /**
     * 删除商家
     * @param shopId 商家ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteShop(Long shopId) {
        return this.removeById(shopId);
    }

    /**
     * 批量删除商家
     * @param shopIds 商家ID列表
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDelete(List<Long> shopIds) {
        return this.removeByIds(shopIds);
    }

    /**
     * 统计分类下的商家数量
     * @param categoryId 分类ID
     * @return 数量
     */
    public long countByCategoryId(Integer categoryId) {
        return this.baseMapper.countByCategoryId(categoryId);
    }

    /**
     * 统计营业中的商家数量
     * @return 数量
     */
    public long countBusinessShops() {
        return this.baseMapper.countBusinessShops();
    }

    /**
     * 统计已认证的商家数量
     * @return 数量
     */
    public long countAuthShops() {
        return this.baseMapper.countAuthShops();
    }

    /**
     * 统计总商家数量
     * @return 数量
     */
    public long countTotal() {
        return this.count();
    }

    /**
     * 查询评分最高的商家
     * @param limit 限制数量
     * @return 商家列表
     */
    public List<Shop> getTopRatedShops(Integer limit) {
        return this.baseMapper.findTopRatedShops(limit);
    }

    /**
     * 查询销量最高的商家
     * @param limit 限制数量
     * @return 商家列表
     */
    public List<Shop> getTopSalesShops(Integer limit) {
        return this.baseMapper.findTopSalesShops(limit);
    }

    /**
     * 分页查询商家
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    public Page<Shop> getShopsByPage(Integer pageNum, Integer pageSize) {
        Page<Shop> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Shop> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("created_at");
        return this.page(page, queryWrapper);
    }

    /**
     * 分页查询营业中的商家
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    public Page<Shop> getBusinessShopsByPage(Integer pageNum, Integer pageSize) {
        Page<Shop> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Shop> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        queryWrapper.orderByDesc("rating", "sales_count");
        return this.page(page, queryWrapper);
    }

    /**
     * 分页查询指定分类的商家
     * @param categoryId 分类ID
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    public Page<Shop> getShopsByCategoryIdWithPage(Integer categoryId, Integer pageNum, Integer pageSize) {
        Page<Shop> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Shop> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId);
        queryWrapper.eq("status", 1);
        queryWrapper.orderByDesc("rating", "sales_count");
        return this.page(page, queryWrapper);
    }
}
