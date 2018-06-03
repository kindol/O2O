package com.kindol.o2o.dao;

import com.kindol.o2o.BaseTest;
import com.kindol.o2o.entity.PersonInfo;
import com.kindol.o2o.entity.WeChatAuth;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WechatAuthDaoTest extends BaseTest {

    @Autowired
    private WechatAuthDao wechatAuthDao;

    @Test
    public void testAInsertWechatAuth(){
        //新增一条微信账号
        WeChatAuth weChatAuth = new WeChatAuth();
        PersonInfo personInfo = new PersonInfo();
        personInfo.setUserId(1l);
        //为微信账号绑定用户信息
        weChatAuth.setPersonInfo(personInfo);
        weChatAuth.setOpenId("weifjwoijgoaiejg");
        weChatAuth.setCreateTime(new Date());
        int effectedNum = wechatAuthDao.insertWechatAuth(weChatAuth);
        assertEquals(1, effectedNum);
    }

    @Test
    public void testBQueryWechatInfoByOpenId(){
        WeChatAuth weChatAuth = wechatAuthDao.queryWechatInfoByOpenId("weifjwoijgoaiejg");
        assertEquals("测试", weChatAuth.getPersonInfo().getName());
    }

}
