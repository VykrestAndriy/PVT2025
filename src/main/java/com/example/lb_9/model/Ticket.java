package com.example.lb_9.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal cost;
    private String number;

    @Enumerated(EnumType.STRING)
    private TicketStatus status = TicketStatus.FREE;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Event event;
}