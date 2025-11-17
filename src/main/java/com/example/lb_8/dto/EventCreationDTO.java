package com.example.lb_8.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventCreationDTO {
    private String name;
    private String eventDateStr;
    private PlaceDTO place;
    private List<TicketPackDTO> ticketPacks;
}