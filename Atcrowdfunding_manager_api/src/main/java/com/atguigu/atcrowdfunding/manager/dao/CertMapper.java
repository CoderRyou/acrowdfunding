package com.atguigu.atcrowdfunding.manager.dao;

import com.atguigu.atcrowdfunding.bean.Cert;
import com.atguigu.atcrowdfunding.bean.CertExample;
import com.atguigu.atcrowdfunding.bean.MemberCert;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CertMapper {
    long countByExample(CertExample example);

    int deleteByExample(CertExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Cert record);

    int insertSelective(Cert record);

    List<Cert> selectByExample(CertExample example);

    Cert selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Cert record, @Param("example") CertExample example);

    int updateByExample(@Param("record") Cert record, @Param("example") CertExample example);

    int updateByPrimaryKeySelective(Cert record);

    int updateByPrimaryKey(Cert record);

    List<Cert> selectCertByAccttype(String accttype);

    int insertMemberCert(List<MemberCert> certimgs);

    List<Map<String,Object>> selectCertByMemberid(Integer memberid);
}