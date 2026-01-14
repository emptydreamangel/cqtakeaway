package com.blue.jitian.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.jitian.Entity.ProductSpec;
import com.blue.jitian.Mapper.ProductSpecMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;


@Service
public class ProductSpecService extends ServiceImpl<ProductSpecMapper, ProductSpec> {

    /**
     * 根据商品ID查询所有规格
     * @param productId 商品ID
     * @return 规格列表
     */
    public List<ProductSpec> getSpecsByProductId(Long productId) {
        return this.baseMapper.findByProductId(productId);
    }

    /**
     * 根据商品ID和名称查询规格
     * @param productId 商品ID
     * @param specName 规格名称
     * @return 规格对象
     */
    public ProductSpec getByProductIdAndName(Long productId, String specName) {
        if (!StringUtils.hasText(specName)) {
            return null;
        }
        return this.baseMapper.findByProductIdAndName(productId, specName);
    }

    /**
     * 检查规格名称是否存在（同一商品下）
     * @param productId 商品ID
     * @param specName 规格名称
     * @return 是否存在
     */
    public boolean isNameExist(Long productId, String specName) {
        if (!StringUtils.hasText(specName)) {
            return false;
        }
        return this.baseMapper.countByProductIdAndName(productId, specName) > 0;
    }

    /**
     * 检查规格名称是否存在（排除指定ID）
     * @param productId 商品ID
     * @param specName 规格名称
     * @param specId 要排除的规格ID
     * @return 是否存在
     */
    public boolean isNameExistExcludeId(Long productId, String specName, Long specId) {
        if (!StringUtils.hasText(specName) || specId == null) {
            return false;
        }
        return this.baseMapper.countByProductIdAndNameExcludeId(productId, specName, specId) > 0;
    }

    /**
     * 添加规格
     * @param spec 规格对象
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean addSpec(ProductSpec spec) {
        // 检查名称是否已存在
        if (isNameExist(spec.getProduct_id(), spec.getSpec_name())) {
            return false;
        }
        
        // 设置默认值
        if (spec.getPrice_add() == null) {
            spec.setPrice_add(BigDecimal.ZERO);
        }
        if (spec.getStock() == null) {
            spec.setStock(-1);  // 默认无限库存
        }
        if (spec.getSort_order() == null) {
            Integer maxSort = this.baseMapper.getMaxSortOrder(spec.getProduct_id());
            spec.setSort_order(maxSort + 1);
        }
        
        return this.save(spec);
    }

    /**
     * 批量添加规格
     * @param specs 规格列表
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean batchAddSpecs(List<ProductSpec> specs) {
        if (specs == null || specs.isEmpty()) {
            return false;
        }
        
        // 为没有设置排序的规格自动设置排序
        Long productId = specs.get(0).getProduct_id();
        Integer maxSort = this.baseMapper.getMaxSortOrder(productId);
        
        for (ProductSpec spec : specs) {
            if (spec.getPrice_add() == null) {
                spec.setPrice_add(BigDecimal.ZERO);
            }
            if (spec.getStock() == null) {
                spec.setStock(-1);
            }
            if (spec.getSort_order() == null) {
                maxSort++;
                spec.setSort_order(maxSort);
            }
        }
        
        return this.baseMapper.batchInsert(specs) > 0;
    }

    /**
     * 更新规格
     * @param spec 规格对象
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSpec(ProductSpec spec) {
        if (spec.getSpec_id() == null) {
            return false;
        }
        
        // 检查名称是否与其他规格重复
        if (spec.getSpec_name() != null) {
            ProductSpec existing = this.getById(spec.getSpec_id());
            if (existing != null && 
                isNameExistExcludeId(existing.getProduct_id(), spec.getSpec_name(), spec.getSpec_id())) {
                return false;
            }
        }
        
        return this.updateById(spec);
    }

    /**
     * 更新排序
     * @param specId 规格ID
     * @param sortOrder 排序值
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSortOrder(Long specId, Integer sortOrder) {
        return this.baseMapper.updateSortOrder(specId, sortOrder) > 0;
    }

    /**
     * 批量更新排序
     * @param specs 规格列表（包含id和sortOrder）
     * @return 更新成功的数量
     */
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdateSortOrder(List<ProductSpec> specs) {
        int count = 0;
        for (ProductSpec spec : specs) {
            if (updateSortOrder(spec.getSpec_id(), spec.getSort_order())) {
                count++;
            }
        }
        return count;
    }

    /**
     * 减少库存
     * @param specId 规格ID
     * @param decrement 减少的数量
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean decrementStock(Long specId, Integer decrement) {
        if (decrement == null || decrement <= 0) {
            return false;
        }
        return this.baseMapper.decrementStock(specId, decrement) > 0;
    }

    /**
     * 增加库存
     * @param specId 规格ID
     * @param increment 增加的数量
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean incrementStock(Long specId, Integer increment) {
        if (increment == null || increment <= 0) {
            return false;
        }
        return this.baseMapper.incrementStock(specId, increment) > 0;
    }

    /**
     * 删除规格
     * @param specId 规格ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSpec(Long specId) {
        return this.removeById(specId);
    }

    /**
     * 批量删除规格
     * @param specIds 规格ID列表
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDelete(List<Long> specIds) {
        return this.removeByIds(specIds);
    }

    /**
     * 删除商品的所有规格
     * @param productId 商品ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByProductId(Long productId) {
        return this.baseMapper.deleteByProductId(productId) > 0;
    }

    /**
     * 统计商品的规格数量
     * @param productId 商品ID
     * @return 数量
     */
    public long countByProductId(Long productId) {
        return this.baseMapper.countByProductId(productId);
    }

    /**
     * 获取最大排序值
     * @param productId 商品ID
     * @return 最大排序值
     */
    public Integer getMaxSortOrder(Long productId) {
        return this.baseMapper.getMaxSortOrder(productId);
    }
}
