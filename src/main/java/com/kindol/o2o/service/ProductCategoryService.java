package com.kindol.o2o.service;

import com.kindol.o2o.Exceptions.ProductCategoryOperationException;
import com.kindol.o2o.dto.ProductCategoryExecution;
import com.kindol.o2o.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryService {

    /**
     * 查询指定店铺下所有的商品类别信息
     * @param shopId
     * @return
     */
    List<ProductCategory> getProductCategoryList(long shopId);

    /**
     *
     * @param productCategoryList
     * @return
     * @throws ProductCategoryOperationException
     */
    ProductCategoryExecution batchInsertProductCategory(List<ProductCategory> productCategoryList)
            throws ProductCategoryOperationException;

    /**
     * 将此类别下的商品里的类别ID置为空（重要！），再删除掉该商品类别
     * @param productCategoryId
     * @param shopId
     * @return
     * @throws ProductCategoryOperationException
     */
    ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
            throws ProductCategoryOperationException;
}
