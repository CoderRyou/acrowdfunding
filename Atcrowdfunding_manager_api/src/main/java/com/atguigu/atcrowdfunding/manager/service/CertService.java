package com.atguigu.atcrowdfunding.manager.service;

import com.atguigu.atcrowdfunding.bean.Cert;
import com.atguigu.atcrowdfunding.bean.MemberCert;

import java.util.List;
import java.util.Map;

public interface CertService {

    List<Cert> queryAllCert();

    List<Cert> queryCertByAccttype(String accttype);

    int insertMemberCert(List<MemberCert> certimgs);

    List<Map<String,Object>> queryCertByMemberid(Integer id);
}
