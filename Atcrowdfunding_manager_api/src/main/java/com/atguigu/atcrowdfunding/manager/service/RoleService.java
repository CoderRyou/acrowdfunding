package com.atguigu.atcrowdfunding.manager.service;

import com.atguigu.atcrowdfunding.bean.Role;
import org.springframework.stereotype.Service;

import java.util.List;

public interface RoleService {
    List<Role> getRoleByText(String text);

    int addRole(Role role);

    int deleteRole(List<Integer> ids);

    Role getRoleById(Integer id);

    int saveRole(Role role);

    List<Integer> getPermissionIdsByRoleId(Integer roleId);

    int saveRolePermissionRelationship(Integer roleId, List<Integer> ids);

    int deleteRolePermissionByRoleId(Integer roleId);
}
