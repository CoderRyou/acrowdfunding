package com.atguigu.atcrowdfunding.manager.service.impl;

import com.atguigu.atcrowdfunding.bean.Permission;
import com.atguigu.atcrowdfunding.bean.Role;
import com.atguigu.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.bean.UserExample;
import com.atguigu.atcrowdfunding.exception.LoginException;
import com.atguigu.atcrowdfunding.manager.dao.RoleMapper;
import com.atguigu.atcrowdfunding.manager.dao.UserMapper;
import com.atguigu.atcrowdfunding.manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public User login(Map<String, Object> paramMap) {

        User user = userMapper.login(paramMap);

        if(user == null){
            throw new LoginException("用户账户或密码不正确！");
        }

        return user;
    }

    @Override
    public List<User> getAll() {
        return userMapper.selectByExample(null);
    }

    @Override
    public int addUser(User user) {
        //设置默认密码
        user.setUserpswd("123");
        //创建时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        user.setCreatetime(sdf.format(new Date()));
        return userMapper.insertSelective(user);
    }

    @Override
    public int deleteBatch(List<Integer> userId) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(userId);
        return userMapper.deleteByExample(example);
    }

    @Override
    public User getUserById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public int saveUser(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public List<Integer> getRoleIdByUid(Integer id) {
        return userMapper.selectRoleIdByUid(id);
    }

    @Override
    public List<Role> getAllRole() {
        return roleMapper.selectByExample(null);
    }

    @Override
    public List<User> getUserByText(String text) {
        return userMapper.selectUserByText(text);
    }

    @Override
    public int saveUserRoleRelationship(Integer userId, List<Integer> ids) {
        return userMapper.insertUserRoleRelationship(userId,ids);
    }

    @Override
    public int deleteUserRoleRelationship(Integer userId, List<Integer> ids) {
        return userMapper.deleteUserRoleRelationship(userId,ids);
    }

    @Override
    public List<Permission> getMyPermissionsByUid(Integer userId) {
        return userMapper.selectMyPermissionByUid(userId);
    }
}
