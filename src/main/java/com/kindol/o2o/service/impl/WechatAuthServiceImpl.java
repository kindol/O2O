package com.kindol.o2o.service.impl;

import com.kindol.o2o.Exceptions.WechatAuthOperationExecption;
import com.kindol.o2o.dao.PersonInfoDao;
import com.kindol.o2o.dao.WechatAuthDao;
import com.kindol.o2o.dto.WechatAuthExecution;
import com.kindol.o2o.entity.PersonInfo;
import com.kindol.o2o.entity.WeChatAuth;
import com.kindol.o2o.enums.WechatAuthStateEnum;
import com.kindol.o2o.service.WechatAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class WechatAuthServiceImpl implements WechatAuthService {

    private static Logger logger = LoggerFactory.getLogger(WechatAuthServiceImpl.class);

    @Autowired
    private WechatAuthDao wechatAuthDao;
    @Autowired
    private PersonInfoDao personInfoDao;

    @Override
    public WeChatAuth getWechatAuthByOpenId(String openId) {
        return wechatAuthDao.queryWechatInfoByOpenId(openId);
    }

    @Override
    @Transactional
    public WechatAuthExecution register(WeChatAuth weChatAuth) throws WechatAuthOperationExecption {
        //空值判断
        if (weChatAuth == null || weChatAuth.getOpenId() == null)
            return new WechatAuthExecution(WechatAuthStateEnum.NULL_AUTH_INFO);
        try {
            weChatAuth.setCreateTime(new Date());
            //如果微信账号里夹带着用户信息并且用户id为考哪个，则认为该用户第一次使用平台（且通过微信登录）
            //则自动创建用户信息
            if (weChatAuth.getPersonInfo() != null && weChatAuth.getPersonInfo().getUserId() == null){
                try {
                    weChatAuth.getPersonInfo().setCreateTime(new Date());
                    weChatAuth.getPersonInfo().setEnableStatus(1);
                    PersonInfo personInfo = weChatAuth.getPersonInfo();
                    //在personInfo表格里添加平台里的用户信息，得到user_id再去设置weChatAuth
                    int effectedNum = personInfoDao.insertPersonInfo(personInfo);
                    weChatAuth.setPersonInfo(personInfo);
                    if (effectedNum <= 0)
                        throw new WechatAuthOperationExecption("添加用户信息失败");
                }catch (Exception e){
                    logger.error("insertPersonInfo error: " + e.toString());
                    throw new WechatAuthOperationExecption("insertPersonInfo error:" + e.getMessage());
                }
            }
            int effectedNum = wechatAuthDao.insertWechatAuth(weChatAuth);
            if (effectedNum <= 0)
                throw new WechatAuthOperationExecption("账号创建失败");
            else
                return new WechatAuthExecution(WechatAuthStateEnum.SUCCESS, weChatAuth);
        }catch (Exception e){
            logger.error("insertWechatAuth error: " + e.toString());
            throw new WechatAuthOperationExecption("insertWechatAuth error:" + e.getMessage());
        }
    }
}
