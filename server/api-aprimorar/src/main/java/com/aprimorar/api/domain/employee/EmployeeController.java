package com.aprimorar.api.domain.employee;

import com.aprimorar.api.domain.employee.dto.EmployeeRequestDTO;
import com.aprimorar.api.domain.employee.dto.EmployeeOptionsDTO;
import com.aprimorar.api.domain.employee.dto.EmployeeResponseDTO;
import com.aprimorar.api.shared.PageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
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

@Slf4j
@RestController
@RequestMapping("/v1/employees")
@Tag(name = "Employee", description = "Employee management APIs")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @Operation(operationId = "createEmployee", description = "Cria um novo colaborador com os dados fornecidos.")
    @ApiResponse(responseCode = "201", description = "Colaborador criado com sucesso.")
    public ResponseEntity<EmployeeResponseDTO> createEmployee(
        @RequestBody @Valid EmployeeRequestDTO employeeRequestDto
    ) {
        EmployeeResponseDTO response = employeeService.createEmployee(employeeRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(operationId = "getEmployees", description = "Retorna uma lista paginada de colaboradores.")
    @ApiResponse(responseCode = "200", description = "Lista de colaboradores retornada com sucesso.")
    public ResponseEntity<PageDTO<EmployeeResponseDTO>> getEmployees(
        @ParameterObject @PageableDefault(sort = "name") Pageable pageable,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) boolean archived
    ) {
        PageDTO<EmployeeResponseDTO> employees = employeeService.getEmployees(pageable, search, archived);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/options")
    @Operation(operationId = "getEmployeeOptions", description = "Retorna uma lista de opções de colaboradores.")
    @ApiResponse(responseCode = "200", description = "Lista de opções de colaboradores retornada com sucesso.")
    public ResponseEntity<List<EmployeeOptionsDTO>> getEmployeeOptions() {
        List<EmployeeOptionsDTO> options = employeeService.getEmployeeOptions();
        return ResponseEntity.ok(options);
    }

    @GetMapping("/{employeeId}")
    @Operation(operationId = "getEmployeeById", description = "Retorna um colaborador por ID.")
    @ApiResponse(responseCode = "200", description = "Colaborador retornado com sucesso.")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable UUID employeeId) {
        EmployeeResponseDTO foundEmployee = employeeService.findById(employeeId);
        return ResponseEntity.ok(foundEmployee);
    }

    @PatchMapping("/{employeeId}")
    @Operation(operationId = "updateEmployee", description = "Atualiza um colaborador por ID.")
    @ApiResponse(responseCode = "200", description = "Colaborador atualizado com sucesso.")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(
        @PathVariable UUID employeeId,
        @RequestBody @Valid EmployeeRequestDTO employeeRequestDTO
    ) {
        EmployeeResponseDTO updatedEmployee = employeeService.updateEmployee(employeeId, employeeRequestDTO);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{employeeId}")
    @Operation(operationId = "deleteEmployee", description = "Deleta um colaborador por ID.")
    @ApiResponse(responseCode = "204", description = "Colaborador deletado com sucesso.")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{employeeId}/archive")
    @Operation(operationId = "archiveEmployee", description = "Arquiva um colaborador por ID.")
    @ApiResponse(responseCode = "204", description = "Colaborador arquivado com sucesso.")
    public ResponseEntity<Void> archiveEmployee(@PathVariable UUID employeeId) {
        employeeService.archiveEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{employeeId}/unarchive")
    @Operation(operationId = "unarchiveEmployee", description = "Desarquiva um colaborador por ID.")
    @ApiResponse(responseCode = "204", description = "Colaborador desarquivado com sucesso.")
    public ResponseEntity<Void> unarchiveEmployee(@PathVariable UUID employeeId) {
        employeeService.unarchiveEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }
}
