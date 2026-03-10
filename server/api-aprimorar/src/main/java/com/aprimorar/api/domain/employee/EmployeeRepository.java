package com.aprimorar.api.domain.employee;

import java.util.Optional;
import java.util.UUID;

import com.aprimorar.api.domain.employee.entity.Employee;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EmployeeRepository extends PagingAndSortingRepository<Employee, UUID> {
    Optional<Employee> findById(UUID id);
    Employee save(Employee newEmployee);

    Optional<Employee> findByIdAndArchivedAtIsNull(UUID employeeId);
}
