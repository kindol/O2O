package com.kindol.o2o.dao;

import com.kindol.o2o.BaseTest;
import com.kindol.o2o.entity.LocalAuth;
import com.kindol.o2o.entity.PersonInfo;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocalAuthDaoTest extends BaseTest {

    @Autowired
    private LocalAuthDao localAuthDao;
    private static final String username = "testusername";
    private static final String password = "testpassword";

    @Test
    @Ignore
    public void testAInsertLocalAuth(){
        LocalAuth localAuth = new LocalAuth();
        PersonInfo personInfo = new PersonInfo();
        personInfo.setUserId(1l);

        localAuth.setPersonInfo(personInfo);

        localAuth.setUsername(username);
        localAuth.setPassword(password);
        int effectedNum = localAuthDao.insertLocalAuth(localAuth);
        assertEquals(effectedNum, 1);
    }

    @Test
    public void testBQueryLocalByUserNameAndPwd(){
        LocalAuth localAuth = localAuthDao.queryLocalByUserNameAndPwd(username, password);
        assertEquals("测试", localAuth.getPersonInfo().getName());
    }

    @Test
    public void testCQueryLocalByUserId(){
        LocalAuth localAuth = localAuthDao.queryLocalByUserId(1l);
        assertEquals("测试", localAuth.getPersonInfo().getName());
    }

    @Test
    public void testDUpdateLocalAuth(){
        Date now = new Date();
        int effectedNum = localAuthDao.updateLocalAuth(1l, username, password, password+"new", now);
        assertEquals(effectedNum, 1);

        LocalAuth localAuth = localAuthDao.queryLocalByUserId(1l);
        System.out.println(localAuth.getPassword());
    }
}
