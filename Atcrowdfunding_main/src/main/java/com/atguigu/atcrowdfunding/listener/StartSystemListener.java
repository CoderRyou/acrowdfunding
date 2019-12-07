package com.atguigu.atcrowdfunding.listener;

import com.atguigu.atcrowdfunding.bean.Permission;
import com.atguigu.atcrowdfunding.manager.service.PermissionService;
import com.atguigu.atcrowdfunding.utils.Const;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StartSystemListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //1.将项目的上下文路径（request.getContextPath()）放置导application域中
        ServletContext application = servletContextEvent.getServletContext();
        String contextPath = application.getContextPath();
        application.setAttribute("APP_PATH",contextPath);
        System.out.println("APP_PATH...");

        /**************获取所有的许可uri****************/
        ApplicationContext ioc = WebApplicationContextUtils.getWebApplicationContext(application);
        PermissionService permissionService = ioc.getBean(PermissionService.class);

        List<Permission> permissions = permissionService.getAll();
        Set<String> allUris = new HashSet<>();

        for (Permission permission:permissions){
            allUris.add("/"+permission.getUrl());
        }

        application.setAttribute(Const.ALL_URIS,allUris);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
