package com.example.patient_medicine_appointment_system.repository;

import com.example.patient_medicine_appointment_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

     Optional<User> findByEmail(String email);
}
