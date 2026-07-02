package aprimorar.pessoas.controller;

import aprimorar.pessoas.dto.responsavel.ResponsavelFiltroRequest;
import aprimorar.pessoas.dto.responsavel.ResponsavelRequestDTO;
import aprimorar.pessoas.dto.responsavel.ResponsavelResponseDTO;
import aprimorar.pessoas.service.ResponsavelMutationService;
import aprimorar.pessoas.service.ResponsavelQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<ResponsavelResponseDTO> createResponsavel(@RequestBody @Valid ResponsavelRequestDTO responsavelRequestDTO) {
        ResponsavelResponseDTO responsavel = responsavelMutationService.createResponsavel(responsavelRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responsavel);
    }

    @GetMapping
    @Operation(operationId = "getResponsaveis", description = "Retorna uma lista de responsáveis paginada")
    @ApiResponse(responseCode = "200", description = "Lista de responsáveis retornada com sucesso.")
    public ResponseEntity<Page<ResponsavelResponseDTO>> getResponsaveis(
        @ParameterObject ResponsavelFiltroRequest filtro,
        @ParameterObject @PageableDefault(sort = "nome") Pageable pageable
    ) {
        return ResponseEntity.ok(responsavelQueryService.getResponsaveis(filtro, pageable));
    }

    @GetMapping("/list")
    @Operation(operationId = "listResponsaveis", description = "Retorna uma lista de responsáveis para dropdown")
    @ApiResponse(responseCode = "200", description = "Lista de opções retornada com sucesso.")
    public ResponseEntity<List<ResponsavelResponseDTO>> listResponsaveis() {
        return ResponseEntity.ok(responsavelQueryService.getResponsaveisList());
    }

    @GetMapping("/{responsavelId}")
    @Operation(operationId = "getResponsavelById", description = "Retorna um responsável por ID")
    @ApiResponse(responseCode = "200", description = "Responsável retornado com sucesso")
    public ResponseEntity<ResponsavelResponseDTO> getResponsavelById(@PathVariable UUID responsavelId) {
        ResponsavelResponseDTO responsavel = responsavelQueryService.findResponsavelById(responsavelId);
        return ResponseEntity.ok(responsavel);
    }

    @PatchMapping("/{responsavelId}")
    @Operation(operationId = "updateResponsavel", description = "Atualiza um responsável por ID")
    @ApiResponse(responseCode = "200", description = "Responsável atualizado com sucesso")
    public ResponseEntity<ResponsavelResponseDTO> updateResponsavel(
        @PathVariable UUID responsavelId,
        @RequestBody @Valid ResponsavelRequestDTO responsavelRequestDTO
    ) {
        ResponsavelResponseDTO responsavel = responsavelMutationService.updateResponsavel(responsavelId, responsavelRequestDTO);
        return ResponseEntity.ok(responsavel);
    }

    @DeleteMapping("/{responsavelId}")
    @Operation(operationId = "deleteResponsavel", description = "Deleta um responsável por ID")
    @ApiResponse(responseCode = "204", description = "Responsável deletado com sucesso")
    public ResponseEntity<Void> deleteResponsavel(@PathVariable UUID responsavelId) {
        responsavelMutationService.deleteResponsavel(responsavelId);
        return ResponseEntity.noContent().build();
    }
}
