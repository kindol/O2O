package com.kindol.o2o.service;

import com.kindol.o2o.Exceptions.ProductCategoryOperationException;
import com.kindol.o2o.dto.ProductCategoryExecution;
import com.kindol.o2o.entity.ProductCategory;
import com.kindol.o2o.entity.ShopCategory;

import java.util.List;

public interface ShopCategoryService {

    String SC_LIST_KEY = "shopcategorylist";
    /**
     * 根据查询条件获取ShopCategory列表
     * @param shopCategoryCondition
     * @return
     */
    List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);
}
