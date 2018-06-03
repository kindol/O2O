package com.kindol.o2o.web.local;

import com.kindol.o2o.dto.LocalAuthExecution;
import com.kindol.o2o.entity.LocalAuth;
import com.kindol.o2o.entity.PersonInfo;
import com.kindol.o2o.enums.LocalAuthStateEnum;
import com.kindol.o2o.service.LocalAuthService;
import com.kindol.o2o.util.CodeUtil;
import com.kindol.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "local")
public class LocalAuthController {

    @Autowired
    private LocalAuthService localAuthService;

    @RequestMapping(value = "/bindlocalauth", method = RequestMethod.POST)
    @ResponseBody
    /**
     * 绑定账号
     */
    private Map<String, Object> bindLocalAuth(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        //验证码
        if (!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        //获取输入的账号
        String userName = HttpServletRequestUtil.getString(request, "userName");
        //获取输入的密码
        String password = HttpServletRequestUtil.getString(request, "password");
        //从session中获取当前用户信息（用户一旦通过微信登录之后，便能获取到用户的信息）
        PersonInfo user = (PersonInfo)request.getSession().getAttribute("user");
        //非空判断，要求账号密码和当前用户的session非空
        if (userName != null && password != null && user != null && user.getUserId() != null){
            //创建LocalAuth对象并赋值
            LocalAuth localAuth = new LocalAuth();
            localAuth.setUsername(userName);
            localAuth.setPassword(password);
            localAuth.setPersonInfo(user);
            //绑定账号
            LocalAuthExecution le = localAuthService.bindLocalAuth(localAuth);
            if (le.getState() == LocalAuthStateEnum.SUCCESS.getState()){
                modelMap.put("success", true);
            }else {
                modelMap.put("success", false);
                modelMap.put("errMsg", le.getStateInfo());
            }
        }else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "用户和密码不能为空");
        }
        return modelMap;


    }

    @RequestMapping(value = "/changelocalpwd", method = RequestMethod.POST)
    @ResponseBody
    /**
     * 修改密码
     */
    private Map<String, Object> changeLocalPwd(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        //验证码
        if (!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        //获取输入的账号
        String userName = HttpServletRequestUtil.getString(request, "username");
        //获取输入的密码
        String password = HttpServletRequestUtil.getString(request, "password");
        String newPassword = HttpServletRequestUtil.getString(request, "newPassword");
        //从session中获取当前用户信息（用户一旦通过微信登录之后，便能获取到用户的信息）
        PersonInfo user = (PersonInfo)request.getSession().getAttribute("user");
        //非空判断
        if (userName != null && password != null && newPassword != null && user != null
                && user.getUserId() != null && !password.equals(newPassword)){
            try {
                //查看原先账号，看看与输入的账号是否一致，不一致则认为是非法操作
                LocalAuth localAuth = localAuthService.getLocalAuthByUserId(user.getUserId());
                if (localAuth == null || !localAuth.getUsername().equals(userName)){
                    //不一致则直接退出
                    modelMap.put("success", false);
                    modelMap.put("errMsg", "输入的账号非本次登录的账号");
                    return modelMap;
                }
                //修改平台账号的用户密码
                LocalAuthExecution le = localAuthService.modifyLocalAuth(user.getUserId(), userName, password, newPassword);
                if(le.getState() == LocalAuthStateEnum.SUCCESS.getState()){
                    modelMap.put("success", true);
                }else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", le.getStateInfo());
                }
            }catch (Exception e){
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
            }
        }else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入密码");
        }
        return modelMap;
    }

    /**
     * 登录账号密码的验证，同时加入session
     * @param request
     * @return
     */
    @RequestMapping(value = "/logincheck", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> logincheck(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        //验证码，错误超过三次就会将needVerify设置为true，即需要验证码
        boolean needVerify = HttpServletRequestUtil.getBoolean(request, "needVerify");
        if (needVerify && !CodeUtil.checkVerifyCode(request)){
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        //获取输入的账号
        String userName = HttpServletRequestUtil.getString(request, "username");
        //获取输入的密码
        String password = HttpServletRequestUtil.getString(request, "password");
        //非空检验
        if (userName != null && password != null){
            LocalAuth localAuth = localAuthService.getLocalAuthByUsernameAndPassword(userName, password);
            if (localAuth != null){
                modelMap.put("success", true);
                request.getSession().setAttribute("user", localAuth.getPersonInfo());
            }else {
                modelMap.put("success", true);
                modelMap.put("errMsg", "用户名或密码错误");
            }
        }else {
            modelMap.put("success", true);
            modelMap.put("errMsg", "用户名和密码均不能为空");
        }
        return modelMap;


    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> logout(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //将用户session置为空
        request.getSession().setAttribute("user", null);
        modelMap.put("success", true);
        return modelMap;
    }
}
