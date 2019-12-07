package com.atguigu.atcrowdfunding.manager.controller;

import com.atguigu.atcrowdfunding.bean.Advertisement;
import com.atguigu.atcrowdfunding.bean.AjaxResult;
import com.atguigu.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.manager.service.AdvertService;
import com.atguigu.atcrowdfunding.utils.Const;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/advert")
public class AdvertController {

    @Autowired
    AdvertService advertService;

    /**
     * 查询广告列表
     * @param pn
     * @return
     */
    @RequestMapping("/loadAdverts")
    @ResponseBody
    public AjaxResult loadAdverts(@RequestParam(value = "pn",defaultValue = "1") Integer pn){
        //在查询之前只需要调用，传入页码以及分页大小
        PageHelper.startPage(pn,10);
        List<Advertisement> users = advertService.getAllAdverts();
        //封装了详细的分页信息，包括有我们查询出来的数据,传入连续显示的页数
        PageInfo pageInfo = new PageInfo(users,5);
        return AjaxResult.success().add("pageInfo",pageInfo);
    }

    /**
     * 添加广告
     * @param request
     * @param advert
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping("/doAdd")
    public AjaxResult doAdd(HttpServletRequest request, Advertisement advert, HttpSession session){

        try{
            MultipartHttpServletRequest mreq = (MultipartHttpServletRequest) request;

            MultipartFile mfile = mreq.getFile("advpic");

            String name = mfile.getOriginalFilename();//java.jpg
            String extname = name.substring(name.lastIndexOf("."));//.jpg

            String iconpath = UUID.randomUUID().toString()+extname;

            ServletContext servletContext = session.getServletContext();
            String realpath = servletContext.getRealPath("/pics");

            String path = realpath+"\\adv\\"+iconpath;

            System.out.println(path);
            mfile.transferTo(new File(path));

            User user = (User)session.getAttribute(Const.LOGIN_USER);
            advert.setUserid(user.getId());
            advert.setStatus("1");
            advert.setIconpath(iconpath);

            int count = advertService.insertAdvert(advert);
            return AjaxResult.success();
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.fail();
        }

    }

    @RequestMapping("/form")
    public String toAddAdvert(){
        return "/advert/addAdvert";
    }

    @RequestMapping("/index")
    public String toAdvert(){
        return "/advert/advert";
    }
}
