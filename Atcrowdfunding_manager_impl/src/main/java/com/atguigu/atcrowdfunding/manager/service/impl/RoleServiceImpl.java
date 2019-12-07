package com.atguigu.atcrowdfunding.manager.service.impl;

import com.atguigu.atcrowdfunding.bean.Role;
import com.atguigu.atcrowdfunding.bean.RoleExample;
import com.atguigu.atcrowdfunding.manager.dao.RoleMapper;
import com.atguigu.atcrowdfunding.manager.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> getRoleByText(String text) {
        return roleMapper.selectRoleByText(text);
    }


    @Override
    public int saveRole(Role role) {
        return roleMapper.updateByPrimaryKeySelective(role);
    }

    @Override
    public Role getRoleById(Integer id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    @Override
    public int deleteRole(List<Integer> ids) {
        RoleExample example = new RoleExample();
        RoleExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(ids);
        return roleMapper.deleteByExample(example);
    }

    @Override
    public int addRole(Role role) {
        return roleMapper.insertSelective(role);
    }

    @Override
    public List<Integer> getPermissionIdsByRoleId(Integer roleId) {
        return roleMapper.selectPermissionIdsByRoleId(roleId);
    }

    @Override
    public int saveRolePermissionRelationship(Integer roleId, List<Integer> ids) {
        return roleMapper.insertRolePermissionRelationship(roleId,ids);
    }

    @Override
    public int deleteRolePermissionByRoleId(Integer roleId) {
        return roleMapper.deleteRolePermissionByRoleId(roleId);
    }
}
