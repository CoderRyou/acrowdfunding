package com.atguigu.atcrowdfunding.manager.dao;

import com.atguigu.atcrowdfunding.bean.AccountTypeCert;
import com.atguigu.atcrowdfunding.bean.AccountTypeCertExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AccountTypeCertMapper {
    long countByExample(AccountTypeCertExample example);

    int deleteByExample(AccountTypeCertExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AccountTypeCert record);

    int insertSelective(AccountTypeCert record);

    List<AccountTypeCert> selectByExample(AccountTypeCertExample example);

    AccountTypeCert selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AccountTypeCert record, @Param("example") AccountTypeCertExample example);

    int updateByExample(@Param("record") AccountTypeCert record, @Param("example") AccountTypeCertExample example);

    int updateByPrimaryKeySelective(AccountTypeCert record);

    int updateByPrimaryKey(AccountTypeCert record);

    List<Map<String,Object>> selectCertAccttype();

    int deleteCertAccttype(AccountTypeCert record);
}