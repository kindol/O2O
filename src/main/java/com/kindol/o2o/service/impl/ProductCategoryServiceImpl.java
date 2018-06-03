package com.kindol.o2o.service.impl;

import com.kindol.o2o.Exceptions.ProductCategoryOperationException;
import com.kindol.o2o.dao.ProductCategoryDao;
import com.kindol.o2o.dao.ProductDao;
import com.kindol.o2o.dto.ProductCategoryExecution;
import com.kindol.o2o.entity.ProductCategory;
import com.kindol.o2o.enums.ProductCategoryStateEnum;
import com.kindol.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryDao productCategoryDao;
    @Autowired
    private ProductDao productDao;

    @Override
    public List<ProductCategory> getProductCategoryList(long shopId) {
        return productCategoryDao.queryProductCategory(shopId);
    }

    @Override
    @Transactional
    //由于是进行插入，所以直接返回执行结果就可以了
    public ProductCategoryExecution batchInsertProductCategory(List<ProductCategory> productCategoryList) throws ProductCategoryOperationException {
        if (productCategoryList != null && productCategoryList.size() > 0){
            try {
                int effectedNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
                if (effectedNum <= 0)
                    throw new ProductCategoryOperationException("店铺类别创建失败");
                else
                    return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
            }catch (Exception e){
                throw new ProductCategoryOperationException("batchInsertProductCategory error: " + e.getMessage());
            }
        }else {
            return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
        }
    }

    /**
     * 删除事务有两步，所以需要@Transactional
     * @param productCategoryId
     * @param shopId
     * @return
     * @throws ProductCategoryOperationException
     */
    @Override
    @Transactional
    public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId) throws ProductCategoryOperationException {
        // 将此商品类别下的商品类别ID置为空
        // 先解除tb_product里的商品与该productCategoryId的关联
        int effectedNum = productDao.updateProductCategoryToNull(productCategoryId);
        if (effectedNum < 0)
            throw new ProductCategoryOperationException("商品类别更新失败");
        //删除该productCategory
        try {
            effectedNum = productCategoryDao.deleteProductCategory(productCategoryId, shopId);
            if (effectedNum <= 0){
                throw new ProductCategoryOperationException("店铺类别删除失败");
            }else {
                return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
            }
        }catch (ProductCategoryOperationException e){
            throw new ProductCategoryOperationException("deleteProductCategoryError" + e.getLocalizedMessage());
        }
    }
}
