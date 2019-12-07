package com.atguigu.atcrowdfunding.manager.service;

import com.atguigu.atcrowdfunding.bean.Permission;

import java.util.List;

public interface PermissionService {

    List<Permission> getAll();

    int addPermission(Permission permission);

    int deletePermisionById(Integer id);

    Permission getPermissionById(Integer id);

    int savePermission(Permission permission);
}
