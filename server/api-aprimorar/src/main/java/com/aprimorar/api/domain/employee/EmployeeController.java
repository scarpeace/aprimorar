package com.aprimorar.api.domain.employee;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aprimorar.api.domain.employee.dto.EmployeeRequestDTO;
import com.aprimorar.api.domain.employee.dto.EmployeeResponseDTO;
import com.aprimorar.api.domain.employee.dto.UpdateEmployeeDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/v1/employees")
@Tag(name = "Employees", description = "Employee management APIs")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(summary = "Create employee", description = "Creates a new employee with provided data")
    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@RequestBody @Valid EmployeeRequestDTO employeeRequestDto) {

        EmployeeResponseDTO response = employeeService.createEmployee(employeeRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "List all employees", description = "Retrieves all employees from database with pagination")
    @GetMapping
    public ResponseEntity<Page<EmployeeResponseDTO>> getEmployees(@PageableDefault(page = 0, size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<EmployeeResponseDTO> employees = employeeService.getEmployees(pageable);
        return ResponseEntity.ok(employees);
    }

    @Operation(summary = "Get employee by ID", description = "Retrieves a single employee based on ID")
    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable UUID employeeId) {

        EmployeeResponseDTO foundEmployee = employeeService.findById(employeeId);
        return ResponseEntity.ok(foundEmployee);
    }

    @Operation(summary = "Update employee", description = "Partially updates an existing employee with provided data")
    @PatchMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(@PathVariable UUID employeeId, @RequestBody @Valid UpdateEmployeeDTO updateEmployeeDTO) {

        EmployeeResponseDTO updatedEmployee = employeeService.updateEmployee(employeeId, updateEmployeeDTO);
        return ResponseEntity.ok(updatedEmployee);
    }

    @Operation(summary = "Delete employee", description = "Deletes an employee based on ID")
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID employeeId) {

        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Archive employee", description = "Archives an employee based on ID")
    @PatchMapping("/{employeeId}/archive")
    public ResponseEntity<Void> archiveEmployee(@PathVariable UUID employeeId) {

        employeeService.archiveEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Unarchive employee", description = "Unarchives an employee based on ID")
    @PatchMapping("/{employeeId}/unarchive")
    public ResponseEntity<Void> unarchiveEmployee(@PathVariable UUID employeeId) {

        employeeService.unarchiveEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }

}
