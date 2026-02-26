package com.aprimorar.api.repository;

import com.aprimorar.api.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    Optional<Employee> findByIdAndActiveTrue(UUID id);

    Page<Employee> findAllByActiveTrue(Pageable pageable);
}

