package com.atguigu.atcrowdfunding.controller;

import com.atguigu.atcrowdfunding.bean.AjaxResult;
import com.atguigu.atcrowdfunding.bean.Member;
import com.atguigu.atcrowdfunding.bean.Permission;
import com.atguigu.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.manager.service.PermissionService;
import com.atguigu.atcrowdfunding.manager.service.UserService;
import com.atguigu.atcrowdfunding.potal.service.MemberService;
import com.atguigu.atcrowdfunding.utils.Const;
import com.atguigu.atcrowdfunding.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class DispatcherController {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private MemberService memberService;

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/login")
    public String login(HttpServletRequest request,HttpSession session){
        //判断是否需要自动登录
        String logintype = null;
        Cookie[] cookies = request.getCookies();
        if(cookies != null){//如果客户端禁用了cookie，那么无法获取cookie信息
            for (Cookie cookie:cookies){
                if("logincode".equals(cookie.getName())){
                    String logincode = cookie.getValue();
                    System.out.println("用户存放到cookie中的键值对：logincode:"+logincode);
                    String[] split = logincode.split("&");
                    if (split.length == 3){
                        String loginacct = split[0].split("=")[1];
                        String userpswd = split[1].split("=")[1];
                        logintype = split[2].split("=")[1];

                        Map<String,Object> paramMap = new HashMap<>();
                        paramMap.put("loginacct",loginacct);
                        paramMap.put("userpswd",userpswd);
                        paramMap.put("type",logintype);

                        if("user".equals(logintype)){
                            User user = userService.login(paramMap);

                            if(user != null){
                                session.setAttribute(Const.LOGIN_USER,user);

                                List<Permission> myPermissions = userService.getMyPermissionsByUid(user.getId());

                                Permission root = null;

                                Map<Integer,Permission> permissionMap = new HashMap<>();

                                Set<String> myUris = new HashSet<>();

                                for (Permission permission:myPermissions) {
                                    permissionMap.put(permission.getId(),permission);
                                    myUris.add("/"+permission.getUrl());
                                }

                                for (Permission permission:myPermissions){
                                    if (permission.getPid() == null){
                                        root = permission;
                                    }else{
                                        Permission parent = permissionMap.get(permission.getPid());
                                        parent.getChildren().add(permission);
                                    }
                                }

                                session.setAttribute("root",root);
                                session.setAttribute(Const.MY_URIS,myUris);

                                return "redirect:/main.htm";
                            }
                        }else if("member".equals(logintype)){
                            Member member = memberService.login(paramMap);

                            if(member != null){
                                session.setAttribute(Const.LOGIN_MEMBER,member);
                                return "redirect:/member/index.htm";
                            }
                        }
                    }
                }
            }//end for
        }//end if


        return "login";
    }

    @RequestMapping("/main")
    public String main(){
        return "main";
    }

    @RequestMapping("/doLogout")
    public String doLogout(HttpSession session,HttpServletRequest request,HttpServletResponse response){

        session.invalidate();

        Cookie cookie = new Cookie("logincode", null);
        cookie.setMaxAge(0);
        cookie.setPath(request.getContextPath());
        response.addCookie(cookie);

        return "redirect:/index.htm";
    }

    @RequestMapping("/doLogin")
    @ResponseBody
    public AjaxResult doLogin(String loginacct, String userpswd, String type,
                              String rememberme, HttpSession session, HttpServletResponse response){
        AjaxResult result;

        session.removeAttribute(Const.LOGIN_MEMBER);
        session.removeAttribute(Const.LOGIN_USER);

        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("loginacct",loginacct);
        paramMap.put("userpswd", MD5Util.digest(userpswd));
        paramMap.put("type",type);

        User user = null;
        Member member = null;
        try {
            if (type.equals("user")){
                user = userService.login(paramMap);
                session.setAttribute(Const.LOGIN_USER,user);

                if ("1".equals(rememberme)){
                    String logincode = "\"loginacct="+user.getLoginacct()+"&userpswd="+user.getUserpswd()+"&logintype=user\"";

                    System.out.println("用户存放到cookie中的键值对：logincode:"+logincode);

                    Cookie c =new Cookie("logincode",logincode);

                    c.setMaxAge(60*60*24*14);//2周时间cookie过期
                    c.setPath("/");//表示任何请求路径都可以访问

                    response.addCookie(c);
                }

                result = AjaxResult.success().add("type","user");
            }else if (type.equals("member")){
                member = memberService.login(paramMap);
                session.setAttribute(Const.LOGIN_MEMBER,member);

                if ("1".equals(rememberme)){
                    String logincode = "\"loginacct="+member.getLoginacct()+"&userpswd="+member.getUserpswd()+"&logintype=member\"";

                    System.out.println("用户存放到cookie中的键值对：logincode:"+logincode);

                    Cookie c =new Cookie("logincode",logincode);

                    c.setMaxAge(60*60*24*14);//2周时间cookie过期
                    c.setPath("/");//表示任何请求路径都可以访问

                    response.addCookie(c);
                }

                return AjaxResult.success().add("type","member");
            }else{
                return AjaxResult.fail();
            }
        } catch (Exception e) {
            result = AjaxResult.fail();
            result.setMsg("登录失败");
            e.printStackTrace();
        }

        //========================================//

        List<Permission> myPermissions = userService.getMyPermissionsByUid(user.getId());

        Permission root = null;

        Map<Integer,Permission> permissionMap = new HashMap<>();

        Set<String> myUris = new HashSet<>();

        for (Permission permission:myPermissions) {
            permissionMap.put(permission.getId(),permission);
            myUris.add("/"+permission.getUrl());
        }

        for (Permission permission:myPermissions){
            if (permission.getPid() == null){
                root = permission;
            }else{
                Permission parent = permissionMap.get(permission.getPid());
                parent.getChildren().add(permission);
            }
        }

        session.setAttribute("root",root);
        session.setAttribute(Const.MY_URIS,myUris);

        return result;
    }

    /*
    @RequestMapping("/doLogin")
    public String doLogin(String loginacct, String userpswd, String type, HttpSession session){
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("loginacct",loginacct);
        paramMap.put("userpswd",userpswd);
        paramMap.put("type",type);

        User user = userService.login(paramMap);

        session.setAttribute(Const.LOGIN_USER,user);

        return "redirect:/main.htm";
    }
    */
}
