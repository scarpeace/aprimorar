package com.aprimorar.api.domain.employee.web;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.aprimorar.api.domain.employee.dto.EmployeeRequestDTO;
import com.aprimorar.api.domain.employee.dto.EmployeeResponseDTO;
import com.aprimorar.api.domain.employee.dto.EmployeeSummaryDTO;
import com.aprimorar.api.exception.ProblemDetailResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Employee", description = "Employee management APIs")
public interface EmployeeControllerDocs {

    @Operation(
            operationId = "createEmployee",
            summary = "Criar funcionário",
            description = "Cria um novo funcionário com os dados fornecidos."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Funcionário criado com sucesso",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = EmployeeResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Requisição inválida",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProblemDetailResponseDTO.class)))
    })
    ResponseEntity<EmployeeResponseDTO> createEmployee(EmployeeRequestDTO request);

    @Operation(
            operationId = "getEmployees",
            summary = "Listar funcionários",
            description = "Retorna todos os funcionários do banco de dados com paginação."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Listagem dos funcionários com paginação"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProblemDetailResponseDTO.class)))
    })
    ResponseEntity<Page<EmployeeResponseDTO>> getEmployees(
            @Parameter(description = "Página com informações de paginação") Pageable pageable,
            @Parameter(description = "Termo de busca") String search
    );

    @Operation(
            operationId = "getEmployeeSummary",
            summary = "Obter opções de funcionários",
            description = "Retorna uma lista simplificada de funcionários para uso em dropdowns."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Listagem dos funcionários para opções e dropdown",
                content = @Content(mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = EmployeeSummaryDTO.class)))),
        @ApiResponse(responseCode = "400", description = "Requisição inválida",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProblemDetailResponseDTO.class)))
    })
    ResponseEntity<List<EmployeeSummaryDTO>> getEmployeeSummary();

    @Operation(
            operationId = "getEmployeeById",
            summary = "Obter funcionário por ID",
            description = "Retorna um único funcionário com base no ID fornecido."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Listagem do funcionário por ID",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = EmployeeResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Requisição inválida",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProblemDetailResponseDTO.class)))
    })
    ResponseEntity<EmployeeResponseDTO> getEmployeeById(
            @Parameter(description = "ID do funcionário") UUID employeeId
    );

    @Operation(
            operationId = "updateEmployee",
            summary = "Atualizar funcionário",
            description = "Atualiza parcialmente um funcionário existente com os dados fornecidos."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Funcionário atualizado com sucesso",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = EmployeeResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Requisição inválida",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProblemDetailResponseDTO.class)))
    })
    ResponseEntity<EmployeeResponseDTO> updateEmployee(
            @Parameter(description = "ID do funcionário") UUID employeeId,
            @Parameter(description = "Dados do funcionário") EmployeeRequestDTO request
    );

    @Operation(
            operationId = "deleteEmployee",
            summary = "Deletar funcionário",
            description = "Deleta um funcionário com base no ID fornecido."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Funcionário deletado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProblemDetailResponseDTO.class)))
    })
    ResponseEntity<Void> deleteEmployee(
            @Parameter(description = "ID do funcionário") UUID employeeId
    );

    @Operation(
            operationId = "archiveEmployee",
            summary = "Arquivar funcionário",
            description = "Arquiva um funcionário com base no ID fornecido."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Funcionário arquivado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProblemDetailResponseDTO.class)))
    })
    ResponseEntity<Void> archiveEmployee(
            @Parameter(description = "ID do funcionário") UUID employeeId
    );

    @Operation(
            operationId = "unarchiveEmployee",
            summary = "Desarquivar funcionário",
            description = "Desarquiva um funcionário com base no ID fornecido."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Funcionário desarquivado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProblemDetailResponseDTO.class)))
    })
    ResponseEntity<Void> unarchiveEmployee(
            @Parameter(description = "ID do funcionário") UUID employeeId
    );

}
