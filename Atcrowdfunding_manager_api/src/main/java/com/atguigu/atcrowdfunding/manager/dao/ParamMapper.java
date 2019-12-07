package com.atguigu.atcrowdfunding.manager.dao;

import com.atguigu.atcrowdfunding.bean.Param;
import com.atguigu.atcrowdfunding.bean.ParamExample;

import java.util.List;

public interface ParamMapper {
    long countByExample(ParamExample example);

    int deleteByExample(ParamExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Param record);

    int insertSelective(Param record);

    List<Param> selectByExample(ParamExample example);

    Param selectByPrimaryKey(Integer id);

    int updateByExampleSelective(Param record, ParamExample example);

    int updateByExample(Param record, ParamExample example);

    int updateByPrimaryKeySelective(Param record);

    int updateByPrimaryKey(Param record);
}