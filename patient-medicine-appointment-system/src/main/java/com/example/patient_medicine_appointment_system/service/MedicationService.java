package com.example.patient_medicine_appointment_system.service;

import com.example.patient_medicine_appointment_system.entity.Doctor;
import com.example.patient_medicine_appointment_system.entity.Medication;
import com.example.patient_medicine_appointment_system.entity.Patient;
import com.example.patient_medicine_appointment_system.exception.ResourceNotFoundException;
import com.example.patient_medicine_appointment_system.repository.DoctorRepository;
import com.example.patient_medicine_appointment_system.repository.MedicationRepository;
import com.example.patient_medicine_appointment_system.repository.PatientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MedicationService {

    private MedicationRepository medicationRepository;

    private PatientRepository patientRepository;

    private DoctorRepository doctorRepository;

    public Medication addMedicationByPatientId(Long patientId, Medication medication, Long doctorId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + patientId));

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + doctorId));

        medication.setPatient(patient);
        medication.setDoctor(doctor);

        if (medication.isActive() == false) {
            medication.setActive(true);
        }
        return medicationRepository.save(medication);
    }

    public List<Medication> getPatientMedications(Long patientId) {

        List<Medication> medications =  medicationRepository.findByPatientId(patientId);
        return medications;
    }


    public Medication getMedicationById(Long id) {
        return medicationRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Medication not found with ID: "+ id));
    }

    public Medication updateMedication(Long id,Medication medication) {
        Medication existingMedication = medicationRepository.findById(id).orElseThrow(() -> new RuntimeException("Medication not found with ID: " + id));
        if (medication == null) {
            throw new IllegalArgumentException("Medication data is null");
        }

        existingMedication.setName(medication.getName());
        existingMedication.setDosage(medication.getDosage());
        existingMedication.setFrequency(medication.getFrequency());
        existingMedication.setStartDate(medication.getStartDate());
        existingMedication.setEndDate(medication.getEndDate());
        existingMedication.setActive(medication.isActive());

        return medicationRepository.save(existingMedication);
    }
}
