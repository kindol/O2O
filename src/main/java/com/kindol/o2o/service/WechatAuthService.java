package com.kindol.o2o.service;

import com.kindol.o2o.Exceptions.WechatAuthOperationExecption;
import com.kindol.o2o.dto.WechatAuthExecution;
import com.kindol.o2o.entity.WeChatAuth;
import com.kindol.o2o.enums.WechatAuthStateEnum;

public interface WechatAuthService {

    /**
     * 通过openId查找平台对应的微信账号
     * @param openId
     * @return
     */
    WeChatAuth getWechatAuthByOpenId(String openId);

    /**
     * 注册微信账号，抛出RunTimeExecption是为了支持事务的回滚
     * @param weChatAuth
     * @return
     */
    WechatAuthExecution register(WeChatAuth weChatAuth) throws WechatAuthOperationExecption;
}
