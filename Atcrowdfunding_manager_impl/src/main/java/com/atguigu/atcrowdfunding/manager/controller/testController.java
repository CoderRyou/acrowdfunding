package com.atguigu.atcrowdfunding.manager.controller;

import com.atguigu.atcrowdfunding.manager.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class testController {

    @Autowired
    TestService testService;

    @RequestMapping("/test")
    public String test(){
        System.out.println("TestController");
        testService.insert();
        return "success";
    }
}
