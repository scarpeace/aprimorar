package com.aprimorar.api.domain.employee;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    Optional<Employee> findByIdAndArchivedAtIsNull(UUID employeeId);
}
