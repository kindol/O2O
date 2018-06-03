package com.kindol.o2o.interceptor.shopadmin;

import com.kindol.o2o.entity.PersonInfo;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 店家管理系统登录验证拦截器
 */
public class ShopLoginInterceptor extends HandlerInterceptorAdapter {

    /**
     * 主要做事前拦截，即用户操作发生前（controller之前），改写preHandle里的逻辑，进行拦截
     * @param request
     * @param response
     * @param handler 需要拦截的controller
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从session中取出用户信息来
        Object userObj = request.getSession().getAttribute("user");
        if (userObj != null){
            //若用户信息不为空则将session里的用户信息转换成PersonInfo实体类对象
            PersonInfo user = (PersonInfo)userObj;
            //控制判断，确保uerId不为空并且该账号的可用状态为1，并且用户类型为店家，验证通过则返回正常执行
            //注意，返回true将会接着到下面的interceptor进行下一步校验，直到最后一个校验，最后到达controller
            if (user != null && user.getUserId() != null && user.getUserId() > 0 && user.getEnableStatus() == 1)
                return true;
        }
        //不满足验证，则直接跳转到账号登录页面
        PrintWriter out = response.getWriter();
        out.print("<html>");
        out.print("<script>");
        out.print("window.open ('" + request.getContextPath() + "/local/login?usertype=2','_self')");
        out.print("</script>");
        out.print("</html>");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }
}
