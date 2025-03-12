package com.example.patient_medicine_appointment_system;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Patient Medicine and Appointment System API",
				description = "This API provides functionalities for managing doctor, patient appointments and medications. It includes endpoints for booking, updating, and canceling appointments, as well as handling patient and doctor records.",
				version = "1.0",
				contact = @Contact(
						name = "Anto Delgin Anston",
						email = "antodelgin@gmail.com",
						url = "https://www.google.com"
				)
		)
)
public class PatientMedicineAppointmentSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(PatientMedicineAppointmentSystemApplication.class, args);
	}
}
