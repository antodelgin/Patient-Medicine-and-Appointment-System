package com.example.patient_medicine_appointment_system.service;

import com.example.patient_medicine_appointment_system.entity.Appointment;
import com.example.patient_medicine_appointment_system.entity.Doctor;
import com.example.patient_medicine_appointment_system.entity.Patient;
import com.example.patient_medicine_appointment_system.exception.ResourceNotFoundException;
import com.example.patient_medicine_appointment_system.repository.AppointmentRepository;
import com.example.patient_medicine_appointment_system.repository.DoctorRepository;
import com.example.patient_medicine_appointment_system.repository.PatientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    private static final int APPOINTMENT_DURATION = 30; // 30 minutes per appointment
    private static final LocalTime START_TIME = LocalTime.of(9, 0);  // 9:00 AM
    private static final LocalTime END_TIME = LocalTime.of(17, 0);   // 5:00 PM

    public List<LocalDateTime> getAvailableSlots(Long doctorId, LocalDate date) {
        List<LocalDateTime> availableSlots = new ArrayList<>();

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        List<Appointment> bookedAppointments = appointmentRepository.findByDoctorIdAndAppointmentDateTimeBetween(doctorId, startOfDay, endOfDay);

        LocalDateTime slotTime = date.atTime(START_TIME);

        while (slotTime.toLocalTime().isBefore(END_TIME)) {
            final LocalDateTime currentSlot = slotTime;

            boolean isBooked = bookedAppointments.stream()
                    .anyMatch(a -> a.getAppointmentDateTime().isEqual(currentSlot));

            if (!isBooked) {
                availableSlots.add(slotTime);
            }
            slotTime = slotTime.plusMinutes(APPOINTMENT_DURATION);
        }
        return availableSlots;
    }

    public Appointment bookAppointment(Long patientId, Long doctorId, LocalDateTime slot, String reason) {
        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);

        if (patientOpt.isEmpty()) {
            throw new ResourceNotFoundException("Patient not found with ID: "+ patientId);
        }
        if (doctorOpt.isEmpty()) {
            throw new ResourceNotFoundException("Doctor not found with ID: " + doctorId);
        }

        boolean slotAlreadyBooked = appointmentRepository.existsByDoctorIdAndAppointmentDateTime(doctorId, slot);
        if (slotAlreadyBooked) {
            throw new RuntimeException("This time slot is already booked.");
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patientOpt.get());
        appointment.setDoctor(doctorOpt.get());
        appointment.setAppointmentDateTime(slot);
        appointment.setReason(reason);
        appointment.setStatus(Appointment.AppointmentStatus.SCHEDULED);

        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment completeAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + appointmentId));


        appointment.setStatus(Appointment.AppointmentStatus.COMPLETED);
        return appointmentRepository.save(appointment);
    }


    public Appointment cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: "+ appointmentId));

        appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
        return appointmentRepository.save(appointment);
    }


    public List<Appointment> getAppointmentsByDoctorId(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }
}

