package com.example.patient_medicine_appointment_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Home", description = "Controller for handling the home page.")
@Controller
@AllArgsConstructor
public class HomeController {

    @Operation(
            summary = "Show the home page",
            description = "Displays the main landing page of the application."
    )
    @GetMapping("/")
    public String showHomePage() {
        return "index";
    }
}
