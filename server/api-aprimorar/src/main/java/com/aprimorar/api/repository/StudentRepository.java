package com.aprimorar.api.repository;

import com.aprimorar.api.entity.Student;
import com.aprimorar.api.enums.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID>{

    Page<Student> findAllByActiveTrue(Pageable pageable);

    Page<Student> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Student> findByActivity(Activity activity, Pageable pageable);

    Page<Student> findByNameContainingIgnoreCaseAndActivity(String name, Activity activity, Pageable pageable);

    Page<Student> findAllByActiveTrueAndNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Student> findAllByActiveTrueAndActivity(Activity activity, Pageable pageable);

    Page<Student> findAllByActiveTrueAndNameContainingIgnoreCaseAndActivity(
            String name,
            Activity activity,
            Pageable pageable
    );

    Optional<Student> findByIdAndActiveTrue(UUID id);
}
