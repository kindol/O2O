package com.kindol.o2o.service;

import com.kindol.o2o.BaseTest;
import com.kindol.o2o.dto.ImageHolder;
import com.kindol.o2o.dto.ProductExecution;
import com.kindol.o2o.entity.Product;
import com.kindol.o2o.entity.ProductCategory;
import com.kindol.o2o.entity.Shop;
import com.kindol.o2o.enums.ProductStateEnum;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@Service
public class ProductServiceTest extends BaseTest {

    @Autowired
    private ProductService productService;

    @Test
    public void testAddProduct() throws FileNotFoundException {
        Product product = new Product();
        Shop shop = new Shop();
        shop.setShopId(23L);
        ProductCategory pc = new ProductCategory();
        pc.setProductCategoryId(10L);
        product.setShop(shop);
        product.setProductCategory(pc);
        product.setProductName("摩托车2");
        product.setProductDesc("摩托车2");
        product.setPriority(30);
        product.setCreateTime(new Date());
        product.setEnableStatus(ProductStateEnum.SUCCESS.getState());

        File thumbnailFile = new File("F:/20170606085020558290.jpg");
        InputStream is = new FileInputStream(thumbnailFile);
        ImageHolder thumbnail = new ImageHolder(thumbnailFile.getName(), is);

        File productImg1 = new File("F:/2017060608502085437.jpg");
        InputStream is1 = new FileInputStream(productImg1);


        List<ImageHolder> productImgList = new ArrayList<>();
        productImgList.add(new ImageHolder(productImg1.getName(), is1));

        ProductExecution pe = productService.addProduct(product, thumbnail, productImgList);
        assertEquals(ProductStateEnum.SUCCESS.getState(), pe.getState());
    }

    @Test
    @Ignore
    public void testModifyProduct() throws FileNotFoundException {
        //创建shopId为1且productCategoryId为1的商品实例并给其成员变量赋值
        Product product = new Product();
        Shop shop = new Shop();
        shop.setShopId(1L);
        ProductCategory pc = new ProductCategory();
        pc.setProductCategoryId(1L);
        product.setShop(shop);
        product.setProductId(1l);
        product.setProductCategory(pc);
        product.setProductName("正式的商品");
        product.setProductDesc("正式的商品");

        //创建缩略图文件流
        File thumbnailFile = new File("D:/ioc.png");
        InputStream is = new FileInputStream(thumbnailFile);
        ImageHolder thumbnail = new ImageHolder(thumbnailFile.getName(), is);

        File productImg1 = new File("D:/ord.png");
        InputStream is1 = new FileInputStream(productImg1);
        File productImg2 = new File("D:/res.png");
        InputStream is2 = new FileInputStream(productImg2);

        List<ImageHolder> productImgList = new ArrayList<>();
        productImgList.add(new ImageHolder(productImg1.getName(), is1));
        productImgList.add(new ImageHolder(productImg2.getName(), is2));

        ProductExecution pe = productService.modifyProduct(product, thumbnail, productImgList);
        assertEquals(ProductStateEnum.SUCCESS.getState(), pe.getState());
    }
}
