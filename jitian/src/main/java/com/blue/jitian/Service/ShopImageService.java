package com.blue.jitian.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.jitian.Entity.ShopImage;
import com.blue.jitian.Mapper.ShopImageMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class ShopImageService extends ServiceImpl<ShopImageMapper, ShopImage> {

    /**
     * 根据商家ID查询所有图片
     * @param shopId 商家ID
     * @return 图片列表
     */
    public List<ShopImage> getImagesByShopId(Long shopId) {
        return this.baseMapper.findByShopId(shopId);
    }

    /**
     * 根据商家ID和图片类型查询
     * @param shopId 商家ID
     * @param imageType 图片类型
     * @return 图片列表
     */
    public List<ShopImage> getImagesByShopIdAndType(Long shopId, Integer imageType) {
        return this.baseMapper.findByShopIdAndType(shopId, imageType);
    }

    /**
     * 查询商家的环境图
     * @param shopId 商家ID
     * @return 环境图列表
     */
    public List<ShopImage> getEnvironmentImages(Long shopId) {
        return this.baseMapper.findEnvironmentImages(shopId);
    }

    /**
     * 查询商家的菜品图
     * @param shopId 商家ID
     * @return 菜品图列表
     */
    public List<ShopImage> getDishImages(Long shopId) {
        return this.baseMapper.findDishImages(shopId);
    }

    /**
     * 添加图片
     * @param image 图片对象
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean addImage(ShopImage image) {
        // 如果没有设置排序，自动设置为最大值+1
        if (image.getSort_order() == null) {
            Integer maxSort = this.baseMapper.getMaxSortOrder(image.getShop_id());
            image.setSort_order(maxSort + 1);
        }
        
        return this.save(image);
    }

    /**
     * 批量添加图片
     * @param images 图片列表
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean batchAddImages(List<ShopImage> images) {
        if (images == null || images.isEmpty()) {
            return false;
        }
        
        // 为没有设置排序的图片自动设置排序
        Long shopId = images.get(0).getShop_id();
        Integer maxSort = this.baseMapper.getMaxSortOrder(shopId);
        
        for (ShopImage image : images) {
            if (image.getSort_order() == null) {
                maxSort++;
                image.setSort_order(maxSort);
            }
        }
        
        return this.baseMapper.batchInsert(images) > 0;
    }

    /**
     * 更新图片
     * @param image 图片对象
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateImage(ShopImage image) {
        if (image.getImage_id() == null) {
            return false;
        }
        return this.updateById(image);
    }

    /**
     * 更新图片排序
     * @param imageId 图片ID
     * @param sortOrder 排序值
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSortOrder(Long imageId, Integer sortOrder) {
        return this.baseMapper.updateSortOrder(imageId, sortOrder) > 0;
    }

    /**
     * 批量更新排序
     * @param images 图片列表（包含id和sortOrder）
     * @return 更新成功的数量
     */
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdateSortOrder(List<ShopImage> images) {
        int count = 0;
        for (ShopImage image : images) {
            if (updateSortOrder(image.getImage_id(), image.getSort_order())) {
                count++;
            }
        }
        return count;
    }

    /**
     * 删除图片
     * @param imageId 图片ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteImage(Long imageId) {
        return this.removeById(imageId);
    }

    /**
     * 批量删除图片
     * @param imageIds 图片ID列表
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDelete(List<Long> imageIds) {
        return this.removeByIds(imageIds);
    }

    /**
     * 删除商家的所有图片
     * @param shopId 商家ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByShopId(Long shopId) {
        return this.baseMapper.deleteByShopId(shopId) > 0;
    }

    /**
     * 删除商家指定类型的所有图片
     * @param shopId 商家ID
     * @param imageType 图片类型
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByShopIdAndType(Long shopId, Integer imageType) {
        return this.baseMapper.deleteByShopIdAndType(shopId, imageType) > 0;
    }

    /**
     * 统计商家图片数量
     * @param shopId 商家ID
     * @return 数量
     */
    public long countByShopId(Long shopId) {
        return this.baseMapper.countByShopId(shopId);
    }

    /**
     * 统计商家指定类型的图片数量
     * @param shopId 商家ID
     * @param imageType 图片类型
     * @return 数量
     */
    public long countByShopIdAndType(Long shopId, Integer imageType) {
        return this.baseMapper.countByShopIdAndType(shopId, imageType);
    }

    /**
     * 获取商家图片的最大排序值
     * @param shopId 商家ID
     * @return 最大排序值
     */
    public Integer getMaxSortOrder(Long shopId) {
        return this.baseMapper.getMaxSortOrder(shopId);
    }
}
