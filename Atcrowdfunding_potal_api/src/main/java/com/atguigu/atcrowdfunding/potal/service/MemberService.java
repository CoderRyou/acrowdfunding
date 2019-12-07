package com.atguigu.atcrowdfunding.potal.service;

import com.atguigu.atcrowdfunding.bean.Member;

import java.util.Map;

public interface MemberService {

    Member login(Map<String,Object> param);

    int updateAcctType(Member member);

    int updateBasicinfo(Member member);

    int updateEmail(Member member);

    int updateAuthStatus(Member member);

    Member getMemberByPiid(String processInstanceId);

    Member getMemberById(Integer id);
}
