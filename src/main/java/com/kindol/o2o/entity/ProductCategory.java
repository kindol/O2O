package com.kindol.o2o.entity;

/*
商品类别
 */

import java.util.Date;

public class ProductCategory {

    private Long productCategoryId;
    private Long shopId;                        //商铺ID，不适用shop实体类，因为不符合逻辑，而且只需要表明是属于哪个店铺的即可
    private String productCategoryName;
    private Integer priority;                   //展示权值，越大排得越前
    private Date createTime;

    public Long getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getProductCategoryName() {
        return productCategoryName;
    }

    public void setProductCategoryName(String productCategoryName) {
        this.productCategoryName = productCategoryName;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
