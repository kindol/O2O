package com.kindol.o2o.service;

import com.kindol.o2o.BaseTest;
import com.kindol.o2o.entity.Area;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class AreaServiceTest extends BaseTest {

    @Autowired
    private AreaService areaService;
    @Autowired
    private CacheService cacheService;

    @Test
    public void testGetAreaList(){
        List<Area> areaList = areaService.getAreaList();
        assertEquals("西苑", areaList.get(0).getAreaName());
        cacheService.removeFromCache(AreaService.AREA_LIST_KEY);
        areaList = areaService.getAreaList();
    }

}
