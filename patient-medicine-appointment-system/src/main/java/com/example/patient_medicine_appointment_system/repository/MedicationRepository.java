package com.example.patient_medicine_appointment_system.repository;

import com.example.patient_medicine_appointment_system.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
    @Query("SELECT m FROM Medication m JOIN FETCH m.doctor JOIN FETCH m.doctor JOIN FETCH m.patient WHERE m.patient.id = :patientId")
    List<Medication> findByPatientId(@Param("patientId") Long patientId);
}
