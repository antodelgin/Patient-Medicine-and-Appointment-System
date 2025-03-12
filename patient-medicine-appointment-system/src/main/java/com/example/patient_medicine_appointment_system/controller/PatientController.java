package com.example.patient_medicine_appointment_system.controller;

import com.example.patient_medicine_appointment_system.entity.Patient;
import com.example.patient_medicine_appointment_system.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Patient Management", description = "Endpoints for managing patient records, including registration, updates, and retrieval.")
@Controller
@RequestMapping("/patients")
@AllArgsConstructor
public class PatientController {

    private PatientService patientService;

    @Operation(
            summary = "Show registration form for a new patient",
            description = "Displays a form where users can enter patient details to register a new patient."
    )
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        Patient patient = new Patient();
        model.addAttribute("patient", patient);
        return "add-patient";
    }

    @Operation(
            summary = "Save a new patient",
            description = "Registers a new patient in the system. Requires patient details as input."
    )
    @PostMapping("/save-patient")
    public String savePatient(@Valid @ModelAttribute("patient") Patient patient, BindingResult result) {
        if(result.hasErrors()){
                return "add-patient";
        }

        patientService.createPatient(patient);
        return "redirect:/patients/patients-list";
    }

    @Operation(
            summary = "Get patient details by ID",
            description = "Retrieves details of a specific patient using their unique ID."
    )
    @GetMapping("/patient/{id}")
    public String getPatientById(@PathVariable Long id, Model model) {
        Patient patient = patientService.getPatientById(id);
        model.addAttribute("patient", patient);
        return "patient-details";
    }

    @Operation(
            summary = "Get list of all patients",
            description = "Returns a list of all registered patients in the system."
    )
    @GetMapping("/patients-list")
    public String getAllPatients(Model model) {
        List<Patient> patients = patientService.getAllPatients();
        model.addAttribute("patients", patients);
        return "patients-list";
    }

    @Operation(
            summary = "Show update form for a patient",
            description = "Displays a form allowing users to update details of an existing patient."
    )
    @GetMapping("/update-patient/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Patient patient = patientService.getPatientById(id);
        model.addAttribute("patient",patient);
        return "update-patient";
    }


    @Operation(
            summary = "Update patient details",
            description = "Updates the details of an existing patient in the system."
    )
    @PostMapping("/update-patient/{id}")
    public String updatePatient(@PathVariable Long id, @Valid @ModelAttribute("patient") Patient patient, BindingResult result) {

        if (result.hasErrors()) {
            System.out.println("Validation errors: " + result.getAllErrors());
            return "update-patient";

        }
    patientService.updatePatient(id, patient);
    return "redirect:/patients/patients-list";
    }
}