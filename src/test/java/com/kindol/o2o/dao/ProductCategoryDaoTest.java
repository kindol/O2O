package com.kindol.o2o.dao;

import com.kindol.o2o.BaseTest;
import com.kindol.o2o.entity.ProductCategory;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @FixMethodOrder 意思是类中执行的测试方法需要按照指定顺序执行
 * NAME_ASCENDING 表示按照方法的名字顺序排序执行
 * DEFAULT 则默认，为不可预期的顺序
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductCategoryDaoTest extends BaseTest {

    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Test
    @Ignore
    public void testQueryById(){
        long shopId = 1;
        List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategory(shopId);
        System.out.println("该店铺自定义类别数为：" + productCategoryList.size());
    }

    @Test
    public void testbatchInsertProductCategory(){
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryName("交通工具");
        productCategory.setPriority(1);
        productCategory.setCreateTime(new Date());
        productCategory.setShopId(23L);

        /*ProductCategory productCategory2 = new ProductCategory();
        productCategory2.setProductCategoryName("夜茶");
        productCategory2.setPriority(2);
        productCategory2.setCreateTime(new Date());
        productCategory2.setShopId(26L);*/

        List<ProductCategory> productCategoryList = new ArrayList<>();
        productCategoryList.add(productCategory);
        //productCategoryList.add(productCategory2);
        int effectNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
        assertEquals(2, effectNum);
    }

    /**
     * 前后一个删除一个添加操作相同数据，
     * 形成了UT（unit test）回环，这样对数据库的数据没有任何影响
     */

    @Test
    public void testDeleteProductCategory(){
        long shopId = 1;
        List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategory(shopId);
        for (ProductCategory pc: productCategoryList){
            if ("商品类别1".equals(pc.getProductCategoryName()) || "商品类别2".equals(pc.getProductCategoryName())){
                int effectedNum = productCategoryDao.deleteProductCategory(pc.getProductCategoryId(), shopId);
                assertEquals(1, effectedNum);
            }
        }
    }
}
