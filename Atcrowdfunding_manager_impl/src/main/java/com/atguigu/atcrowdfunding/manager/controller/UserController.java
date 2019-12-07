package com.atguigu.atcrowdfunding.manager.controller;

import com.atguigu.atcrowdfunding.bean.AjaxResult;
import com.atguigu.atcrowdfunding.bean.Role;
import com.atguigu.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.manager.service.RoleService;
import com.atguigu.atcrowdfunding.manager.service.UserService;
import com.atguigu.atcrowdfunding.utils.Data;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/user")
@Controller
public class UserController {

    @Autowired
    UserService userService;

    @ResponseBody
    @RequestMapping("doUnAssignRole")
    public AjaxResult unAssignRole(Integer userId,Data data){

        try {
            userService.deleteUserRoleRelationship(userId,data.getIds());
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.fail();
        }

        return AjaxResult.success();
    }


    @ResponseBody
    @RequestMapping("doAssignRole")
    public AjaxResult assignRole(Integer userId,Data data){

        try {
            userService.saveUserRoleRelationship(userId,data.getIds());
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.fail();
        }

        return AjaxResult.success();
    }

    /**
     * 加载用户角色信息
     * @param id
     * @return
     */
    @RequestMapping("/loadRoles")
    @ResponseBody
    public AjaxResult loadRoles(Integer id){

        List<Role> leftRoleList = null;
        List<Role> rightRoleList = null;
        try {
            List<Integer> roleIds = userService.getRoleIdByUid(id);
            List<Role> allRoles = userService.getAllRole();

            leftRoleList = new ArrayList<>();
            rightRoleList = new ArrayList<>();

            for (Role role : allRoles){

                if(roleIds.contains(role.getId())){
                    rightRoleList.add(role);
                }else {
                    leftRoleList.add(role);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.fail();
        }

        return AjaxResult.success()
                .add("leftRoleList",leftRoleList)
                .add("rightRoleList",rightRoleList);
    }

    @RequestMapping("/assignRole")
    public String toAssignRole(Integer id){
        return "user/assignRole";
    }

    /**
     * 更新用户
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/doEditUser",method = RequestMethod.PUT)
    public AjaxResult doEditUser(User user){

        try {
            userService.saveUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.fail();
        }

        return AjaxResult.success();
    }

    /**
     * 跳转更新用户界面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("editUser")
    public String toEditUser(Integer id, Model model){

        User user = userService.getUserById(id);
        model.addAttribute("user",user);

        return "/user/editUser";
    }

    /**
     * 删除用户
     * @param data
     * @return
     */
    @RequestMapping(value = "/doDelete")
    @ResponseBody
    public AjaxResult deleteUser(Data data){

//        System.out.println(data.getUserId().get(0));
        try {
            userService.deleteBatch(data.getIds());
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.fail();
        }

        return AjaxResult.success();
    }

    /**
     * 添加后台用户数据
     * @param user
     * @return
     */
    @RequestMapping(value = "/doAddUser",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult addUser(User user){

        System.out.println(user);
        try {
            userService.addUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.fail();
        }

        return AjaxResult.success();
    }

    /**
     * 跳转到添加用户界面
     * @return
     */
    @RequestMapping("/addUser")
    public String toAddUser(){
        return "user/addUser";
    }

    /**
     * 采用异步加载用户列表数据
     * @return
     */
    @RequestMapping("/loadUsers")
    @ResponseBody
    public AjaxResult loadUsers(@RequestParam(value = "pn",defaultValue = "1") Integer pn,String text){
        //在查询之前只需要调用，传入页码以及分页大小
        PageHelper.startPage(pn,10);
        List<User> users = userService.getUserByText(text);
        //封装了详细的分页信息，包括有我们查询出来的数据,传入连续显示的页数
        PageInfo pageInfo = new PageInfo(users,5);
        return AjaxResult.success().add("pageInfo",pageInfo);
    }

    /**
     * 跳转到用户维护页面
     * @return
     */
    @RequestMapping("/index")
    public String toUser(){
        return "/user/user";
    }

}
