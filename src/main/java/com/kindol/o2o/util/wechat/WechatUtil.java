package com.kindol.o2o.util.wechat;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindol.o2o.dto.UserAccessToken;
import com.kindol.o2o.dto.WechatUser;
import com.kindol.o2o.entity.PersonInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.security.SecureRandom;

/**
 * 微信工具类
 */
public class WechatUtil {

    private static Logger logger = LoggerFactory.getLogger(WechatUtil.class);

    /**
     * 获取UserAccessToken实体类
     * @param code
     * @return
     */
    public static UserAccessToken getUserAccessToken(String code){
        //测试号里的appId
        String appId = "wxdb0a6572b2bfc06a";
        logger.debug("appId: " + appId);
        //测试号里的appSecret
        String appSecret = "3fb5ca241af5cf612606c6c04e4a38e2";
        logger.debug("secret: " + appSecret);
        //根据传入的code，拼接出访问微信定义好的接口URL
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" + appSecret
                + "&code=" + code + "&grant_type=authorization_code";
        //向相应URL发送请求获取token json字符串
        String tokenStr = httpsRequest(url, "GET", null);
        logger.debug("userAccessToken: " + tokenStr);

        UserAccessToken token = new UserAccessToken();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            token = objectMapper.readValue(tokenStr, UserAccessToken.class);
        } catch (JsonParseException e) {
            logger.error("获取用户accessToken失败: " + e.getMessage());
            e.printStackTrace();
        } catch (JsonMappingException e) {
            logger.error("获取用户accessToken失败: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("获取用户accessToken失败: " + e.getMessage());
            e.printStackTrace();
        }
        if (token == null){
            logger.error("获取用户accessToken失败");
            return null;
        }
        return token;

    }

    /**
     * 获取WechatUser实体类
     * @param accessToken
     * @param openId
     * @return
     */
    public static WechatUser getUserInfo(String accessToken, String openId){
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId
                + "&lang=zh_CN";
        String userStr = httpsRequest(url, "GET", null);
        logger.debug("user info: " + userStr);
        WechatUser user = new WechatUser();
        ObjectMapper mapper = new ObjectMapper();
        try {
            user = mapper.readValue(userStr,WechatUser.class);
        } catch (JsonParseException e) {
            logger.error("获取用户信息失败: " + e.getMessage());
            e.printStackTrace();
        } catch (JsonMappingException e) {
            logger.error("获取用户信息失败: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("获取用户信息失败: " + e.getMessage());
            e.printStackTrace();
        }
        if (user == null){
            logger.error("获取用户信息失败");
            return null;
        }
        return user;

    }

    /**
     * 发起https请求并获取结果，主要是为了能够支持https，需要加密
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式
     * @param outputStr     提交的数据
     * @return      json字符串
     */
    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr){
        StringBuffer buffer = new StringBuffer();
        try {
            //创建SSLContext对象，且使用指定的信任管理器初始化
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new SecureRandom());
            //从上述SSLContext对象得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection)url.openConnection();
            httpsURLConnection.setSSLSocketFactory(ssf);

            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setUseCaches(false);
            //设置请求的方式GET/POST
            httpsURLConnection.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod))
                httpsURLConnection.connect();

            //有数据要提交的时候
            if (null != outputStr) {
                OutputStream outputStream = httpsURLConnection.getOutputStream();
                //注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            //将返回的输入流转换为字符串
            InputStream inputStream = httpsURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }

            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            httpsURLConnection.disconnect();
            logger.debug("https buffer: " + buffer.toString());
        } catch (ConnectException e) {
            logger.error("Weixin server connection time out");
        } catch (Exception e) {
            logger.error("https request error:{}", e);
        }

        return buffer.toString();
    }

    /**
     * 将WechatUser里的信息转换成PersonInfo的信息并返回PersonInfo实体类
     * @param user
     * @return
     */
    public static PersonInfo getPersonInfoFromRequest(WechatUser user){
        PersonInfo personInfo = new PersonInfo();
        personInfo.setName(user.getNickName());
        personInfo.setGender(user.getSex()+"");
        personInfo.setProfileImg(user.getHeadimgurl());
        personInfo.setEnableStatus(1);
        return personInfo;
    }

}
