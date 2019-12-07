package com.atguigu.atcrowdfunding.potal.service.impl;

import com.atguigu.atcrowdfunding.bean.Ticket;
import com.atguigu.atcrowdfunding.potal.dao.TicketMapper;
import com.atguigu.atcrowdfunding.potal.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    TicketMapper ticketMapper;

    @Override
    public Ticket getTicketByMemberidAndStatus(Integer memberid) {
        return ticketMapper.selectTicketByMemberidAndStatus(memberid);
    }

    @Override
    public int insertTicket(Ticket ticket) {
        return ticketMapper.insertTicket(ticket);
    }

    @Override
    public int updatePstepById(Ticket ticket) {
        return ticketMapper.updatePstepById(ticket);
    }

    @Override
    public int update(Ticket ticket) {
        return ticketMapper.update(ticket);
    }
}
