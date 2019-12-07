package com.atguigu.atcrowdfunding.interceptor;

import com.atguigu.atcrowdfunding.bean.Member;
import com.atguigu.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.utils.Const;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

/**
 * 进行登录验证的拦截器类
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //1.指定哪些路径不需要登录验证
        Set<String> uris = new HashSet<>();
        uris.add("/index.htm");
        uris.add("/login.htm");
        uris.add("/doLogin.do");
        uris.add("/reg.htm");
        uris.add("/reg.do");
        uris.add("/doLogout.do");

        //指定哪些路径是会员需要登录后可以访问的
        Set<String> memberUris = new HashSet<String>();
        memberUris.add("/member/index.htm");
        memberUris.add("/member/apply.htm");
        memberUris.add("/member/updateAcctType.do");
        memberUris.add("/member/updateBasicinfo.do");
        memberUris.add("/member/doUploadCert.do");
        memberUris.add("/member/doCheckEmail.do");
        memberUris.add("/member/doCheckCode.do");

        //2.获取请求uri
        String reqUri = request.getServletPath();

        if(uris.contains(reqUri)){
            return true;
        }else {
            //3.进行登录验证
            User user = (User) request.getSession().getAttribute(Const.LOGIN_USER);
            Member member = (Member) request.getSession().getAttribute(Const.LOGIN_MEMBER);
            if(member != null){
                if (memberUris.contains(reqUri)){
                    return true;
                }else {
                    //跳转到登录界面
                    response.sendRedirect(request.getContextPath()+"/login.htm");
                    return false;
                }
            }else if(user != null){
                if(memberUris.contains(reqUri)){
                    //跳转到登录界面
                    response.sendRedirect(request.getContextPath()+"/login.htm");
                    return false;
                }else{
                    return true;
                }
            }else {
                //跳转到登录界面
                response.sendRedirect(request.getContextPath()+"/login.htm");
                return false;
            }
        }

    }
}
