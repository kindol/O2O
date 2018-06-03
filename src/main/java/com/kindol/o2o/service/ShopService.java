package com.kindol.o2o.service;

import com.kindol.o2o.dto.ImageHolder;
import com.kindol.o2o.dto.ShopExecution;
import com.kindol.o2o.entity.Shop;

import java.io.InputStream;

public interface ShopService {

    /**
     * 注册店铺信息，包括图片处理
     * @param shop
     * @param thumbnail
     * @return
     */
    ShopExecution addShop(Shop shop, ImageHolder thumbnail);

    /**
     * 通过店铺id获取店铺信息
     * @param shopId
     * @return
     */
    Shop getByShopId(long shopId);

    /**
     * 更新店铺信息，包括对图片的处理
     * @param shop
     * @param thumbnail
     * @return
     */
    ShopExecution modifyShop(Shop shop, ImageHolder thumbnail);

    /**
     * 根据shopCondition分页返回相应列表数据
     * @param shopCondition
     * @param pageIndex 前端只认页数，而后台认行数，所以还需要添加一个工具类进行转换
     * @param pageSize
     * @return
     */
    ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);
}
