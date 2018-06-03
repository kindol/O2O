package com.kindol.o2o.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindol.o2o.Exceptions.HeadLineOperationException;
import com.kindol.o2o.cache.JedisUtil;
import com.kindol.o2o.dao.HeadLineDao;
import com.kindol.o2o.entity.HeadLine;
import com.kindol.o2o.service.HeadLineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class HeadLineServiceImpl implements HeadLineService {

    @Autowired
    private HeadLineDao headLineDao;
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired
    private JedisUtil.Strings jedisStrings;
    private static Logger logger = LoggerFactory.getLogger(HeadLineServiceImpl.class);

    @Override
    @Transactional
    public List<HeadLine> getHeadLineList(HeadLine headLineCondition){
        String key = HL_LIST_KEY;
        List<HeadLine> headLineList = null;
        ObjectMapper objectMapper = new ObjectMapper();
        //三种headLine，分为1.所有 2.可上线 3.不可上线
        if (headLineCondition != null && headLineCondition.getEnableStatus() != null){
            key = key + "_" + headLineCondition.getEnableStatus();
        }
        if (!jedisKeys.exists(key)){
            headLineList = headLineDao.queryHeadLine(headLineCondition);
            String jsonString = null;
            try {
                jsonString = objectMapper.writeValueAsString(headLineList);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new HeadLineOperationException(e.getMessage());
            }
            jedisStrings.set(key, jsonString);
        }else {
            String jsonString = jedisStrings.get(key);
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, HeadLine.class);
            try {
                headLineList = objectMapper.readValue(jsonString, javaType);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new HeadLineOperationException(e.getMessage());
            }
        }
        return headLineList;
    }
}
