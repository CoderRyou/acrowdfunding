package com.atguigu.atcrowdfunding.manager.service;

import com.atguigu.atcrowdfunding.bean.AccountTypeCert;

import java.util.List;
import java.util.Map;

public interface CertTypeService {

    List<Map<String,Object>> queryCertAccttype();

    int insertCertAccttype(AccountTypeCert record);

    int deleteCertAccttype(AccountTypeCert record);
}
