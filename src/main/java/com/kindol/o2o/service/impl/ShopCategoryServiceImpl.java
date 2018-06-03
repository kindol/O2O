package com.kindol.o2o.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindol.o2o.Exceptions.ShopCategoryOperationException;
import com.kindol.o2o.cache.JedisUtil;
import com.kindol.o2o.dao.ShopCategoryDao;
import com.kindol.o2o.entity.ShopCategory;
import com.kindol.o2o.service.ShopCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {

    @Autowired
    private ShopCategoryDao shopCategoryDao;
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired
    private JedisUtil.Strings jedisStrings;
    private static Logger logger = LoggerFactory.getLogger(ShopCategoryServiceImpl.class);

    @Override
    @Transactional
    public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) {
        List<ShopCategory> shopCategoryList = null;
        String key = SC_LIST_KEY;
        ObjectMapper objectMapper = new ObjectMapper();

        //由于不像mybatis可以在xml中配置判断是否condition为空或者为其他身份，所以使用redis的时候需要自己手动加上key的不同
        if (shopCategoryCondition == null){
            //若查询条件为空则列出所有大类，即parentId为空的店铺类别
            key = key + "_allfirstlevel";
        }else if (shopCategoryCondition != null && shopCategoryCondition.getParent() != null
                && shopCategoryCondition.getParent().getShopCategoryId() != null){
            key = key + "_parent" + shopCategoryCondition.getParent().getShopCategoryId();
        }else if (shopCategoryCondition != null){
            key = key + "_allsecondlevel";
        }
        if (!jedisKeys.exists(key)){
            shopCategoryList = shopCategoryDao.queryShopCategory(shopCategoryCondition);
            String jsonString;
            try {
                jsonString = objectMapper.writeValueAsString(shopCategoryList);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new ShopCategoryOperationException(e.getMessage());
            }
            jedisStrings.set(key, jsonString);
        }else {
            String jsonString = jedisStrings.get(key);
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, ShopCategory.class);
            try {
                shopCategoryList = objectMapper.readValue(jsonString, javaType);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new ShopCategoryOperationException(e.getMessage());
            }
        }
        return shopCategoryList;
    }
}
