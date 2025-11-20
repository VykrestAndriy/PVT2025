package com.example.lb_9.controller;

import com.example.lb_9.dto.EventCreationDTO;
import com.example.lb_9.dto.TicketPackDTO;
import com.example.lb_9.model.Customer;
import com.example.lb_9.model.Ticket;
import com.example.lb_9.service.TicketService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@Controller
@RequestMapping("/")
public class EventController {

    private final TicketService ticketService;

    public EventController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public String home(Model model) {
        model.addAttribute("events", ticketService.findUpcomingEvents());
        model.addAttribute("searchParam", "");
        model.addAttribute("foundTickets", new ArrayList<Ticket>());
        model.addAttribute("newCustomer", new Customer());
        model.addAttribute("customers", ticketService.findAllCustomers());
        return "events_list";
    }

    @GetMapping("/create-event")
    public String showCreateEventForm(Model model) {
        EventCreationDTO defaultDto = new EventCreationDTO();
        defaultDto.setTicketPacks(new ArrayList<>());
        defaultDto.getTicketPacks().add(new TicketPackDTO());
        model.addAttribute("eventDto", defaultDto);
        return "create_event";
    }

    @PostMapping("/create-event")
    public String createEvent(@ModelAttribute EventCreationDTO eventDto, RedirectAttributes redirectAttributes) {
        try {
            ticketService.createEventFromDTO(eventDto);
            redirectAttributes.addFlashAttribute("successMessage", "Подію успішно створено!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Помилка при створенні події: " + e.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/customer/add")
    public String createCustomer(@ModelAttribute Customer newCustomer, RedirectAttributes redirectAttributes) {
        Customer customer = ticketService.createCustomer(newCustomer);
        redirectAttributes.addFlashAttribute("successMessage", "Покупця " + customer.getName() + " успішно додано.");
        return "redirect:/";
    }

    @GetMapping("/tickets/search")
    public String searchFreeTickets(@RequestParam("searchParam") String eventName, Model model) {
        model.addAttribute("events", ticketService.findUpcomingEvents());
        model.addAttribute("searchParam", eventName);
        model.addAttribute("foundTickets", ticketService.findFreeTicketsByEventName(eventName));
        model.addAttribute("newCustomer", new Customer());
        model.addAttribute("customers", ticketService.findAllCustomers());
        return "events_list";
    }

    @PostMapping("/ticket/assign/{ticketId}")
    public String assignTicket(@PathVariable Long ticketId, @RequestParam Long customerId, RedirectAttributes redirectAttributes) {
        String eventName = null;
        try {
            eventName = ticketService.assignTicketToCustomer(ticketId, customerId);

            redirectAttributes.addFlashAttribute("successMessage", "Квиток успішно присвоєно покупцю.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Помилка присвоєння квитка: " + e.getMessage());
            return "redirect:/";
        }

        if (eventName != null && !eventName.isEmpty()) {
            try {
                String encodedEventName = URLEncoder.encode(eventName, StandardCharsets.UTF_8.toString());
                return "redirect:/tickets/search?searchParam=" + encodedEventName;
            } catch (Exception e) {
                return "redirect:/";
            }
        }

        return "redirect:/";
    }
}