package com.aprimorar.api.domain.employee;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aprimorar.api.domain.employee.dto.EmployeeRequestDTO;
import com.aprimorar.api.domain.employee.dto.EmployeeResponseDTO;
import com.aprimorar.api.domain.employee.dto.UpdateEmployeeDTO;
import com.aprimorar.api.shared.MapperUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/employees")
@Tag(name = "Employees", description = "Employee management APIs")
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(summary = "Create employee", description = "Creates a new employee with provided data")
    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@RequestBody @Valid EmployeeRequestDTO employeeRequestDto) {

        log.info("EmployeeController::createEmployee request body {}", MapperUtils.jsonAsString(employeeRequestDto));
        EmployeeResponseDTO response = employeeService.createEmployee(employeeRequestDto);

        log.info("EmployeeController::createEmployee response body {}", MapperUtils.jsonAsString(response));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "List all employees", description = "Retrieves all employees from database with pagination")
    @GetMapping
    public ResponseEntity<Page<EmployeeResponseDTO>> listEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<EmployeeResponseDTO> employees = employeeService.getEmployees(pageRequest);

        log.info("EmployeeController:listEmployees execution completed. Page: {}, Size: {}, Total Elements: {}", page, size, employees.getTotalElements());
        return ResponseEntity.ok(employees);
    }

    @Operation(summary = "Get employee by ID", description = "Retrieves a single employee based on ID")
    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable UUID employeeId) {
        log.info("EmployeeController::getEmployeeById by id  {}", employeeId);

        EmployeeResponseDTO foundEmployee = employeeService.getById(employeeId);

        log.info("EmployeeController::getEmployeeById by id  {} response {}", employeeId, MapperUtils.jsonAsString(foundEmployee));
        return ResponseEntity.ok(foundEmployee);
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
