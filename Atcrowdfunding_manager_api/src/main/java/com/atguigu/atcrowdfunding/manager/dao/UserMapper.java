package com.atguigu.atcrowdfunding.manager.dao;

import com.atguigu.atcrowdfunding.bean.Permission;
import com.atguigu.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.bean.UserExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    long countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User login(Map<String, Object> paramMap);

    void insertBatchUser(List<User> users);

    List<User> selectUserByText(@Param("text") String text);

    List<Integer> selectRoleIdByUid(Integer id);

    int insertUserRoleRelationship(@Param("userId") Integer userId,@Param("ids") List<Integer> ids);

    int deleteUserRoleRelationship(@Param("userId") Integer userId,@Param("ids") List<Integer> ids);

    List<Permission> selectMyPermissionByUid(Integer userId);
}