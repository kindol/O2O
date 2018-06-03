package com.kindol.o2o;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 用于配置spring和junit整合，junit启动时加载springIOC容器
 * @RunWith告诉junit Spring配置文件的位置
 * @ContextConfiguration 表示需要的配置文件的位置，
 * 不需要加载spring-web，因为在此不测试controller，一般使用浏览器或者postman
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml", "classpath:spring/spring-redis.xml"})
public class BaseTest {

}
