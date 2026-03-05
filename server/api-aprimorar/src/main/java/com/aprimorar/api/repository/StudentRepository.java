package com.aprimorar.api.repository;

import com.aprimorar.api.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID>{

    Page<Student> findByActive(Boolean active, Pageable pageable);

    Page<Student> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Student> findByActiveAndNameContainingIgnoreCase(Boolean active, String name, Pageable pageable);

    Optional<Student> findByIdAndActiveTrue(UUID id);
}
