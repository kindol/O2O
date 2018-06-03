package com.kindol.o2o.dao;

import com.kindol.o2o.entity.WeChatAuth;

public interface WechatAuthDao {

    /**
     * 通过openId查询对应本平台的微信账号
     * @param openId
     * @return
     */
    WeChatAuth queryWechatInfoByOpenId(String openId);

    /**
     * 添加对应本平台的微信账号
     * @param weChatAuth
     * @return
     */
    int insertWechatAuth(WeChatAuth weChatAuth);

}
