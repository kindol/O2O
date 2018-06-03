package com.kindol.o2o.service;

import com.kindol.o2o.BaseTest;
import com.kindol.o2o.dto.WechatAuthExecution;
import com.kindol.o2o.entity.PersonInfo;
import com.kindol.o2o.entity.WeChatAuth;
import com.kindol.o2o.enums.WechatAuthStateEnum;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class WechatAuthServiceTest extends BaseTest {

    @Autowired
    private WechatAuthService wechatAuthService;

    @Test
    public void testRegister(){
        //新增微信账号
        WeChatAuth weChatAuth = new WeChatAuth();
        PersonInfo personInfo = new PersonInfo();
        String openId = "fiefjoefeaf";
        personInfo.setCreateTime(new Date());
        personInfo.setName("测试一下");
        personInfo.setUserType(1);
        weChatAuth.setPersonInfo(personInfo);
        weChatAuth.setOpenId(openId);
        weChatAuth.setCreateTime(new Date());
        WechatAuthExecution wae = wechatAuthService.register(weChatAuth);
        assertEquals(WechatAuthStateEnum.SUCCESS.getState(), wae.getState());

        weChatAuth = wechatAuthService.getWechatAuthByOpenId("fiefjoefeaf");
        System.out.println(weChatAuth.getPersonInfo().getName());
    }

}
