package com.blue.jitian.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blue.jitian.Entity.ProductSpec;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface ProductSpecMapper extends BaseMapper<ProductSpec> {

    /**
     * 根据商品ID查询所有规格
     * @param productId 商品ID
     * @return 规格列表
     */
    @Select("SELECT * FROM product_specs WHERE product_id = #{productId} ORDER BY sort_order ASC, spec_id ASC")
    List<ProductSpec> findByProductId(@Param("productId") Long productId);

    /**
     * 根据商品ID和规格名称查询
     * @param productId 商品ID
     * @param specName 规格名称
     * @return 规格对象
     */
    @Select("SELECT * FROM product_specs WHERE product_id = #{productId} AND spec_name = #{specName}")
    ProductSpec findByProductIdAndName(@Param("productId") Long productId, @Param("specName") String specName);

    /**
     * 检查规格名称是否存在（同一商品下）
     * @param productId 商品ID
     * @param specName 规格名称
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM product_specs WHERE product_id = #{productId} AND spec_name = #{specName}")
    long countByProductIdAndName(@Param("productId") Long productId, @Param("specName") String specName);

    /**
     * 检查规格名称是否存在（排除指定ID）
     * @param productId 商品ID
     * @param specName 规格名称
     * @param specId 要排除的规格ID
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM product_specs WHERE product_id = #{productId} AND spec_name = #{specName} AND spec_id != #{specId}")
    long countByProductIdAndNameExcludeId(@Param("productId") Long productId, @Param("specName") String specName, @Param("specId") Long specId);

    /**
     * 统计商品的规格数量
     * @param productId 商品ID
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM product_specs WHERE product_id = #{productId}")
    long countByProductId(@Param("productId") Long productId);

    /**
     * 删除商品的所有规格
     * @param productId 商品ID
     * @return 影响行数
     */
    @Delete("DELETE FROM product_specs WHERE product_id = #{productId}")
    int deleteByProductId(@Param("productId") Long productId);

    /**
     * 更新排序
     * @param specId 规格ID
     * @param sortOrder 排序值
     * @return 影响行数
     */
    @Update("UPDATE product_specs SET sort_order = #{sortOrder} WHERE spec_id = #{specId}")
    int updateSortOrder(@Param("specId") Long specId, @Param("sortOrder") Integer sortOrder);

    /**
     * 减少库存
     * @param specId 规格ID
     * @param decrement 减少的数量
     * @return 影响行数
     */
    @Update("UPDATE product_specs SET stock = stock - #{decrement} WHERE spec_id = #{specId} AND (stock >= #{decrement} OR stock = -1)")
    int decrementStock(@Param("specId") Long specId, @Param("decrement") Integer decrement);

    /**
     * 增加库存
     * @param specId 规格ID
     * @param increment 增加的数量
     * @return 影响行数
     */
    @Update("UPDATE product_specs SET stock = stock + #{increment} WHERE spec_id = #{specId}")
    int incrementStock(@Param("specId") Long specId, @Param("increment") Integer increment);

    /**
     * 获取商品规格的最大排序值
     * @param productId 商品ID
     * @return 最大排序值
     */
    @Select("SELECT COALESCE(MAX(sort_order), 0) FROM product_specs WHERE product_id = #{productId}")
    Integer getMaxSortOrder(@Param("productId") Long productId);

    /**
     * 批量插入规格
     * @param specs 规格列表
     * @return 影响行数
     */
    @Insert("<script>" +
            "INSERT INTO product_specs (product_id, spec_name, price_add, stock, sort_order) VALUES " +
            "<foreach collection='specs' item='spec' separator=','>" +
            "(#{spec.product_id}, #{spec.spec_name}, #{spec.price_add}, #{spec.stock}, #{spec.sort_order})" +
            "</foreach>" +
            "</script>")
    int batchInsert(@Param("specs") List<ProductSpec> specs);
}
