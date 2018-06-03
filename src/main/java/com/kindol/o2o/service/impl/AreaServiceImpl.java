package com.kindol.o2o.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindol.o2o.Exceptions.AreaOperationException;
import com.kindol.o2o.cache.JedisUtil;
import com.kindol.o2o.dao.AreaDao;
import com.kindol.o2o.entity.Area;
import com.kindol.o2o.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//告诉SpringIOC本类由IOC容器托管，表明需要其注入含有标签@Autowired的类
@Service
public class AreaServiceImpl implements AreaService {

    //service需要跟dao进行交互才可以操作
    @Autowired
    private AreaDao areaDao;
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired
    private JedisUtil.Strings jedisStrings;
    private static Logger logger = LoggerFactory.getLogger(AreaServiceImpl.class);

    @Override
    @Transactional
    public List<Area> getAreaList() {
        String key = AREA_LIST_KEY;
        List<Area> areaList = null;
        ObjectMapper mapper = new ObjectMapper();
        if (!jedisKeys.exists(key)){
            areaList = areaDao.queryArea();
            String jsonString = null;
            try {
                jsonString = mapper.writeValueAsString(areaList);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new AreaOperationException(e.getMessage());
            }
            jedisStrings.set(key, jsonString);
        }else {
            String jsonString = jedisStrings.get(key);
            //为组合类型所以需要使用JavaType
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, Area.class);
            try {
                areaList = mapper.readValue(jsonString, javaType);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new AreaOperationException(e.getMessage());
            }
        }
        return areaList;
    }


}
