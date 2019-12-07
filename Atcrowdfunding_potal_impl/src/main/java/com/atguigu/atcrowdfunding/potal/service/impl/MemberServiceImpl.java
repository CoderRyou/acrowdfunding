package com.atguigu.atcrowdfunding.potal.service.impl;

import com.atguigu.atcrowdfunding.bean.Member;
import com.atguigu.atcrowdfunding.exception.LoginException;
import com.atguigu.atcrowdfunding.potal.dao.MemberMapper;
import com.atguigu.atcrowdfunding.potal.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public Member login(Map<String, Object> param) {

        Member member = memberMapper.login(param);

        if(member==null){
            throw new LoginException("账号或密码错误！");
        }

        return member;
    }

    @Override
    public int updateAcctType(Member member) {
        return memberMapper.updateAcctType(member);
    }

    @Override
    public int updateBasicinfo(Member member) {
        return memberMapper.updateBasicinfo(member);
    }

    @Override
    public int updateEmail(Member member) {
        return memberMapper.updateEmail(member);
    }

    @Override
    public int updateAuthStatus(Member member) {
        return memberMapper.updateAuthStatus(member);
    }

    @Override
    public Member getMemberByPiid(String processInstanceId) {
        return memberMapper.selectMemberByPiid(processInstanceId);
    }

    @Override
    public Member getMemberById(Integer id) {
        return memberMapper.selectMemberById(id);
    }
}
