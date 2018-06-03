package com.kindol.o2o.dto;

import com.kindol.o2o.entity.Product;
import com.kindol.o2o.enums.ProductStateEnum;

import java.util.List;

public class ProductExecution {

    //操作状态
    private int state;
    //状态信息
    private String stateInfo;

    //商品数量
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    //操作的product（增删改查用）
    private Product product;

    //操作的product列表
    private List<Product> productList;

    public ProductExecution() {
    }

    //失败时使用的构造器
    public ProductExecution(ProductStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    //成功时候使用的构造器，针对单个商品
    public ProductExecution(ProductStateEnum stateEnum, Product product) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.product = product;
    }

    //成功时候使用的构造器，针对多个商品

    public ProductExecution(int state, String stateInfo, List<Product> productList) {
        this.state = state;
        this.stateInfo = stateInfo;
        this.productList = productList;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
