package com.aprimorar.api.service;

import com.aprimorar.api.dto.employee.CreateEmployeeDTO;
import com.aprimorar.api.dto.employee.EmployeeResponseDTO;
import com.aprimorar.api.entity.Employee;
import com.aprimorar.api.exception.domain.EmployeeNotFoundException;
import com.aprimorar.api.mapper.EmployeeMapper;
import com.aprimorar.api.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepo;
    private final EmployeeMapper employeeMapper;

    public EmployeeService(EmployeeRepository employeeRepo, EmployeeMapper employeeMapper) {
        this.employeeRepo = employeeRepo;
        this.employeeMapper = employeeMapper;
    }

    public Page<EmployeeResponseDTO> listEmployees(Pageable pageable) {
        Page<Employee> employeePage =  employeeRepo.findAll(pageable);
        return employeePage.map(employeeMapper::toDto);
    }

    public Page<EmployeeResponseDTO> listActiveEmployees(Pageable pageable) {
        Page<Employee> activeEmployeesPage = employeeRepo.findAllByActiveTrue(pageable);
        return activeEmployeesPage.map(employeeMapper::toDto);
    }

    public EmployeeResponseDTO findById(UUID employeeId) {
        Employee foundEmployee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        return employeeMapper.toDto(foundEmployee);
    }

    public EmployeeResponseDTO createEmployee(CreateEmployeeDTO createEmployeeDto) {
        log.info("Creating employee with name: {}", createEmployeeDto.name());
        Employee newEmployee = employeeMapper.toEntity(createEmployeeDto);
        Employee savedEmployee = employeeRepo.save(newEmployee);
        return employeeMapper.toDto(savedEmployee);
    }

    @Transactional
    public void softDeleteEmployee(UUID employeeId) {
        Employee foundEmployee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        if (Boolean.TRUE.equals(foundEmployee.getActive())) {
            foundEmployee.setActive(false);
            foundEmployee.setUpdatedAt(Instant.now());
        }
    }

    @Transactional
    public EmployeeResponseDTO updateEmployee(UUID employeeId, CreateEmployeeDTO createEmployeeDto) {
        Employee foundEmployee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        employeeMapper.updateFromDto(createEmployeeDto, foundEmployee);
        foundEmployee.setUpdatedAt(Instant.now());

        return employeeMapper.toDto(foundEmployee);
    }
}
