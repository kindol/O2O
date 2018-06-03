package com.kindol.o2o.service;

import com.kindol.o2o.entity.PersonInfo;

public interface PersonInfoService {

    /**
     * 根据用户id获取用户信息
     * @param userId
     * @return
     */
    PersonInfo getPersonInfoById(Long userId);

}
