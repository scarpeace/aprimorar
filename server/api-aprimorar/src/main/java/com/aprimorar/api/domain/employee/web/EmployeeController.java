package com.aprimorar.api.domain.employee.web;

import java.util.List;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aprimorar.api.domain.employee.EmployeeService;
import com.aprimorar.api.domain.employee.dto.EmployeeOptionDTO;
import com.aprimorar.api.domain.employee.dto.EmployeeRequestDTO;
import com.aprimorar.api.domain.employee.dto.EmployeeResponseDTO;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/employees")
public class EmployeeController implements EmployeeControllerDocs {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@RequestBody @Valid EmployeeRequestDTO employeeRequestDto) {

        EmployeeResponseDTO response = employeeService.createEmployee(employeeRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<EmployeeResponseDTO>> getEmployees(
            @ParameterObject @PageableDefault(page = 0, size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) String search) {

        Page<EmployeeResponseDTO> employees = employeeService.getEmployees(pageable, search);
        return ResponseEntity.ok(employees);
    }

    @Override
    @GetMapping("/options")
    public ResponseEntity<List<EmployeeOptionDTO>> getEmployeeOptions() {
        List<EmployeeOptionDTO> options = employeeService.getEmployeeOptions();
        return ResponseEntity.ok(options);
    }

    @Override
    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable UUID employeeId) {

        EmployeeResponseDTO foundEmployee = employeeService.findById(employeeId);
        return ResponseEntity.ok(foundEmployee);
    }

    @Override
    @PatchMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(@PathVariable UUID employeeId, @RequestBody @Valid EmployeeRequestDTO request) {

        EmployeeResponseDTO updatedEmployee = employeeService.updateEmployee(employeeId, request);
        return ResponseEntity.ok(updatedEmployee);
    }

    @Override
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID employeeId) {

        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/{employeeId}/archive")
    public ResponseEntity<Void> archiveEmployee(@PathVariable UUID employeeId) {

        employeeService.archiveEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/{employeeId}/unarchive")
    public ResponseEntity<Void> unarchiveEmployee(@PathVariable UUID employeeId) {

        employeeService.unarchiveEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }

}
