package com.example.lb_8.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TicketPackDTO {
    private BigDecimal cost;
    private Integer count;
}