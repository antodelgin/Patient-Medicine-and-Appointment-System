package com.example.patient_medicine_appointment_system.controller;

import com.example.patient_medicine_appointment_system.entity.Appointment;
import com.example.patient_medicine_appointment_system.service.AppointmentService;
import com.example.patient_medicine_appointment_system.service.DoctorService;
import com.example.patient_medicine_appointment_system.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Tag(name = "Appointments", description = "APIs for managing appointments")
@Controller
@RequestMapping("/appointments")
@AllArgsConstructor
public class AppointmentController {


    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final PatientService patientService;

    @Operation(summary = "Search for available appointments", description = "Displays a form where users can select a doctor and a patient to search for available appointment slots.")
    @GetMapping("/search")
    public String showSearchForm(Model model) {
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("patients", patientService.getAllPatients());
        return "search-appointment";
    }

    @Operation(summary = "Get available appointment slots for a doctor",description = "Retrieves a list of available appointment slots for a selected doctor on a specific date.")
    @GetMapping("/available-slots")
    public String getAvailableSlots(@RequestParam Long doctorId,
                                    @RequestParam Long patientId,
                                    @RequestParam("date") String date,
                                    Model model) {
        LocalDate selectedDate = LocalDate.parse(date);
        List<LocalDateTime> availableSlots = appointmentService.getAvailableSlots(doctorId, selectedDate);

        model.addAttribute("availableSlots", availableSlots);
        model.addAttribute("doctorId", doctorId);
        model.addAttribute("patientId", patientId);
        model.addAttribute("selectedDate", selectedDate);

        model.addAttribute("doctor", doctorService.getDoctorById(doctorId));
        model.addAttribute("patient", patientService.getPatientById(patientId));

        return "available-slots";
    }

    @Operation(summary = "Book an appointment",
            description = "Schedules an appointment for a patient with a selected doctor at a given date and time. ")
    @PostMapping("/book")
    public String bookAppointment(@RequestParam Long patientId,
                                  @RequestParam Long doctorId,
                                  @RequestParam String dateTime,
                                  @RequestParam String reason,
                                  Model model) {

        LocalDateTime appointmentTime = LocalDateTime.parse(dateTime);
        Appointment appointment = appointmentService.bookAppointment(patientId, doctorId, appointmentTime, reason);

        model.addAttribute("appointment", appointment);
        model.addAttribute("doctorId", doctorId);
        model.addAttribute("patientId", patientId);
        model.addAttribute("appointmentTime", appointmentTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
        model.addAttribute("doctor", doctorService.getDoctorById(doctorId));
        model.addAttribute("patient", patientService.getPatientById(patientId));

        return "appointment-success";
    }

    @Operation(summary = "View all appointments", description = "Retrieves a list of all appointments in the system.")
    @GetMapping("/all-appointments")
    public String viewAppointments(Model model) {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        model.addAttribute("appointments", appointments);
        return "appointments-list"; // Thymeleaf template
    }

    @Operation(summary = "Complete an appointment", description = "Marks an appointment as completed.")
    @PostMapping("/complete/{appointmentId}")
    public String completeAppointment(@PathVariable Long appointmentId) {
        Appointment appointment = appointmentService.completeAppointment(appointmentId);
        Long doctorId = appointment.getDoctor().getId();

        return "redirect:/appointments/doctor-appointments/" + doctorId ;
    }

    @Operation(summary = "Cancel an appointment", description = "Cancels an existing appointment.")
    @PostMapping("/cancel/{appointmentId}")
    public String cancelAppointment(@PathVariable Long appointmentId) {
        Appointment appointment = appointmentService.cancelAppointment(appointmentId);
        Long doctorId = appointment.getDoctor().getId();

        return "redirect:/appointments/doctor-appointments/" + doctorId;
    }

    @Operation(summary = "View a doctor's appointments", description = "Retrieves all appointments for a specific doctor.")
    @GetMapping("/doctor-appointments/{doctorId}")
    public String viewDoctorAppointments(@PathVariable Long doctorId, Model model) {
        List<Appointment> appointments = appointmentService.getAppointmentsByDoctorId(doctorId);
        model.addAttribute("appointments", appointments);
        model.addAttribute("doctorId", doctorId);
        return "appointments-list";
    }
}

