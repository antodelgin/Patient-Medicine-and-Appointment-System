package com.example.patient_medicine_appointment_system;

import com.example.patient_medicine_appointment_system.entity.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class Tests {
    @Test
    void testPatientEntity() {
        // Create a patient
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setAge(30);
        patient.setGender("Male");
        patient.setEmail("john@example.com");
        patient.setPhoneNumber("1234567890");
        patient.setMedicalHistory("No known allergies");

        // Verify getters
        assertEquals(1L, patient.getId());
        assertEquals("John", patient.getFirstName());
        assertEquals("Doe", patient.getLastName());
        assertEquals(30, patient.getAge());
        assertEquals("Male", patient.getGender());
        assertEquals("john@example.com", patient.getEmail());
        assertEquals("1234567890", patient.getPhoneNumber());
        assertEquals("No known allergies", patient.getMedicalHistory());
    }

    @Test
    void testDoctorEntity() {
        // Create a doctor
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setFirstName("Jane");
        doctor.setLastName("Smith");
        doctor.setEmail("jane@hospital.com");
        doctor.setSpecialization("Cardiology");
        doctor.setDegree("MD");
        doctor.setPhoneNumber("9876543210");

        // Verify getters
        assertEquals(1L, doctor.getId());
        assertEquals("Jane", doctor.getFirstName());
        assertEquals("Smith", doctor.getLastName());
        assertEquals("jane@hospital.com", doctor.getEmail());
        assertEquals("Cardiology", doctor.getSpecialization());
        assertEquals("MD", doctor.getDegree());
        assertEquals("9876543210", doctor.getPhoneNumber());
    }

    @Test
    void testMedicationEntity() {
        // Create patient and doctor for relationship
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("John");

        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setFirstName("Jane");

        // Create a medication
        Medication medication = new Medication();
        medication.setId(1L);
        medication.setName("Aspirin");
        medication.setDosage("100mg");
        medication.setFrequency("Daily");
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(10);
        medication.setStartDate(startDate);
        medication.setEndDate(endDate);
        medication.setActive(true);
        medication.setPatient(patient);
        medication.setDoctor(doctor);

        // Verify getters
        assertEquals(1L, medication.getId());
        assertEquals("Aspirin", medication.getName());
        assertEquals("100mg", medication.getDosage());
        assertEquals("Daily", medication.getFrequency());
        assertEquals(startDate, medication.getStartDate());
        assertEquals(endDate, medication.getEndDate());
        assertTrue(medication.isActive());
        assertEquals(patient, medication.getPatient());
        assertEquals(doctor, medication.getDoctor());
    }

    @Test
    void testAppointmentEntity() {
        // Create patient and doctor for relationship
        Patient patient = new Patient();
        patient.setId(1L);

        Doctor doctor = new Doctor();
        doctor.setId(1L);

        // Create appointment
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        LocalDateTime dateTime = LocalDateTime.now().plusDays(3);
        appointment.setAppointmentDateTime(dateTime);
        appointment.setReason("Annual checkup");
        appointment.setStatus(Appointment.AppointmentStatus.SCHEDULED);

        // Verify getters
        assertEquals(1L, appointment.getId());
        assertEquals(patient, appointment.getPatient());
        assertEquals(doctor, appointment.getDoctor());
        assertEquals(dateTime, appointment.getAppointmentDateTime());
        assertEquals("Annual checkup", appointment.getReason());
        assertEquals(Appointment.AppointmentStatus.SCHEDULED, appointment.getStatus());
    }

    @Test
    void testUserEntity() {
        // Create user
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("securePassword");
        user.setRole(User.Role.PATIENT);

        // Verify getters
        assertEquals(1L, user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("securePassword", user.getPassword());
        assertEquals(User.Role.PATIENT, user.getRole());

        // Test UserDetails implementation
        assertEquals("test@example.com", user.getUsername());
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
        assertFalse(user.getAuthorities().isEmpty());
    }

}
