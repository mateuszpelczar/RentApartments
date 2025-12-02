package com.example.RentApartments.config;

import com.example.RentApartments.model.Rola;
import com.example.RentApartments.model.User;
import com.example.RentApartments.model.UserRole;
import com.example.RentApartments.repository.jpa.RolaRepository;
import com.example.RentApartments.repository.jpa.UserRepository;
import com.example.RentApartments.repository.jpa.UserRoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final RolaRepository rolaRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.admin.firstName}")
    private String adminUsername;

    @Value("${app.admin.lastName}")
    private String adminSurname;

    public DataInitializer(UserRepository userRepository,
                          RolaRepository rolaRepository,
                          UserRoleRepository userRoleRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.rolaRepository = rolaRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initializeRoles();
        initializeAdminUser();
    }

    private void initializeRoles() {
        String[] roleNames = {"USER", "ADMIN"};

        for (String roleName : roleNames) {
            Optional<Rola> existingRole = rolaRepository.findByRolename(roleName);
            if (existingRole.isEmpty()) {
                Rola rola = new Rola();
                rola.setRolename(roleName);
                rolaRepository.save(rola);
            }
        }
    }

    private void initializeAdminUser() {
        Optional<User> existingAdmin = userRepository.findByEmail(adminEmail);

        if (existingAdmin.isPresent()) {
            return;
        }

        User admin = new User();
        admin.setUsername(adminUsername);
        admin.setSurname(adminSurname);
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode(adminPassword));

        User savedAdmin = userRepository.save(admin);

        Rola userRole = rolaRepository.findByRolename("USER")
                .orElseThrow(() -> new RuntimeException("Rola USER nie zostala znaleziona"));
        Rola adminRole = rolaRepository.findByRolename("ADMIN")
                .orElseThrow(() -> new RuntimeException("Rola ADMIN nie zostala znaleziona"));

        UserRole userRoleAssignment = new UserRole();
        userRoleAssignment.setUser(savedAdmin);
        userRoleAssignment.setRole(userRole);
        userRoleRepository.save(userRoleAssignment);

        UserRole adminRoleAssignment = new UserRole();
        adminRoleAssignment.setUser(savedAdmin);
        adminRoleAssignment.setRole(adminRole);
        userRoleRepository.save(adminRoleAssignment);
    }
}
