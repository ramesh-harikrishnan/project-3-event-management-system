package com.example.project_three_event_management_system.config;

import com.example.project_three_event_management_system.entity.ERole;
import com.example.project_three_event_management_system.entity.Role;
import com.example.project_three_event_management_system.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

// Example Data Initializer Class
@Component
public class DataInitializer {

    private final RoleRepository roleRepository;

    @Autowired
    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Bean
    public CommandLineRunner initializeRoles() {
        return args -> {
            // Ensure ROLE_USER exists
            if (roleRepository.findByName(ERole.ROLE_USER).isEmpty()) {
                roleRepository.save(new Role(ERole.ROLE_USER));
            }
            // Ensure ROLE_ADMIN exists
            if (roleRepository.findByName(ERole.ROLE_ADMIN).isEmpty()) {
                roleRepository.save(new Role(ERole.ROLE_ADMIN));
            }
        };
    }
}
