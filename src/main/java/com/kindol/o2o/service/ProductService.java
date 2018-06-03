package com.kindol.o2o.service;

import com.kindol.o2o.Exceptions.ProductOperationException;
import com.kindol.o2o.dto.ImageHolder;
import com.kindol.o2o.dto.ProductExecution;
import com.kindol.o2o.entity.Product;

import java.util.List;

public interface ProductService {
    /**
     * 添加商品信息以及图片处理
     * @param product 商品
     * @param thumbnail 缩略图（包括图片和图片名字）
     * @param imageHolderList 详情图片（包括图片和图片名字，和商品是一对多的关系）
     * @return
     * @throws ProductOperationException
     */
    ProductExecution addProduct(Product product, ImageHolder thumbnail,
                                List<ImageHolder> imageHolderList) throws ProductOperationException;

    /**
     * 通过id索引product，get直接返回实体类，不需要状态表明
     * @param productId
     * @return
     */
    Product getProdcutById(long productId);

    /**
     * 修改商品信息以及图片处理
     * @param product
     * @param thumbnail
     * @param imageHolderList
     * @return
     * @throws ProductOperationException
     */
    ProductExecution modifyProduct(Product product, ImageHolder thumbnail,
                                List<ImageHolder> imageHolderList) throws ProductOperationException;

    /**
     * 商品列表分页，可输入的条件有：商品名（模糊），商品状态，店铺ID，商品类别
     * @param productCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize);
}
