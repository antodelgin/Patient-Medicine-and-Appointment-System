package com.example.patient_medicine_appointment_system.service;

import com.example.patient_medicine_appointment_system.entity.Patient;
import com.example.patient_medicine_appointment_system.exception.ResourceNotFoundException;
import com.example.patient_medicine_appointment_system.repository.PatientRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PatientService {

    private PatientRepository patientRepository;
    private PasswordEncoder passwordEncoder;

    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }


    public Patient updatePatient(Long id, Patient patient) {
        Patient existingPatient = patientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        existingPatient.setFirstName(patient.getFirstName());
        existingPatient.setLastName(patient.getLastName());
        existingPatient.setAge(patient.getAge());
        existingPatient.setGender(patient.getGender());
        existingPatient.setEmail(patient.getEmail());
        existingPatient.setPhoneNumber(patient.getPhoneNumber());
        existingPatient.setMedicalHistory(patient.getMedicalHistory());

        return patientRepository.save(existingPatient);
    }

}
