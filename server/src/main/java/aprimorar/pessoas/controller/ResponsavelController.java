package aprimorar.pessoas.controller;

import aprimorar.pessoas.dto.ResponsaveisListDTO;
import aprimorar.pessoas.dto.ResponsavelRequestDTO;
import aprimorar.pessoas.dto.ResponsavelResponseDTO;
import aprimorar.pessoas.service.ResponsavelMutationService;
import aprimorar.pessoas.service.ResponsavelQueryService;
import aprimorar.shared.PageDTO;
import aprimorar.shared.exception.ProblemResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;
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
@RequestMapping("/v1/responsaveis")
@Tag(name = "Responsavel", description = "APIs de gestão de responsáveis")
public class ResponsavelController {

    private final ResponsavelQueryService responsavelQueryService;
    private final ResponsavelMutationService responsavelMutationService;

    public ResponsavelController(ResponsavelQueryService responsavelQueryService, ResponsavelMutationService responsavelMutationService) {
        this.responsavelQueryService = responsavelQueryService;
        this.responsavelMutationService = responsavelMutationService;
    }

    @PostMapping
    @Operation(operationId = "createResponsavel", description = "Cria um novo responsável")
    @ApiResponse(responseCode = "201", description = "Responsável criado com sucesso")
    @ApiResponse(
        responseCode = "400",
        description = "Falha de validação",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    @ApiResponse(
        responseCode = "409",
        description = "Conflito de regra de negócio",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    @ApiResponse(
        responseCode = "500",
        description = "Erro interno do sistema",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ResponseEntity<ResponsavelResponseDTO> createResponsavel(@RequestBody @Valid ResponsavelRequestDTO responsavelRequestDTO) {
        ResponsavelResponseDTO responsavel = responsavelMutationService.createResponsavel(responsavelRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responsavel);
    }

    @GetMapping
    @Operation(operationId = "getResponsaveis", description = "Retorna uma lista de responsáveis paginada")
    @ApiResponse(responseCode = "200", description = "Lista de responsáveis retornada com sucesso.")
    @ApiResponse(
        responseCode = "400",
        description = "Parâmetros inválidos",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    @ApiResponse(
        responseCode = "500",
        description = "Erro interno do sistema",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ResponseEntity<PageDTO<ResponsavelResponseDTO>> getResponsaveis(
        @PageableDefault(sort = "name") @ParameterObject Pageable pageable,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) Boolean archived
    ) {
        return ResponseEntity.ok(responsavelQueryService.getResponsaveis(pageable, search, archived));
    }

    @GetMapping("/options")
    @Operation(operationId = "listResponsaveis", description = "Retorna uma lista de responsáveis para dropdown")
    @ApiResponse(responseCode = "200", description = "Lista de opções retornada com sucesso.")
    @ApiResponse(
        responseCode = "500",
        description = "Erro interno do sistema",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ResponseEntity<List<ResponsaveisListDTO>> listResponsaveis() {
        return ResponseEntity.ok(responsavelQueryService.listResponsaveis());
    }

    @GetMapping("/{responsavelId}")
    @Operation(operationId = "getResponsavelById", description = "Retorna um responsável por ID")
    @ApiResponse(responseCode = "200", description = "Responsável retornado com sucesso")
    @ApiResponse(
        responseCode = "404",
        description = "Responsável não encontrado",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    @ApiResponse(
        responseCode = "500",
        description = "Erro interno do sistema",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ResponseEntity<ResponsavelResponseDTO> getResponsavelById(@PathVariable UUID responsavelId) {
        ResponsavelResponseDTO responsavel = responsavelQueryService.findResponsavelById(responsavelId);
        return ResponseEntity.ok(responsavel);
    }

    @PatchMapping("/{responsavelId}")
    @Operation(operationId = "updateResponsavel", description = "Atualiza um responsável por ID")
    @ApiResponse(responseCode = "200", description = "Responsável atualizado com sucesso")
    @ApiResponse(
        responseCode = "400",
        description = "Falha de validação",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    @ApiResponse(
        responseCode = "404",
        description = "Responsável não encontrado",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    @ApiResponse(
        responseCode = "409",
        description = "Conflito de regra de negócio",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    @ApiResponse(
        responseCode = "500",
        description = "Erro interno do sistema",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ResponseEntity<ResponsavelResponseDTO> updateResponsavel(
        @PathVariable UUID responsavelId,
        @RequestBody @Valid ResponsavelRequestDTO responsavelRequestDTO
    ) {
        ResponsavelResponseDTO responsavel = responsavelMutationService.updateResponsavel(responsavelId, responsavelRequestDTO);

        return ResponseEntity.ok(responsavel);
    }

    @PatchMapping("/{responsavelId}/archive")
    @Operation(operationId = "archiveResponsavel", description = "Arquiva um responsável por ID")
    @ApiResponse(responseCode = "204", description = "Responsável arquivado com sucesso")
    @ApiResponse(
        responseCode = "404",
        description = "Responsável não encontrado",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    @ApiResponse(
        responseCode = "409",
        description = "Conflito de regra de negócio",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    @ApiResponse(
        responseCode = "500",
        description = "Erro interno do sistema",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ResponseEntity<Void> archiveResponsavel(@PathVariable UUID responsavelId) {
        responsavelMutationService.archiveResponsavel(responsavelId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{responsavelId}/unarchive")
    @Operation(operationId = "unarchiveResponsavel", description = "Desarquiva um responsável por ID")
    @ApiResponse(responseCode = "204", description = "Responsável desarquivado com sucesso")
    @ApiResponse(
        responseCode = "404",
        description = "Responsável não encontrado",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    @ApiResponse(
        responseCode = "409",
        description = "Conflito de regra de negócio",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    @ApiResponse(
        responseCode = "500",
        description = "Erro interno do sistema",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ResponseEntity<Void> unarchiveResponsavel(@PathVariable UUID responsavelId) {
        responsavelMutationService.unarchiveResponsavel(responsavelId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{responsavelId}")
    @Operation(operationId = "deleteResponsavel", description = "Deleta um responsável por ID")
    @ApiResponse(responseCode = "204", description = "Responsável deletado com sucesso")
    @ApiResponse(
        responseCode = "404",
        description = "Responsável não encontrado",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    @ApiResponse(
        responseCode = "409",
        description = "Conflito de regra de negócio",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    @ApiResponse(
        responseCode = "500",
        description = "Erro interno do sistema",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ResponseEntity<Void> deleteResponsavel(@PathVariable UUID responsavelId, @RequestParam(required = false, defaultValue = "false") boolean cascade) {
        responsavelMutationService.deleteResponsavel(responsavelId, cascade);
        return ResponseEntity.noContent().build();
    }
}
