package com.atguigu.atcrowdfunding.interceptor;

import com.atguigu.atcrowdfunding.bean.Permission;
import com.atguigu.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.manager.service.PermissionService;
import com.atguigu.atcrowdfunding.manager.service.UserService;
import com.atguigu.atcrowdfunding.utils.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PermissionInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private PermissionService permissionService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //1.获取所有的许可uri
        Set<String> allUris = (Set<String>)request.getSession().getServletContext().getAttribute(Const.ALL_URIS);

        Set<String> myUris =null;

        //2.判断请求uri是否在所有许可uri范围内
        String reqUri = request.getServletPath();
        if(allUris.contains(reqUri)){//在范围内

            //3.判断当前用户是否有权限访问
            User user = (User) request.getSession().getAttribute(Const.LOGIN_USER);
            myUris = (Set<String>)request.getSession().getAttribute(Const.MY_URIS);

            if(myUris.contains(reqUri)){
                return true;
            }else{
                response.sendRedirect(request.getContextPath()+"/login.htm");
                return false;
            }

        }else {
            return true;
        }

    }
}
