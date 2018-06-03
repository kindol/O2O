package com.kindol.o2o.web.frontend;

import com.kindol.o2o.dto.ProductExecution;
import com.kindol.o2o.entity.Product;
import com.kindol.o2o.entity.ProductCategory;
import com.kindol.o2o.entity.Shop;
import com.kindol.o2o.service.ProductCategoryService;
import com.kindol.o2o.service.ProductService;
import com.kindol.o2o.service.ShopService;
import com.kindol.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/frontend")
public class ShopDetailController {

    @Autowired
    private ShopService shopService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;

    /**
     * 获取店铺信息以及该店铺下面的商品类别列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/listshopdetailpageinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listShopDetailPageInfo(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        //从前端传来shopId
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        Shop shop = null;
        List<ProductCategory> productCategoryList = null;
        if (shopId != -1){
            shop = shopService.getByShopId(shopId);
            productCategoryList = productCategoryService.getProductCategoryList(shopId);
            modelMap.put("success", true);
            modelMap.put("shop", shop);
            modelMap.put("productCategoryList", productCategoryList);
        }else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopId");
        }
        return modelMap;
    }

    @RequestMapping(value = "/listproductsbyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listProductsByShop(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        //从前端传来shopId
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        //空值判断
        if ((pageIndex > -1) && (pageSize > -1) && (shopId > -1)){
            long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
            String productName = HttpServletRequestUtil.getString(request, "productName");
            Product productCondition = compactProductCondition4Search(shopId, productCategoryId, productName);
            ProductExecution pe = productService.getProductList(productCondition, pageIndex, pageSize);
            modelMap.put("success", true);
            modelMap.put("productList", pe.getProductList());
            modelMap.put("count", pe.getCount());
        }else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
        }
        return modelMap;
    }

    /**
     * 组合查询条件，并将条件封装到ProductCondition对象里返回
     * @param shopId
     * @param productCategoryId
     * @param productName
     * @return
     */
    private Product compactProductCondition4Search(long shopId, long productCategoryId, String productName){
        Product productCondition = new Product();
        Shop shop = new Shop();
        shop.setShopId(shopId);
        productCondition.setShop(shop);
        if (productCategoryId != -1){
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }
        if (productName != null){
            productCondition.setProductName(productName);
        }
        productCondition.setEnableStatus(1);
        return productCondition;
    }
}
