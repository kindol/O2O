package com.kindol.o2o.service.impl;

import com.kindol.o2o.Exceptions.ShopOperationException;
import com.kindol.o2o.dao.ShopDao;
import com.kindol.o2o.dto.ImageHolder;
import com.kindol.o2o.dto.ShopExecution;
import com.kindol.o2o.entity.Shop;
import com.kindol.o2o.enums.ShopStateEnum;
import com.kindol.o2o.service.ShopService;
import com.kindol.o2o.util.ImageUtil;
import com.kindol.o2o.util.PageCalculator;
import com.kindol.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/*
service关于店铺注册的逻辑：
service需要事务的管理（为了保证事务的原子性，以下如果有不成功的步骤都会导致事务回滚），对于店铺注册
首先需要将店铺信息插入到数据库当中，其次，
返回店铺ID，再根据店铺ID创建出存储图片的文件夹，
在文件夹下处理存储的图片，再将图片地址更新回数据库
 */

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopDao shopDao;

    //@Transactional表明需要事务的支持
    @Override
    @Transactional
    public ShopExecution addShop(Shop shop, ImageHolder thumbnail) {
        //空值判断
        if (shop == null)
            return new ShopExecution(ShopStateEnum.NULL_SHOP);

        try {
            //给店铺信息赋初始值
            shop.setEnableStatus(0);
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());

            //添加店铺信息
            int effectedNum = shopDao.insertShop(shop);
            if (effectedNum <= 0)
                throw new ShopOperationException("创建店铺失败");
            else {
                if (thumbnail.getImage() != null){
                    try {
                        //存储图片
                        addShopImg(shop, thumbnail);
                    }catch (Exception e){
                        throw new ShopOperationException("addShopImg error: " + e.getMessage());
                    }
                    //更新店铺的图片地址
                    effectedNum = shopDao.updateShop(shop);
                    if (effectedNum <= 0){
                        throw new ShopOperationException("更新图片地址失败");
                    }
                }
            }
        }catch (Exception e){
            //抛出RuntimeException的原因是当事务失败的时候，只有RuntimeException导致事务回滚
            throw new ShopOperationException("addShopError: " + e.getMessage());
        }

        return new ShopExecution(ShopStateEnum.CHECK, shop);
    }

    @Override
    public Shop getByShopId(long shopId) {
        return shopDao.queryByShopId(shopId);
    }

    @Override
    public ShopExecution modifyShop(Shop shop, ImageHolder thumbnail) {
        //分类两步进行
        //1. 判断是否需要处理图片（编写工具类）
        //2. 更新店铺信息

        //修改信息的shop为空或者shopId为空是非法的，根据ID进行对应行的修改
        if (shop == null || shop.getShopId() == null)
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        else {
            try {
                //处理图片
                if (thumbnail.getImage() != null && thumbnail.getImageName() != null && !"".equals(thumbnail.getImageName())){
                    Shop tempShop = shopDao.queryByShopId(shop.getShopId());
                    if (tempShop.getShopImg() != null){
                        ImageUtil.deleteFileOrPath(tempShop.getShopImg());
                    }
                    addShopImg(shop, thumbnail);
                }

                //更新店铺信息
                shop.setLastEditTime(new Date());
                int effectNum = shopDao.updateShop(shop);
                if (effectNum <= 0){
                    return new ShopExecution(ShopStateEnum.INNER_ERROR);
                }else {
                    shop = shopDao.queryByShopId(shop.getShopId());
                    return new ShopExecution(ShopStateEnum.SUCCESS, shop);
                }
            }catch (Exception e){
                throw new ShopOperationException("modifyShop error!" + e.getMessage());
            }
        }
    }

    @Override
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
        List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
        int count  = shopDao.queryShopCount(shopCondition);
        ShopExecution shopExecution = new ShopExecution();
        if (shopList != null){
            shopExecution.setShopList(shopList);
            shopExecution.setCount(count);
        }else {
            shopExecution.setState(ShopStateEnum.INNER_ERROR.getState());
        }
        return shopExecution;
    }

    private void addShopImg(Shop shop, ImageHolder thumbnail){
        //获取shop图片目录的相对路径
        String dest = PathUtil.getShopImgPath(shop.getShopId());
        String shopImgAddr = ImageUtil.generateThumbnail(thumbnail, dest);
        shop.setShopImg(shopImgAddr);
    }
}
