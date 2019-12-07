package com.atguigu.atcrowdfunding.manager.service.impl;

import com.atguigu.atcrowdfunding.bean.AccountTypeCert;
import com.atguigu.atcrowdfunding.manager.dao.AccountTypeCertMapper;
import com.atguigu.atcrowdfunding.manager.service.CertTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CertTypeServiceImpl implements CertTypeService {

    @Autowired
    private AccountTypeCertMapper accountTypeCertMapper;

    @Override
    public List<Map<String, Object>> queryCertAccttype() {
        return accountTypeCertMapper.selectCertAccttype();
    }

    @Override
    public int insertCertAccttype(AccountTypeCert record) {
        return accountTypeCertMapper.insert(record);
    }

    @Override
    public int deleteCertAccttype(AccountTypeCert record) {
        return accountTypeCertMapper.deleteCertAccttype(record);
    }


}
