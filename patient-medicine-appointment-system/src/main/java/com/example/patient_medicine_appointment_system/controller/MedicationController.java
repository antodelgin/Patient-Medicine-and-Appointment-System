package com.example.patient_medicine_appointment_system.controller;

import com.example.patient_medicine_appointment_system.entity.Doctor;
import com.example.patient_medicine_appointment_system.entity.Medication;
import com.example.patient_medicine_appointment_system.entity.Patient;
import com.example.patient_medicine_appointment_system.service.DoctorService;
import com.example.patient_medicine_appointment_system.service.MedicationService;
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

@Controller
@RequestMapping("medications")
@AllArgsConstructor
@Tag(name = "Medication Management", description = "APIs for managing patient medications")
public class MedicationController {
    private MedicationService medicationService;

    private PatientService patientService;

    private DoctorService doctorService;

    @Operation(summary = "Show form to add medication", description = "Displays a form to add medication for a specific patient.")
    @GetMapping("/add-medication/{patientId}")
    public String showAddMedicationForm(@PathVariable Long patientId, Model model) {
        Medication medication = new Medication();

        Patient patient = patientService.getPatientById(patientId);
        List<Doctor> doctors = doctorService.getAllDoctors();
        medication.setPatient(patient);

        model.addAttribute("medication", medication);
        model.addAttribute("patient", patient);
        model.addAttribute("doctors", doctors);

        return "add-medication";
    }

    @Operation(summary = "Save medication", description = "Saves a new medication entry for a patient.")
    @PostMapping("/save-medication")
    public String addMedication(@Valid @ModelAttribute("medication") Medication medication, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("doctors", doctorService.getAllDoctors());
            model.addAttribute("patient", medication.getPatient());
            return "add-medication";
        }
        Long patientId = medication.getPatient().getId();
        Long doctorId = medication.getDoctor().getId();
        medicationService.addMedicationByPatientId(patientId,medication,doctorId);
        return "redirect:/medications/patient-medication/" + patientId;
    }

    @Operation(summary = "Show form to update medication", description = "Displays a form to update medication details.")
    @GetMapping("/update-medication/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Medication medication = medicationService.getMedicationById(id);

        List<Doctor> doctors = doctorService.getAllDoctors();

        model.addAttribute("medication", medication);
        model.addAttribute("doctors", doctors);
        return "update-medication";
    }


    @Operation(summary = "Update medication details", description = "Updates an existing medication entry.")
    @PostMapping("/update-medication/{id}")
    public String updateMedication(@PathVariable Long id, @Valid @ModelAttribute("medication") Medication medication, BindingResult result) {

        if (result.hasErrors()) {
            System.out.println("Validation errors: " + result.getAllErrors());
            return "update-medication";
        }
        medicationService.updateMedication(id, medication);
        Long patientId = medicationService.getMedicationById(id).getPatient().getId();

        return "redirect:/medications/patient-medication/" + patientId;
    }

    @Operation(summary = "Get patient medications", description = "Fetches all medications assigned to a specific patient.")
    @GetMapping("/patient-medication/{id}")
    public String getPatientMedication(@PathVariable("id") Long patientId, Model model){
        List<Medication> medications = medicationService.getPatientMedications(patientId);

        model.addAttribute("medications", medications);
        model.addAttribute("patientId", patientId);

        if (medications.isEmpty()) {
            Patient patient = patientService.getPatientById(patientId);
            model.addAttribute("patient", patient);
        }
        return "medication-details";
    }
}
