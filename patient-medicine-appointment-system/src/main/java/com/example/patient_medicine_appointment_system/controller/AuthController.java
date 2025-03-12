package com.example.patient_medicine_appointment_system.controller;

import com.example.patient_medicine_appointment_system.entity.User;
import com.example.patient_medicine_appointment_system.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
@Tag(name = "Authentication", description = "Handles user registration and login")
public class AuthController {

    private AuthService authService;
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "Show registration form", description = "Displays the user registration form.")
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @Operation(summary = "Register a new user", description = "Processes user registration and saves it to the database.")
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            authService.registerUser(user);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @Operation(summary = "Show login page", description = "Displays the login form for users to authenticate.")
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
}
