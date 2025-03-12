package com.example.patient_medicine_appointment_system.controller;

import com.example.patient_medicine_appointment_system.entity.Doctor;
import com.example.patient_medicine_appointment_system.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/doctors")
@AllArgsConstructor
@Tag(name = "Doctor Management", description = "APIs for managing doctors")
public class DoctorController {

    private DoctorService doctorService;

    @Operation(summary = "Show the doctor registration form", description = "Displays a form to register a new doctor.")
    @GetMapping("/register")
    public String showDoctorForm(Model model) {
        Doctor doctor = new Doctor();
        model.addAttribute("doctor", doctor);
        return "add-doctor";
    }

    @Operation(summary = "Register a new doctor", description = "Creates a new doctor entry after validating the provided details. Only users with the 'DOCTOR' role can register.")
    @PostMapping("/save-doctor")
    public String registerDoctor(@Valid @ModelAttribute("doctor") Doctor doctor, BindingResult result) {
        if(result.hasErrors()){
            return "add-doctor";
        }
        doctorService.createDoctor(doctor);
        return "redirect:/doctors/doctors-list";
    }

    @Operation(summary = "Get doctor details by ID", description = "Fetches the details of a specific doctor using their ID.")
    @GetMapping("/doctor/{id}")
    public String getDoctorById(@PathVariable Long id, Model model) {
        Doctor doctor = doctorService.getDoctorById(id);
        model.addAttribute("doctor", doctor);
        return "doctor-details";
    }

    @Operation(summary = "Get the list of all doctors", description = "Retrieves a list of all registered doctors.")
    @GetMapping("/doctors-list")
    public String getAllDoctors(Model model) {
        List<Doctor> doctors = doctorService.getAllDoctors();
        model.addAttribute("doctors", doctors);
        return "doctors-list";
    }

    @Operation(summary = "Show the update form for a doctor", description = "Displays a form to update an existing doctor's details.")
    @GetMapping("/update-doctor/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Doctor doctor = doctorService.getDoctorById(id);
        System.out.println("Doctor Data: " + doctor);

        model.addAttribute("doctor", doctor);
        return "update-doctor";
    }

    @Operation(summary = "Update doctor details", description = "Updates an existing doctor's details based on the provided ID and form data.")
    @PostMapping("/update-doctor/{id}")
    public String updateDoctor(@PathVariable Long id, @Valid @ModelAttribute("doctor") Doctor doctor, BindingResult result) {

        if (result.hasErrors()) {
            System.out.println("Validation errors: " + result.getAllErrors());
            return "update-doctor";
        }
        doctorService.updateDoctor(id, doctor);
        return "redirect:/doctors/doctors-list";
    }
}
