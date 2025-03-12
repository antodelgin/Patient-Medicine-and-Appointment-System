package com.example.patient_medicine_appointment_system.service;

import com.example.patient_medicine_appointment_system.entity.Doctor;
import com.example.patient_medicine_appointment_system.exception.ResourceNotFoundException;
import com.example.patient_medicine_appointment_system.repository.DoctorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DoctorService {

    private DoctorRepository doctorRepository;

    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: "+ id));
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor updateDoctor(Long id,Doctor doctor) {

        Doctor existingDoctor = doctorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: "+ id));
        existingDoctor.setFirstName(doctor.getFirstName());
        existingDoctor.setLastName(doctor.getLastName());
        existingDoctor.setEmail(doctor.getEmail());
        existingDoctor.setSpecialization(doctor.getSpecialization());
        existingDoctor.setDegree(doctor.getDegree());
        existingDoctor.setPhoneNumber(doctor.getPhoneNumber());
        return doctorRepository.save(doctor);
    }
}
