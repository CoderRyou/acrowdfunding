package com.atguigu.atcrowdfunding.manager.controller;

import com.atguigu.atcrowdfunding.bean.AjaxResult;
import com.atguigu.atcrowdfunding.bean.Permission;
import com.atguigu.atcrowdfunding.manager.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    PermissionService permissionService;

    @ResponseBody
    @RequestMapping("/doEditPermission")
    public AjaxResult EditPermission(Permission permission){

        try {
            permissionService.savePermission(permission);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.fail();
        }

        return AjaxResult.success();
    }

    @RequestMapping("/editPermission")
    public String toEditPermission(Integer id,Model model){

        Permission permission = permissionService.getPermissionById(id);

        model.addAttribute("permission",permission);

        return "permission/editPermission";
    }

    @ResponseBody
    @RequestMapping("/doDelete")
    public AjaxResult DeletePermission(Integer id){

        try {
            permissionService.deletePermisionById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.fail();
        }

        return AjaxResult.success();
    }

    @ResponseBody
    @RequestMapping("/doAddPermission")
    public AjaxResult addPermission(Permission permission){

        try {
            permissionService.addPermission(permission);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.fail();
        }

        return AjaxResult.success();
    }

    @RequestMapping("/addPermission")
    public String toAddPermission(){
        return "permission/addPermission";
    }

    /**
     * 加载许可列表
     * @return
     */
    @ResponseBody
    @RequestMapping("/loadData")
    public List<Permission> loadPermission(){

        List<Permission> permissions = permissionService.getAll();

        List<Permission> root = new ArrayList<>();

        Map<Integer,Permission> permissionMap = new HashMap<>();

        for (Permission permission:permissions) {
            permissionMap.put(permission.getId(),permission);
        }

        for (Permission permission:permissions){
            if (permission.getPid() == null){
                root.add(permission);
            }else{
                Permission parent = permissionMap.get(permission.getPid());
                parent.getChildren().add(permission);
            }
        }

        return root;
    }

    @RequestMapping("/index")
    public String toPermission(){
        return "permission/permission";
    }

}
