package com.example.lb_9.repository;

import com.example.lb_9.model.Ticket;
import com.example.lb_9.model.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByEventNameAndStatus(String eventName, TicketStatus status);
}