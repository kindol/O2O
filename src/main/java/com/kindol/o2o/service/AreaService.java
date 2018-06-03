package com.kindol.o2o.service;

import com.kindol.o2o.entity.Area;

import java.util.List;

public interface AreaService {
    String AREA_LIST_KEY = "arealist";
    List<Area> getAreaList();
}
