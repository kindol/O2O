package com.kindol.o2o.service;

import com.kindol.o2o.BaseTest;
import com.kindol.o2o.dto.ImageHolder;
import com.kindol.o2o.dto.ShopExecution;
import com.kindol.o2o.entity.Area;
import com.kindol.o2o.entity.PersonInfo;
import com.kindol.o2o.entity.Shop;
import com.kindol.o2o.entity.ShopCategory;
import com.kindol.o2o.enums.ShopStateEnum;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class ShopServiceTest extends BaseTest {

    @Autowired
    private ShopService shopService;

    @Test
    public void testAddShop() throws FileNotFoundException {
        Shop shop = new Shop();
        PersonInfo owner = new PersonInfo();
        Area area = new Area();
        ShopCategory shopCategory = new ShopCategory();

        owner.setUserId(1L);
        area.setAreaId(2);
        shopCategory.setShopCategoryId(22L);

        shop.setOwner(owner);
        shop.setArea(area);
        shop.setShopCategory(shopCategory);
        shop.setShopName("亮茶店铺");

        shop.setShopDesc("亮茶店铺");
        shop.setShopAddr("亮茶店铺");
        shop.setPhone("96548325542");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(ShopStateEnum.PASS.getState());
        shop.setAdvice("营业");

        File shopImg = new File("C:/Users/user/Pictures/sunset.jpg");
        InputStream is = new FileInputStream(shopImg);
        ShopExecution se = shopService.addShop(shop, new ImageHolder(shopImg.getName(), is));
        assertEquals(ShopStateEnum.CHECK.getState(), se.getState());
    }

    @Test
    @Ignore
    public void testModifyShop(){
        Shop shop = new Shop();
        shop.setShopId(1l);
        shop.setShopName("修改后的店铺名称");
        File shopImg = new File("D:/headImg.jpg");
        InputStream is = null;
        try {
            is = new FileInputStream(shopImg);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ShopExecution shopExecution = shopService.modifyShop(shop, new ImageHolder( "headImg.jpg", is));
        System.out.println(shopExecution.getShop().getShopImg());
    }

    @Test
    @Ignore
    public void testGetShopList(){
       Shop shopCondition = new Shop();
       ShopCategory shopCategory = new ShopCategory();
       shopCategory.setShopCategoryId(3l);
       shopCondition.setShopCategory(shopCategory);
       ShopExecution shopExecution = shopService.getShopList(shopCondition, 2, 2);
        System.out.println("店铺列表数为：" + shopExecution.getShopList().size());
        System.out.println("店铺总数为：" + shopExecution.getCount());
    }
}
