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
import java.time.Clock;
import java.util.UUID;

@Service
public class EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepo;
    private final EmployeeMapper employeeMapper;
    private final Clock applicationClock;

    public EmployeeService(EmployeeRepository employeeRepo, EmployeeMapper employeeMapper, Clock applicationClock) {
        this.employeeRepo = employeeRepo;
        this.employeeMapper = employeeMapper;
        this.applicationClock = applicationClock;
    }

    @Transactional(readOnly = true)
    public Page<EmployeeResponseDTO> listEmployees(Pageable pageable, boolean includeArchived) {
        Page<Employee> employeePage = includeArchived
                ? employeeRepo.findAll(pageable)
                : employeeRepo.findAllByArchivedAtIsNull(pageable);
        return employeePage.map(employeeMapper::toDto);
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDTO findById(UUID employeeId) {
        Employee foundEmployee = findAnyEmployeeOrThrow(employeeId);
        return employeeMapper.toDto(foundEmployee);
    }

    @Transactional
    public EmployeeResponseDTO createEmployee(CreateEmployeeDTO createEmployeeDto) {
        log.info("Creating employee");
        Employee newEmployee = employeeMapper.toEntity(createEmployeeDto);
        Employee savedEmployee = employeeRepo.save(newEmployee);
        return employeeMapper.toDto(savedEmployee);
    }

    @Transactional
    public void softDeleteEmployee(UUID employeeId) {
        Employee foundEmployee = findAnyEmployeeOrThrow(employeeId);
        log.info("Deactivating employeeId={}", employeeId);
        deactivateIfActive(foundEmployee);
    }

    @Transactional
    public EmployeeResponseDTO updateEmployee(UUID employeeId, UpdateEmployeeDTO updateEmployeeDto) {
        Employee foundEmployee = findAnyEmployeeOrThrow(employeeId);
        log.info("Updating employeeId={}", employeeId);

        employeeMapper.updateFromDto(updateEmployeeDto, foundEmployee);
        foundEmployee.setUpdatedAt(Instant.now(applicationClock));

        return employeeMapper.toDto(foundEmployee);
    }

    private Employee findAnyEmployeeOrThrow(UUID employeeId) {
        return employeeRepo.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
    }

    private void deactivateIfActive(Employee foundEmployee) {
        if (foundEmployee.getArchivedAt() == null) {
            Instant now = Instant.now(applicationClock);
            foundEmployee.setArchivedAt(now);
            foundEmployee.setUpdatedAt(now);
        }
    }
}
