package com.atguigu.atcrowdfunding.manager.dao;

import com.atguigu.atcrowdfunding.bean.Return;
import com.atguigu.atcrowdfunding.bean.ReturnExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReturnMapper {
    long countByExample(ReturnExample example);

    int deleteByExample(ReturnExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Return record);

    int insertSelective(Return record);

    List<Return> selectByExample(ReturnExample example);

    Return selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Return record, @Param("example") ReturnExample example);

    int updateByExample(@Param("record") Return record, @Param("example") ReturnExample example);

    int updateByPrimaryKeySelective(Return record);

    int updateByPrimaryKey(Return record);
}