package com.atguigu.atcrowdfunding.manager.service;

import com.atguigu.atcrowdfunding.bean.Permission;
import com.atguigu.atcrowdfunding.bean.Role;
import com.atguigu.atcrowdfunding.bean.User;

import java.util.List;
import java.util.Map;


public interface UserService {

    User login(Map<String, Object> paramMap);

    List<User> getAll();

    int addUser(User user);

    int deleteBatch(List<Integer> userId);

    User getUserById(Integer id);

    int saveUser(User user);

    List<User> getUserByText( String text);

    List<Integer> getRoleIdByUid(Integer id);

    List<Role> getAllRole();

    int saveUserRoleRelationship(Integer userId, List<Integer> ids);

    int deleteUserRoleRelationship(Integer userId, List<Integer> ids);

    List<Permission> getMyPermissionsByUid(Integer userId);
}
