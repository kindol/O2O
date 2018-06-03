package com.kindol.o2o.service.impl;

import com.kindol.o2o.Exceptions.ProductOperationException;
import com.kindol.o2o.dao.ProductDao;
import com.kindol.o2o.dao.ProductImgDao;
import com.kindol.o2o.dto.ImageHolder;
import com.kindol.o2o.dto.ProductExecution;
import com.kindol.o2o.entity.Product;
import com.kindol.o2o.entity.ProductImg;
import com.kindol.o2o.enums.ProductStateEnum;
import com.kindol.o2o.service.ProductService;
import com.kindol.o2o.util.ImageUtil;
import com.kindol.o2o.util.PageCalculator;
import com.kindol.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductImgDao productImgDao;

    @Override
    @Transactional
    /**
     * 分为四步：
     * 1. 处理缩略图，获取缩略图相对路径并赋值给product
     * 2. 往tb_product写入商品信息，获取productId
     * 3. 结合productId批量处理商品详情图
     * 4. 将商品详情图列表批量插入tb_product_img中
     */
    public ProductExecution addProduct(Product product, ImageHolder thumbnail,
                                       List<ImageHolder> imageHolderList) throws ProductOperationException {
        //空值判断
        if (product != null && product.getShop() != null && product.getShop().getShopId() != null){
            product.setCreateTime(new Date());
            product.setLastEditTime(new Date());
            product.setEnableStatus(1);     //默认为上架
            //如果thumbnail不为空则添加
            if (thumbnail != null){
                addThumbnail(product, thumbnail);
            }
            try {
                int effectedNum = productDao.insertProduct(product);
                if (effectedNum <= 0)
                    throw new ProductOperationException("创建商品失败");
            }catch (Exception e){
                throw new ProductOperationException("创建商品失败" + e.toString());
            }

            //若商品详情图不为空则添加
            if (imageHolderList != null && imageHolderList.size() > 0){
                addProductImgList(product, imageHolderList);
            }
            return new ProductExecution(ProductStateEnum.SUCCESS, product);
        }else {
            //传参为空则返回空值错误信息
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    @Override
    public Product getProdcutById(long productId) {
        return productDao.queryProductById(productId);
    }

    @Override
    @Transactional
    /**
     * 跟add仅有一些不同，分为四步：
     * 1. 若缩略图参数有值，则处理缩略图
     * 若原先存在缩略图则先删除再添加新图，之后获取缩略图相对路径并赋值给productId
     * 2. 若商品详情图参数有值，进行如上操作
     * 3. 将tb_product_img下面的该商品原先的商品详情图片记录全部清除（先删除物理图片，再更新数据库表）
     * 4. 更新tb_product_img和tb_product的信息
     */
    public ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> imageHolderList) throws ProductOperationException {
        //控制判断
        if (product != null && product.getShop() != null && product.getShop().getShopId() != null){
            //给商品设置默认属性
            product.setLastEditTime(new Date());
            //若商品缩略图不为空
            if (thumbnail != null){
                Product tempProduct = productDao.queryProductById(product.getProductId());
                if (tempProduct.getImgAddr() != null){
                    ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
                }
                addThumbnail(product, thumbnail);
            }
            //如果有新存入的商品详情图，则将原先的删除，并添加新的图片
            if (imageHolderList != null && imageHolderList.size() > 0){
                deleteProductImgList(product.getProductId());
                addProductImgList(product, imageHolderList);
            }
            try {
                //更新商品信息
                int effectedNum = productDao.updateProduct(product);
                if (effectedNum <= 0)
                    throw new ProductOperationException("更新商品信息失败");
                return new ProductExecution(ProductStateEnum.SUCCESS, product);
            }catch (Exception e){
                throw new ProductOperationException("更新商品信息失败" + e.toString());
            }
        }else
            return new ProductExecution(ProductStateEnum.EMPTY);
    }

    @Override
    public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
        //页码转换成数据库的行码，并调用dao层取回指定页码的商品列表
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
        List<Product> productList = productDao.queryProductList(productCondition, rowIndex, pageSize);
        //基于同样的查询条件返回该查询条件下的商品总数
        int count = productDao.queryProductCount(productCondition);
        ProductExecution pe = new ProductExecution();
        pe.setProductList(productList);
        pe.setCount(count);
        return pe;
    }

    /**
     * 添加缩略图
     * @param product
     * @param thumbnail
     */
    private void addThumbnail(Product product, ImageHolder thumbnail){
        //dest为相对路径
        String dest = PathUtil.getShopImgPath(product.getShop().getShopId());
        String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail, dest);
        product.setImgAddr(thumbnailAddr);
    }

    /**
     * 批量添加详情图，包括对数据库表的处理
     * @param product
     * @param productImgHolderList
     */
    private void addProductImgList(Product product, List<ImageHolder> productImgHolderList){
        //获取图片存储路径，这里直接存放在相应店铺的文件夹底下
        String dest = PathUtil.getShopImgPath(product.getShop().getShopId());
        List<ProductImg> productImgList = new ArrayList<>();
        //遍历图片一次去处理、并添加进productImg实体类里
        for (ImageHolder productImageHolder: productImgHolderList){
            String imgAddr = ImageUtil.generateNormalImg(productImageHolder, dest);
            ProductImg productImg = new ProductImg();
            productImg.setImgAddr(imgAddr);
            productImg.setProductId(product.getProductId());
            productImg.setCreateTime(new Date());
            productImgList.add(productImg);
        }

        //如果确实是有图片需要添加的，就执行批量添加操作
        if (productImgList.size() > 0){
            try {
                int effectedNum = productImgDao.batchInsertProductImg(productImgList);
                if (effectedNum <= 0)
                    throw new ProductOperationException("创建商品详情图片失败");
            }catch (Exception e){
                throw new ProductOperationException("创建商品详情图片失败" + e.toString());
            }
        }
    }

    /**
     * 删除某个商品下的详情图，包括对数据库表的处理
     * @param productId
     */
    private void deleteProductImgList(long productId){
        List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
        //删除原有的图片，删除图片需要注意要有相对路径才可以删除
        for (ProductImg productImg: productImgList){
            ImageUtil.deleteFileOrPath(productImg.getImgAddr());
        }
        //删除数据库里原有图片的信息
        productImgDao.deleteProductImgByProductId(productId);
    }
}
