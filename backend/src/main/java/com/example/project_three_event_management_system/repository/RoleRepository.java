package com.example.project_three_event_management_system.repository;

import com.example.project_three_event_management_system.entity.Role;
import com.example.project_three_event_management_system.entity.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(ERole name);
}
