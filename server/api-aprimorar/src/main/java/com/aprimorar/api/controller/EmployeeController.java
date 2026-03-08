package com.aprimorar.api.controller;

import com.aprimorar.api.dto.common.PageQuery;
import com.aprimorar.api.dto.employee.CreateEmployeeDTO;
import com.aprimorar.api.dto.employee.EmployeeResponseDTO;
import com.aprimorar.api.dto.employee.UpdateEmployeeDTO;
import com.aprimorar.api.service.EmployeeService;
import com.aprimorar.api.util.PageableUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/v1/employees")
@Tag(name = "Employees", description = "Employee management APIs")
public class EmployeeController {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("name", "createdAt", "updatedAt");

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(summary = "List all employees", description = "Retrieves all employees from database with pagination")
    @GetMapping
    public ResponseEntity<Page<EmployeeResponseDTO>> listEmployees(
            @Valid @ModelAttribute PageQuery pageQuery
    )
    {
        Pageable pageable = PageableUtils.buildPageable(pageQuery, "name", ALLOWED_SORT_FIELDS);
        Page<EmployeeResponseDTO> allEmployees = employeeService.listEmployees(pageable);
        return ResponseEntity.ok(allEmployees);
    }

    @Operation(summary = "List all active employees", description = "Retrieves all ACTIVE employees from database with pagination")
    @GetMapping("/active")
    public ResponseEntity<Page<EmployeeResponseDTO>> listActiveEmployees(
            @Valid @ModelAttribute PageQuery pageQuery
    )
    {
        Pageable pageable = PageableUtils.buildPageable(pageQuery, "name", ALLOWED_SORT_FIELDS);
        Page<EmployeeResponseDTO> activeEmployees = employeeService.listActiveEmployees(pageable);
        return ResponseEntity.ok(activeEmployees);
    }

    @Operation(summary = "Get employee by ID", description = "Retrieves a single employee based on ID")
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

    @Operation(summary = "Update employee", description = "Partially updates an existing employee with provided data")
    @PatchMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(
            @PathVariable UUID employeeId,
            @RequestBody @Valid UpdateEmployeeDTO updateEmployeeDto) {
        EmployeeResponseDTO updatedEmployee = employeeService.updateEmployee(employeeId, updateEmployeeDto);
        return ResponseEntity.ok(updatedEmployee);
    }

    @Operation(summary = "Delete employee", description = "Soft deletes an employee based on ID")
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID employeeId) {
        employeeService.softDeleteEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }

}
