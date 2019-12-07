package com.atguigu.atcrowdfunding.potal.service;

import com.atguigu.atcrowdfunding.bean.Ticket;

public interface TicketService {
    Ticket getTicketByMemberidAndStatus(Integer memberid);

    int insertTicket(Ticket ticket);

    int updatePstepById(Ticket ticket);

    int update(Ticket ticket);
}
