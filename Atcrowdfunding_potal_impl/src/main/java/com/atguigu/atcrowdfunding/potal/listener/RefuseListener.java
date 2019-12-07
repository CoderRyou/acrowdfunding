package com.atguigu.atcrowdfunding.potal.listener;

import com.atguigu.atcrowdfunding.bean.Member;
import com.atguigu.atcrowdfunding.bean.Ticket;
import com.atguigu.atcrowdfunding.potal.service.MemberService;
import com.atguigu.atcrowdfunding.potal.service.TicketService;
import com.atguigu.atcrowdfunding.utils.ApplicationContextUtils;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.context.ApplicationContext;

//实名认证拒绝的操作
public class RefuseListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        System.out.println("refuseListener......");
        Integer memberid = (Integer) delegateExecution.getVariable("memberid");
        //获取IOC容器，通过自定义的工具类，实现Spring接口，以接口注入方式获取IOC容器
        ApplicationContext applicationContext = ApplicationContextUtils.applicationContext;

        //获取TicketService和MemberService对象
        TicketService ticketService = applicationContext.getBean(TicketService.class);
        MemberService memberService = applicationContext.getBean(MemberService.class);
        //更新t_member表的authstatus字段：1 -> 2 - 已实名认证
        Member member = new Member();
        member.setId(memberid);
        member.setAuthstatus("0");
        memberService.updateAuthStatus(member);

        //更新t_ticket表单的status字段0 -> 1 表示流程结束
        Ticket ticket = ticketService.getTicketByMemberidAndStatus(memberid);
        ticket.setStatus("1");
        ticketService.update(ticket);
    }
}
