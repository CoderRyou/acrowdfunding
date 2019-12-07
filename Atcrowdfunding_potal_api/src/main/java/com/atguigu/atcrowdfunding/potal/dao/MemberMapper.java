package com.atguigu.atcrowdfunding.potal.dao;

import com.atguigu.atcrowdfunding.bean.Member;
import org.springframework.stereotype.Repository;

import java.util.Map;

public interface MemberMapper {

    Member login(Map<String,Object> param);

    int updateAcctType(Member member);

    int updateBasicinfo(Member member);

    int updateEmail(Member member);

    int updateAuthStatus(Member member);

    Member selectMemberByPiid(String piid);

    Member selectMemberById(Integer id);
}
