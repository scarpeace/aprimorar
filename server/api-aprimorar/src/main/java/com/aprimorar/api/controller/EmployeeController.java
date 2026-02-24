package com.aprimorar.api.controller;

import com.aprimorar.api.dto.employee.EmployeeResponseDTO;
import com.aprimorar.api.dto.event.EventResponseDTO;
import com.aprimorar.api.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/employees")
@Tag(name = "Employees", description = "Employee management APIs")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(summary = "List all employees", description = "Retrieves all employees from database with pagination")
    @GetMapping
    public ResponseEntity<Page<EmployeeResponseDTO>> listEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy
    )
    {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<EmployeeResponseDTO> allEmployees = employeeService.listEmployees(pageable);
        return ResponseEntity.ok(allEmployees);
    }
}
