package com.example.patient_medicine_appointment_system.repository;

import com.example.patient_medicine_appointment_system.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
