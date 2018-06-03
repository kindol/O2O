package com.kindol.o2o.dto;

import com.kindol.o2o.entity.Shop;
import com.kindol.o2o.enums.ShopStateEnum;

import java.util.List;

/**
 * 完成service层之前，为了配合前端MVVM，
 * 不能直接使用entity，因为操作实体类的时候，例如添加等，都会有一个状态
 * 如果失败，这些处于什么状态，都是需要记录的，再返回给controller
 */

public class ShopExecution {

    //结果状态
    private int state;
    //状态标识
    private String stateInfo;
    //店铺数量（当不只操作一个店铺的时候）
    private int count;
    //操作的shop（增删改店铺的时候用到）
    private Shop shop;
    //shop列表（查询店铺列表的时候时候用）
    private List<Shop> shopList;

    public ShopExecution() {
    }

    /**
     * 店铺操作失败的时候的构造器，失败的时候只返回状态，不返回对象
     * @param stateEnum
     */
    public ShopExecution(ShopStateEnum stateEnum) {
       this.state = stateEnum.getState();
       this.stateInfo = stateEnum.getStateInfo();
    }

    /**
     * 店铺操作成功的时候的构造器
     * @param stateEnum
     * @param shop 返回的对象（单个）
     */
    public ShopExecution(ShopStateEnum stateEnum, Shop shop) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.shop = shop;
    }

    /**
     * 店铺操作成功的时候的构造器
     * @param stateEnum
     * @param shopList 返回的对象列表
     */
    public ShopExecution(ShopStateEnum stateEnum, List<Shop> shopList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.shopList = shopList;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<Shop> getShopList() {
        return shopList;
    }

    public void setShopList(List<Shop> shopList) {
        this.shopList = shopList;
    }
}
