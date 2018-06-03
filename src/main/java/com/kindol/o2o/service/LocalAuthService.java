package com.kindol.o2o.service;

import com.kindol.o2o.Exceptions.LocalAuthOperationException;
import com.kindol.o2o.dto.LocalAuthExecution;
import com.kindol.o2o.entity.LocalAuth;

public interface LocalAuthService {

    LocalAuth getLocalAuthByUsernameAndPassword(String username, String password);

    LocalAuth getLocalAuthByUserId(long userId);

    /**
     * 绑定微信，生成平台专属账号
     * @param localAuth
     * @return
     */
    LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException;

    /**
     * 修改平台账号信息
     * @param userId
     * @param username
     * @param password
     * @param newPassword
     * @return
     */
    LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword) throws LocalAuthOperationException;
}
