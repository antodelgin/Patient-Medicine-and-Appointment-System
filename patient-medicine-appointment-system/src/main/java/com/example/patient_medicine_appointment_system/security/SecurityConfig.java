package com.example.patient_medicine_appointment_system.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/patients/patients-list").hasAnyRole("PATIENT","DOCTOR")
                        .requestMatchers("/patients/patient/**").hasAnyRole("PATIENT","DOCTOR","ADMIN")
                        .requestMatchers("/doctors/**").hasRole("DOCTOR")

                        .requestMatchers("/medications/patient-medication/**").hasAnyRole("PATIENT", "DOCTOR", "ADMIN") // View medications
                        .requestMatchers("/medications/add-medication/**").hasAnyRole("DOCTOR", "ADMIN") // Add medications
                        .requestMatchers("/medications/update-medication/**").hasAnyRole("DOCTOR", "ADMIN") // Update medications
                        .requestMatchers("/medications/save-medication").hasAnyRole("DOCTOR", "ADMIN")
                        .requestMatchers("/favicon.ico", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/patients/**").hasRole("PATIENT")
                        .requestMatchers("/appointments/book").hasRole("PATIENT")
                        .requestMatchers("/appointments/all-appointments").hasAnyRole("DOCTOR", "ADMIN")
                        .requestMatchers("/appointments/complete/**", "/appointments/cancel/**").hasRole("DOCTOR")
                        .requestMatchers("/appointments/doctor-appointments/**").hasRole("DOCTOR")
                        .requestMatchers("/register", "/login").permitAll()
                        .requestMatchers("/").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()

                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationConfiguration authenticationConfiguration() {
        return new AuthenticationConfiguration();
    }
}
