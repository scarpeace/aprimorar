package com.aprimorar.api.domain.parent.web;

import java.util.List;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.aprimorar.api.domain.parent.dto.ParentOptionDTO;
import com.aprimorar.api.domain.parent.dto.ParentRequestDTO;
import com.aprimorar.api.domain.parent.dto.ParentResponseDTO;
import com.aprimorar.api.exception.ProblemDetailResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Parent", description = "Parent management APIs")
public interface ParentControllerDocs {

    @Operation(
            operationId = "createParent",
            summary = "Criar responsável",
            description = "Cria um novo responsável com os dados fornecidos."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Responsável criado com sucesso",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ParentResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Requisição inválida",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProblemDetailResponseDTO.class)))
    })
    ResponseEntity<ParentResponseDTO> createParent(ParentRequestDTO request);

    @Operation(
            operationId = "getParents",
            summary = "Listar responsáveis",
            description = "Lista todos os responsáveis cadastrados."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Responsáveis listados com sucesso",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ParentResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Requisição inválida",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProblemDetailResponseDTO.class)))
    })
    ResponseEntity<Page<ParentResponseDTO>> getParents(
            @ParameterObject Pageable pageable,
            @Parameter(description = "Termo de busca") String search
    );

    @Operation(
            operationId = "getParentById",
            summary = "Listar responsável por ID",
            description = "Lista um responsável específico pelo ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Responsável listado com sucesso",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ParentResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Requisição inválida",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProblemDetailResponseDTO.class)))
    })
    ResponseEntity<ParentResponseDTO> getParentById(
            @Parameter(description = "ID do responsável") UUID parentId
    );

    @Operation(
            operationId = "getParentsOptions",
            summary = "Listar responsáveis para listas e dropdowns",
            description = "Lista todos os responsáveis cadastrados para uso em listas e dropdowns."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Responsáveis listados com sucesso",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ParentOptionDTO.class))),
        @ApiResponse(responseCode = "400", description = "Requisição inválida",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProblemDetailResponseDTO.class)))
    })
    ResponseEntity<List<ParentOptionDTO>> getParentsOptions();

    @Operation(
            operationId = "updateParent",
            summary = "Atualizar responsável",
            description = "Atualiza totalmente os dados de um responsável."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Responsável atualizado com sucesso",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ParentResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Requisição inválida",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProblemDetailResponseDTO.class)))
    })
    ResponseEntity<ParentResponseDTO> updateParent(
            @Parameter(description = "ID do responsável") UUID parentId,
            @Parameter(description = "Dados do responsável") ParentRequestDTO request
    );

    //TODO: falta colocar o código certo dos erros (não precisa colocar da resposta)
    @Operation(
            operationId = "deleteParent",
            summary = "Deletar responsável",
            description = "Deleta um responsável específico."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Responsável deletado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProblemDetailResponseDTO.class)))
    })
    ResponseEntity<Void> deleteParent(
            @Parameter(description = "ID do responsável") UUID parentId
    );

    @Operation(
            operationId = "archiveParent",
            summary = "Arquivar responsável",
            description = "Arquiva um responsável específico."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Responsável arquivado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Responsável não encontrado",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProblemDetailResponseDTO.class)))
    })
    ResponseEntity<Void> archiveParent(
            @Parameter(description = "ID do responsável") UUID parentId
    );

    @Operation(
            operationId = "unarchiveParent",
            summary = "Desarquivar responsável",
            description = "Desarquiva um responsável específico."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Responsável desarquivado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Responsável não encontrado",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProblemDetailResponseDTO.class)))
    })
    ResponseEntity<Void> unarchiveParent(
            @Parameter(description = "ID do responsável") UUID parentId
    );

}
