package com.kindol.o2o.dao;

import com.kindol.o2o.entity.Area;

import java.util.List;

/*
mybatis不需要dao实现类，只需要配置即可
 */

public interface AreaDao {

    /**
     * 列出区域列表
     * @return areaList
     */
    List<Area> queryArea();

}
