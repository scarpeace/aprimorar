package com.aprimorar.api.service;

import com.aprimorar.api.dto.employee.CreateEmployeeDTO;
import com.aprimorar.api.dto.employee.EmployeeResponseDTO;
import com.aprimorar.api.dto.employee.UpdateEmployeeDTO;
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

    @Transactional(readOnly = true)
    public Page<EmployeeResponseDTO> listEmployees(Pageable pageable) {
        Page<Employee> employeePage =  employeeRepo.findAll(pageable);
        return employeePage.map(employeeMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<EmployeeResponseDTO> listActiveEmployees(Pageable pageable) {
        Page<Employee> activeEmployeesPage = employeeRepo.findAllByActiveTrue(pageable);
        return activeEmployeesPage.map(employeeMapper::toDto);
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDTO findById(UUID employeeId) {
        Employee foundEmployee = findEmployeeOrThrow(employeeId);
        return employeeMapper.toDto(foundEmployee);
    }

    @Transactional
    public EmployeeResponseDTO createEmployee(CreateEmployeeDTO createEmployeeDto) {
        log.info("Creating employee with name: {}", createEmployeeDto.name());
        Employee newEmployee = employeeMapper.toEntity(createEmployeeDto);
        Employee savedEmployee = employeeRepo.save(newEmployee);
        return employeeMapper.toDto(savedEmployee);
    }

    @Transactional
    public void softDeleteEmployee(UUID employeeId) {
        Employee foundEmployee = findEmployeeOrThrow(employeeId);
        deactivateIfActive(foundEmployee);
    }

    @Transactional
    public EmployeeResponseDTO updateEmployee(UUID employeeId, UpdateEmployeeDTO updateEmployeeDto) {
        Employee foundEmployee = findEmployeeOrThrow(employeeId);

        employeeMapper.updateFromDto(updateEmployeeDto, foundEmployee);
        foundEmployee.setUpdatedAt(Instant.now());

        return employeeMapper.toDto(foundEmployee);
    }

    private Employee findEmployeeOrThrow(UUID employeeId) {
        return employeeRepo.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
    }

    private void deactivateIfActive(Employee foundEmployee) {
        if (Boolean.TRUE.equals(foundEmployee.getActive())) {
            foundEmployee.setActive(false);
            foundEmployee.setUpdatedAt(Instant.now());
        }
    }
}
