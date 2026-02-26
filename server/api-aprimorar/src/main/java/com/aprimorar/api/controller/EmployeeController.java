package com.aprimorar.api.controller;

import com.aprimorar.api.dto.employee.CreateEmployeeDTO;
import com.aprimorar.api.dto.employee.EmployeeResponseDTO;
import com.aprimorar.api.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("v1/employees")
@Tag(name = "Employees", description = "Employee management APIs")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(summary = "List all employees", description = "Retrieves all employees from database with pagination")
    @Transactional(readOnly = true)
    @GetMapping
    public ResponseEntity<Page<EmployeeResponseDTO>> listEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") @Max(100) int size,
            @RequestParam(defaultValue = "name") String sortBy
    )
    {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<EmployeeResponseDTO> allEmployees = employeeService.listEmployees(pageable);
        return ResponseEntity.ok(allEmployees);
    }

    @Operation(summary = "List all active employees", description = "Retrieves all ACTIVE employees from database with pagination")
    @Transactional(readOnly = true)
    @GetMapping("/active")
    public ResponseEntity<Page<EmployeeResponseDTO>> listActiveEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") @Max(100) int size,
            @RequestParam(defaultValue = "name") String sortBy
    )
    {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<EmployeeResponseDTO> activeEmployees = employeeService.listActiveEmployees(pageable);
        return ResponseEntity.ok(activeEmployees);
    }

    @Operation(summary = "Get employee by ID", description = "Retrieves a single employee based on ID")
    @Transactional(readOnly = true)
    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable UUID employeeId) {
        EmployeeResponseDTO foundEmployee = employeeService.findById(employeeId);
        return ResponseEntity.ok(foundEmployee);
    }

    @Operation(summary = "Create employee", description = "Creates a new employee with provided data")
    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@RequestBody @Valid CreateEmployeeDTO createEmployeeDto) {
        EmployeeResponseDTO response = employeeService.createEmployee(createEmployeeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update employee", description = "Updates an existing employee with provided data")
    @PatchMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(
            @PathVariable UUID employeeId,
            @RequestBody @Valid CreateEmployeeDTO createEmployeeDto) {
        EmployeeResponseDTO updatedEmployee = employeeService.updateEmployee(employeeId, createEmployeeDto);
        return ResponseEntity.ok(updatedEmployee);
    }

    @Operation(summary = "Delete employee", description = "Soft deletes an employee based on ID")
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID employeeId) {
        employeeService.softDeleteEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }
}
