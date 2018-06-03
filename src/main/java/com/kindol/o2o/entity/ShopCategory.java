package com.kindol.o2o.entity;

/*
店铺类别
 */

import java.util.Date;

public class ShopCategory {

    private Long shopCategoryId;
    private String shopCategoryName;
    private String shopCategoryDesc;        //描述
    private String shopCategoryImg;         //图片连接地址
    private Integer prioriry;
    private Date createTime;
    private Date lastEditTime;
    private ShopCategory parent;            //父类别，比如饮品店和奶茶点的关系，为空表示根类，在数据库表中外键指向自身约束店铺类

    public Long getShopCategoryId() {
        return shopCategoryId;
    }

    public void setShopCategoryId(Long shopCategoryId) {
        this.shopCategoryId = shopCategoryId;
    }

    public String getShopCategoryName() {
        return shopCategoryName;
    }

    public void setShopCategoryName(String shopCategoryName) {
        this.shopCategoryName = shopCategoryName;
    }

    public String getShopCategoryDesc() {
        return shopCategoryDesc;
    }

    public void setShopCategoryDesc(String shopCategoryDesc) {
        this.shopCategoryDesc = shopCategoryDesc;
    }

    public String getShopCategoryImg() {
        return shopCategoryImg;
    }

    public void setShopCategoryImg(String shopCategoryImg) {
        this.shopCategoryImg = shopCategoryImg;
    }

    public Integer getPrioriry() {
        return prioriry;
    }

    public void setPrioriry(Integer prioriry) {
        this.prioriry = prioriry;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(Date lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    public ShopCategory getParent() {
        return parent;
    }

    public void setParent(ShopCategory parent) {
        this.parent = parent;
    }
}
