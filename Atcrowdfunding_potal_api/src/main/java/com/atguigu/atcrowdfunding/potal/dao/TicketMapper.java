package com.atguigu.atcrowdfunding.potal.dao;

import com.atguigu.atcrowdfunding.bean.Ticket;

import java.util.List;

public interface TicketMapper {
    Ticket selectTicketByMemberidAndStatus(Integer memberid);

    List<Ticket> selectAll();

    int insertTicket(Ticket ticket);

    int updatePstepById(Ticket ticket);

    int update(Ticket ticket);
}
