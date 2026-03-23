package com.aprimorar.api.domain.employee;

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
import org.springframework.web.bind.annotation.RestController;

import com.aprimorar.api.annotations.ErrorResponsesAnnotation;
import com.aprimorar.api.domain.employee.dto.EmployeeOptionDTO;
import com.aprimorar.api.domain.employee.dto.EmployeeRequestDTO;
import com.aprimorar.api.domain.employee.dto.EmployeeResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/employees")
@Tag(name = "Employee", description = "Employee management APIs")
@ErrorResponsesAnnotation
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(
            operationId = "createEmployee",
            summary = "Criar funcionário",
            description = "Cria um novo funcionário com os dados fornecidos."
    )
    @ApiResponse(responseCode = "201")
    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@RequestBody @Valid EmployeeRequestDTO employeeRequestDto) {

        EmployeeResponseDTO response = employeeService.createEmployee(employeeRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            operationId = "getEmployees",
            summary = "Listar funcionários",
            description = "Retorna todos os funcionários do banco de dados com paginação."
    )
    @ApiResponse(responseCode = "200")
    @GetMapping
    public ResponseEntity<Page<EmployeeResponseDTO>> getEmployees(
            @ParameterObject @PageableDefault(page = 0, size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String search) {

        Page<EmployeeResponseDTO> employees = employeeService.getEmployees(pageable, search);
        return ResponseEntity.ok(employees);
    }

    @Operation(
            operationId = "getEmployeeOptions",
            summary = "Obter opções de funcionários",
            description = "Retorna uma lista simplificada de funcionários para uso em dropdowns."
    )
    @ApiResponse(responseCode = "200")
    @GetMapping("/options")
    public ResponseEntity<List<EmployeeOptionDTO>> getEmployeeOptions() {
        List<EmployeeOptionDTO> options = employeeService.getEmployeeOptions();
        return ResponseEntity.ok(options);
    }

    @Operation(
            operationId = "getEmployeeById",
            summary = "Obter funcionário por ID",
            description = "Retorna um único funcionário com base no ID fornecido."
    )
    @ApiResponse(responseCode = "200")
    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable UUID employeeId) {

        EmployeeResponseDTO foundEmployee = employeeService.findById(employeeId);
        return ResponseEntity.ok(foundEmployee);
    }

    @Operation(
            operationId = "updateEmployee",
            summary = "Atualizar funcionário",
            description = "Atualiza parcialmente um funcionário existente com os dados fornecidos."
    )
    @ApiResponse(responseCode = "200")
    @PatchMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(@PathVariable UUID employeeId, @RequestBody @Valid EmployeeRequestDTO request) {

        EmployeeResponseDTO updatedEmployee = employeeService.updateEmployee(employeeId, request);
        return ResponseEntity.ok(updatedEmployee);
    }

    @Operation(
            operationId = "deleteEmployee",
            summary = "Deletar funcionário",
            description = "Deleta um funcionário com base no ID fornecido."
    )
    @ApiResponse(responseCode = "204")
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID employeeId) {

        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            operationId = "archiveEmployee",
            summary = "Arquivar funcionário",
            description = "Arquiva um funcionário com base no ID fornecido."
    )
    @ApiResponse(responseCode = "204")
    @PatchMapping("/{employeeId}/archive")
    public ResponseEntity<Void> archiveEmployee(@PathVariable UUID employeeId) {

        employeeService.archiveEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            operationId = "unarchiveEmployee",
            summary = "Desarquivar funcionário",
            description = "Desarquiva um funcionário com base no ID fornecido."
    )
    @ApiResponse(responseCode = "204")
    @PatchMapping("/{employeeId}/unarchive")
    public ResponseEntity<Void> unarchiveEmployee(@PathVariable UUID employeeId) {

        employeeService.unarchiveEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }

}
