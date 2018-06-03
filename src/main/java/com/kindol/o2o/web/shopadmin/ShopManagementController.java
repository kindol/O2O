package com.kindol.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindol.o2o.dto.ImageHolder;
import com.kindol.o2o.dto.ProductCategoryExecution;
import com.kindol.o2o.dto.ShopExecution;
import com.kindol.o2o.entity.Area;
import com.kindol.o2o.entity.PersonInfo;
import com.kindol.o2o.entity.Shop;
import com.kindol.o2o.entity.ShopCategory;
import com.kindol.o2o.enums.ShopStateEnum;
import com.kindol.o2o.service.AreaService;
import com.kindol.o2o.service.ShopCategoryService;
import com.kindol.o2o.service.ShopService;
import com.kindol.o2o.util.CodeUtil;
import com.kindol.o2o.util.HttpServletRequestUtil;
import com.kindol.o2o.util.ImageUtil;
import com.kindol.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {

    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;

    @RequestMapping(value = "/getshopbyid", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopById(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if (shopId > -1){
            try {
                Shop shop = shopService.getByShopId(shopId);
                List<Area> areaList = areaService.getAreaList();
                modelMap.put("shop", shop);
                modelMap.put("areaList", areaList);
                modelMap.put("success", true);
            }catch (Exception e){
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
            }
        }else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopId");
        }
        return modelMap;
    }

    @RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> modifyShop(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();

        //判断验证码
        if (!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }

        //1.接收并转换相应的参数，包括店铺信息和图片信息(pom中引入的json就负责转换相应参数)
        //key会先跟前端约定好
        //转化为实体类
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            //将json转换为Shop类
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        //处理图片
        CommonsMultipartFile shopImg = null;
        //从session中获取本次上传文件的内容
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        //判断是否有上传的文件流
        if (commonsMultipartResolver.isMultipart(request)){
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile)multipartHttpServletRequest.getFile("shopImg");
        }

        //2.修改店铺信息
        if (shop != null && shop.getShopId() != null) {
            ShopExecution se = null;
            try {
                if (shopImg == null){
                    se = shopService.modifyShop(shop, null);
                }else{
                    se = shopService.modifyShop(shop, new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream()));
                }
                if (se.getState() == ShopStateEnum.SUCCESS.getState()){
                    modelMap.put("success", true);
                }else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", se.getStateInfo());
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }
            return modelMap;
        }else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺ID");
            return modelMap;
        }

        //3.返回结果
    }

    @RequestMapping(value = "/getshopinitinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopInitInfo(){
        //很多Controller层都是如此编写的，使用modelMap返回
        Map<String, Object> modelMap = new HashMap<>();
        List<ShopCategory> shopCategoryList = new ArrayList<>();
        List<Area> areaList = new ArrayList<>();

        try {
            shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
            areaList = areaService.getAreaList();
            modelMap.put("shopCategoryList", shopCategoryList);
            modelMap.put("areaList", areaList);
            modelMap.put("success", true);
        }catch (Exception e){
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    @RequestMapping(value = "/registershop", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> registerShop(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();

        //判断验证码
        if (!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }

        //1.接收并转换相应的参数，包括店铺信息和图片信息(pom中引入的json就负责转换相应参数)
        //key会先跟前端约定好
        //转化为实体类
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            //将json转换为Shop类
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        //处理图片
        CommonsMultipartFile shopImg = null;
        //从session中获取本次上传文件的内容
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        //判断是否有上传的文件流
        if (commonsMultipartResolver.isMultipart(request)){
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile)multipartHttpServletRequest.getFile("shopImg");
        }else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "上传图片不能为空");
            return modelMap;
        }

        //2.注册店铺
        if (shop != null && shopImg != null) {
            //店主的信息可以通过session创建，user为登录后的用户信息
            PersonInfo owner = (PersonInfo)request.getSession().getAttribute("user");
            shop.setOwner(owner);
            ShopExecution se = null;
            try {
                se = shopService.addShop(shop, new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream()));
                if (se.getState() == ShopStateEnum.CHECK.getState()){
                    modelMap.put("success", true);
                    //该用户可操作的店铺列表
                    List<Shop> shopList = (List<Shop>)request.getSession().getAttribute("shopList");
                    //如果用户可操作的店铺列表不为空，那么创建；否则，往里面添加即可
                    if (shopList == null || shopList.size() == 0){
                        shopList = new ArrayList<>();
                    }
                    shopList.add(se.getShop());
                    request.getSession().setAttribute("shopList", shopList);
                }else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", se.getStateInfo());
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }
            return modelMap;
        }else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺信息");
            return modelMap;
        }

        //3.返回结果

    }

    @RequestMapping(value = "/getshoplist", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopList(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        PersonInfo user = (PersonInfo)request.getSession().getAttribute("user");
        try {
            Shop shopCondition = new Shop();
            shopCondition.setOwner(user);
            ShopExecution shopExecution = shopService.getShopList(shopCondition, 0, 100);
            //列出店铺成功后，将店铺放入session中作为权限验证依据，即该账号只能操作它自己的店铺
            request.getSession().setAttribute("shopList", shopExecution.getShopList());

            modelMap.put("shopList", shopExecution.getShopList());
            modelMap.put("user", user);
            modelMap.put("success", true);
        }catch (Exception e){
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    @RequestMapping(value = "/getshopmanagementinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopManagementInfo(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        //如果前端没有传过来shopid，则试着从session里面获取
        if (shopId <= 0){
            Object currentShopObj = request.getSession().getAttribute("currentShop");
            //如果session中没有ID，那么说明为违规操作，页面将重定向
            if (currentShopObj == null) {
                modelMap.put("redirect", true);
                modelMap.put("url", "/o2o/shopadmin/shoplist");
            }else {
                Shop currentShop = (Shop)currentShopObj;
                modelMap.put("redirect", false);
                modelMap.put("shopId", currentShop.getShopId());
            }
        }else {
            Shop currentShop = new Shop();
            currentShop.setShopId(shopId);
            request.getSession().setAttribute("currentShop", currentShop);
            modelMap.put("redirect", false);
        }
        return modelMap;
    }

    private static void inputStreamToFile(InputStream inputStream, File file){
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = inputStream.read(buffer)) != -1)
                os.write(buffer, 0, bytesRead);
        } catch (Exception e) {
            throw new RuntimeException("使用inputStreamToFile产生异常" + e.getMessage());
        }finally {
            try {
                if (os != null)
                    os.close();
                if (inputStream != null)
                    inputStream.close();
            }catch (IOException e){
                throw new RuntimeException("使用inputStreamToFile关闭io异常" + e.getMessage());
            }
        }
    }

}
