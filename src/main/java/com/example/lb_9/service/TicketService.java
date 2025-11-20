package com.example.lb_9.service;

import com.example.lb_9.dto.EventCreationDTO;
import com.example.lb_9.model.*;
import com.example.lb_9.repository.CustomerRepository;
import com.example.lb_9.repository.EventRepository;
import com.example.lb_9.repository.PlaceRepository;
import com.example.lb_9.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService {

    private final PlaceRepository placeRepository;
    private final EventRepository eventRepository;
    public final TicketRepository ticketRepository;
    public final CustomerRepository customerRepository;

    public TicketService(PlaceRepository placeRepository, EventRepository eventRepository, TicketRepository ticketRepository, CustomerRepository customerRepository) {
        this.placeRepository = placeRepository;
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Event createEventFromDTO(EventCreationDTO dto) {
        Place place = placeRepository.findByName(dto.getPlace().getName())
                .orElseGet(() -> {
                    Place newPlace = new Place(null, dto.getPlace().getName(), dto.getPlace().getAddress(), null);
                    return placeRepository.save(newPlace);
                });

        LocalDateTime eventDate = LocalDateTime.parse(dto.getEventDateStr(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Event event = new Event(null, dto.getName(), eventDate, place, new ArrayList<>());
        event.setPlace(place);
        Event savedEvent = eventRepository.save(event);

        List<Ticket> tickets = new ArrayList<>();
        int ticketNumberCounter = 1;

        for (var pack : dto.getTicketPacks()) {
            for (int i = 0; i < pack.getCount(); i++) {
                Ticket ticket = new Ticket();
                ticket.setCost(pack.getCost());
                ticket.setNumber(String.format("№%d", ticketNumberCounter++));
                ticket.setEvent(savedEvent);
                ticket.setStatus(TicketStatus.FREE);
                tickets.add(ticket);
            }
        }
        ticketRepository.saveAll(tickets);

        savedEvent.setTickets(tickets);
        return savedEvent;
    }

    public List<Ticket> findFreeTicketsByEventName(String eventName) {
        return ticketRepository.findByEventNameAndStatus(eventName, TicketStatus.FREE);
    }

    public List<Event> findUpcomingEvents() {
        return eventRepository.findByEventDateAfterOrderByEventDateAsc(LocalDateTime.now());
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public List<Customer> findAllCustomers() {
        return customerRepository.findAll();
    }

    @Transactional
    public String assignTicketToCustomer(Long ticketId, Long customerId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Квиток не знайдено: " + ticketId));
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Покупця не знайдено: " + customerId));

        if (ticket.getStatus() == TicketStatus.SOLD) {
            throw new IllegalStateException("Квиток вже продано.");
        }

        ticket.setCustomer(customer);
        ticket.setStatus(TicketStatus.SOLD);
        ticketRepository.save(ticket);

        return ticket.getEvent().getName();
    }
}