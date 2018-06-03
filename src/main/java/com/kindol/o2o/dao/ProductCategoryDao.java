package com.kindol.o2o.dao;

import com.kindol.o2o.entity.ProductCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductCategoryDao {

    /**
     * 通过shopId查询店铺商品类别
     * @param shopId
     * @return
     */
    List<ProductCategory> queryProductCategory(long shopId);

    /**
     * 批量添加商品类别
     * @return 返回影响的行数
     */
    int batchInsertProductCategory(List<ProductCategory> productCategoryList);

    /**
     * 删除指定商品类别，需要两个参数
     * @param productCategoryId
     * @param shopId
     * @return
     */
    int deleteProductCategory(@Param("productCategoryId")long productCategoryId,
                              @Param("shopId")long shopId);
}
