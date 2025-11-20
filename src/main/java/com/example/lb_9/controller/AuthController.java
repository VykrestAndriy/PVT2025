package com.example.lb_9.controller;

import com.example.lb_9.dto.AuthRegistrationDTO;
import com.example.lb_9.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // Передача порожнього DTO для заповнення Thymeleaf
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new AuthRegistrationDTO());
        }
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") AuthRegistrationDTO registrationDto,
                               RedirectAttributes redirectAttributes) {
        try {
            userService.save(registrationDto);
            redirectAttributes.addFlashAttribute("successMessage", "Реєстрація пройшла успішно! Будь ласка, увійдіть.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Помилка реєстрації: " + e.getMessage());
            redirectAttributes.addFlashAttribute("user", registrationDto); // Зберігаємо введені дані
            return "redirect:/register";
        }
    }
}