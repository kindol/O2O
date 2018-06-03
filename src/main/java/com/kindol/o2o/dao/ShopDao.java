package com.kindol.o2o.dao;

import com.kindol.o2o.entity.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopDao {

    /**
     * 新增店铺
     * @param shop
     * @return 返回1表示插入成功，-1表示插入失败
     */
    int insertShop(Shop shop);

    /**
     * 更新店铺信息
     * @param shop
     * @return
     */
    int updateShop(Shop shop);

    /**
     * 通过shopId查询店铺信息
     * @param shopId
     * @return
     */
    Shop queryByShopId(long shopId);

    /**
     * 分页查询店铺，可输入的条件有：店铺名（模糊），店铺状态，店铺类别，区域ID，owner，通过此排列组合
     * 当中@Param的作用是由于传入的参数不只有一个，当只有一个的时候mapper文件可以直接使用
     * 而多个则需要进行定义名字
     * @param shopCondition 查询的条件
     * @param rowIndex 从第几行开始取数据
     * @param pageSize 返回的条数
     * @return
     */
    List<Shop> queryShopList(@Param("shopCondition")Shop shopCondition,
                             @Param("rowIndex")int rowIndex, @Param("pageSize")int pageSize);

    /**
     * 返回queryShopList种数（因为queryShopList方法金返回pageSize个，辅助queryShopList方法）
     * @param shopCondition
     * @return
     */
    int queryShopCount(@Param("shopCondition")Shop shopCondition);
}
