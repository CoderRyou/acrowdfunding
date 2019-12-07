package com.atguigu.atcrowdfunding.manager.service.impl;

import com.atguigu.atcrowdfunding.bean.Cert;
import com.atguigu.atcrowdfunding.bean.MemberCert;
import com.atguigu.atcrowdfunding.manager.dao.CertMapper;
import com.atguigu.atcrowdfunding.manager.service.CertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CertServiceImpl implements CertService {

    @Autowired
    private CertMapper certMapper;

    @Override
    public List<Cert> queryAllCert() {
        return certMapper.selectByExample(null);
    }

    @Override
    public List<Cert> queryCertByAccttype(String accttype) {
        return certMapper.selectCertByAccttype(accttype);
    }

    @Override
    public int insertMemberCert(List<MemberCert> certimgs) {
        return certMapper.insertMemberCert(certimgs);
    }

    @Override
    public List<Map<String, Object>> queryCertByMemberid(Integer id) {
        return certMapper.selectCertByMemberid(id);
    }
}
