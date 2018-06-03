package com.kindol.o2o.service.impl;

import com.kindol.o2o.Exceptions.LocalAuthOperationException;
import com.kindol.o2o.dao.LocalAuthDao;
import com.kindol.o2o.dto.LocalAuthExecution;
import com.kindol.o2o.entity.LocalAuth;
import com.kindol.o2o.enums.LocalAuthStateEnum;
import com.kindol.o2o.service.LocalAuthService;
import com.kindol.o2o.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class LocalAuthServiceImpl implements LocalAuthService {

    @Autowired
    private LocalAuthDao localAuthDao;

    @Override
    public LocalAuth getLocalAuthByUsernameAndPassword(String username, String password) {
        return localAuthDao.queryLocalByUserNameAndPwd(username ,MD5.getMd5(password));
    }

    @Override
    public LocalAuth getLocalAuthByUserId(long userId) {
        return localAuthDao.queryLocalByUserId(userId);
    }

    @Override
    @Transactional
    public LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException {
        //空值判断，传入的localAuth账号密码、用户信息特别是userId不能为空，否则直接返回错误
        if (localAuth == null || localAuth.getPassword() == null || localAuth.getUsername() == null
                || localAuth.getPersonInfo() == null || localAuth.getPersonInfo().getUserId() == null){
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }
        //查询此用户是否已绑定过平台账号
        LocalAuth tempAuth = localAuthDao.queryLocalByUserId(localAuth.getPersonInfo().getUserId());
        if (tempAuth != null){
            return new LocalAuthExecution(LocalAuthStateEnum.ONLY_ONE_ACCOUNT);
        }
        try {
            //如果之前没有绑定过平台账号，则创建一个平台账号与该用户绑定
            localAuth.setCreateTime(new Date());
            localAuth.setLastEditTime(new Date());
            //对密码进行MD5加密
            localAuth.setPassword(MD5.getMd5(localAuth.getPassword()));
            int effectedNum = localAuthDao.insertLocalAuth(localAuth);
            //判断创建是否成功
            if (effectedNum <= 0)
                throw new LocalAuthOperationException("账号绑定失败");
            else
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS, localAuth);
        }catch (Exception e){
            throw new LocalAuthOperationException("insertLocalAuth error: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword) throws LocalAuthOperationException {
        //非空判断，传入的用户ID，账号、新旧密码是否为空，新旧密码是否相同，若不满足条件则返回错误信息
        if (userId != null && username != null && password != null && newPassword != null && !password.equals(newPassword)){
            try {
                int effectedNum = localAuthDao.updateLocalAuth(userId, username, MD5.getMd5(password),
                        MD5.getMd5(newPassword), new Date());
                if (effectedNum <= 0)
                    throw new LocalAuthOperationException("更新密码失败");
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
            }catch (Exception e){
                throw new LocalAuthOperationException("更新密码失败： " + e.toString());
            }
        }else {
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }
    }
}
