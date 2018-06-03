package com.kindol.o2o.dao;

import com.kindol.o2o.BaseTest;
import com.kindol.o2o.entity.Area;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * 每次都加载Spring-dao的配置文件
 */

public class AreaDaoTest extends BaseTest
{
    @Autowired
    private AreaDao areaDao;

    @Test
    public void testQueryArea(){
        List<Area> areaList = areaDao.queryArea();
        assertEquals(2, areaList.size());
    }
}
