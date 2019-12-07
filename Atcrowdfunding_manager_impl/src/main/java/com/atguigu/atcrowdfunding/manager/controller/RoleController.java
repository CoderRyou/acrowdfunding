package com.atguigu.atcrowdfunding.manager.controller;

import com.atguigu.atcrowdfunding.bean.AjaxResult;
import com.atguigu.atcrowdfunding.bean.Permission;
import com.atguigu.atcrowdfunding.bean.Role;
import com.atguigu.atcrowdfunding.manager.service.PermissionService;
import com.atguigu.atcrowdfunding.manager.service.RoleService;
import com.atguigu.atcrowdfunding.utils.Data;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    @Autowired
    PermissionService permissionService;

    @RequestMapping("/doAssignPermission")
    @ResponseBody
    public AjaxResult AssignPermission(Integer roleId,Data data){

        try {
            roleService.deleteRolePermissionByRoleId(roleId);

            roleService.saveRolePermissionRelationship(roleId,data.getIds());
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.fail();
        }

        return AjaxResult.success();
    }

    @ResponseBody
    @RequestMapping("/loadPermission")
    public List<Permission> loadPermission(Integer roleId){

        List<Permission> permissions = permissionService.getAll();

        List<Permission> root = new ArrayList<>();

        Map<Integer,Permission> permissionMap = new HashMap<>();

        List<Integer> permissionIds = roleService.getPermissionIdsByRoleId(roleId);

        for (Permission permission:permissions) {
            if (permissionIds.contains(permission.getId())){
                permission.setChecked(true);
            }
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

    @RequestMapping("/assignPermission")
    public String toAssignPermission(Integer id){
        return "role/assignPermission";
    }

    @RequestMapping("/doEditRole")
    @ResponseBody
    public AjaxResult EditRole(Role role){

        try {
            roleService.saveRole(role);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.fail();
        }

        return AjaxResult.success();
    }

    @RequestMapping("/editRole")
    public String toEditRole(Integer id, Model model){

        Role role = roleService.getRoleById(id);
        model.addAttribute("role",role);

        return "role/editRole";
    }

    /**
     * 删除角色
     * @return
     */
    @ResponseBody
    @RequestMapping("/doDelete")
    public AjaxResult deleteRole(Data data){

        try {
            roleService.deleteRole(data.getIds());
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.fail();
        }

        return AjaxResult.success();
    }

    /**
     * 添加角色
     * @return
     */
    @ResponseBody
    @RequestMapping("/doAddRole")
    public AjaxResult addRole(Role role){

        try {
            roleService.addRole(role);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.fail();
        }

        return AjaxResult.success();
    }

    /**
     * 跳转到角色添加页面
     * @return
     */
    @RequestMapping("addRole")
    public String toAddRole(){

        return "role/addRole";
    }

    /**
     * 采用异步加载用户列表数据
     * @return
     */
    @RequestMapping("/loadRoles")
    @ResponseBody
    public AjaxResult loadUsers(@RequestParam(value = "pn",defaultValue = "1") Integer pn, String text){
        //在查询之前只需要调用，传入页码以及分页大小
        PageHelper.startPage(pn,10);
        List<Role> roles = roleService.getRoleByText(text);
        //封装了详细的分页信息，包括有我们查询出来的数据,传入连续显示的页数
        PageInfo pageInfo = new PageInfo(roles,5);
        return AjaxResult.success().add("pageInfo",pageInfo);
    }

    /**
     * 跳转到角色维护页面
     * @return
     */
    @RequestMapping("/index")
    public String toRole(){
        return "role/role";
    }
}
