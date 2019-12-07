package com.atguigu.atcrowdfunding.test;

import com.atguigu.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.manager.dao.UserMapper;
import com.atguigu.atcrowdfunding.utils.MD5Util;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class InsertBatchUser {
    public static void main(String[] args) {
        //1.创建springIOC容器
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/spring-context.xml");
        //2.从容器中获取userMapper
        UserMapper userMapper = ac.getBean(UserMapper.class);

        List<User> users = new ArrayList<User>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());

        for (int i = 0; i < 100; i++) {
            String loginacct = UUID.randomUUID().toString().substring(0, 5);
            String pswd = "123456";

            User user = new User();
            user.setLoginacct(loginacct);
            user.setUserpswd(MD5Util.digest(pswd));
            user.setEmail(loginacct+"@qq.com");
            user.setUsername("管理员");
            user.setCreatetime(time);

            users.add(user);
        }

        userMapper.insertBatchUser(users);

    }
}
