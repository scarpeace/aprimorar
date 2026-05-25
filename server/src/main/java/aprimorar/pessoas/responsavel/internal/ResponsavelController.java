package aprimorar.pessoas.responsavel.internal;

import aprimorar.pessoas.responsavel.api.dto.ResponsaveisListDTO;
import aprimorar.pessoas.responsavel.api.dto.ResponsavelRequestDTO;
import aprimorar.pessoas.responsavel.api.dto.ResponsavelResponseDTO;
import aprimorar.shared.PageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    @Operation(operationId = "criarResponsavel", description = "Cria um novo responsável")
    @ApiResponse(responseCode = "200", description = "Responsável criado com sucesso")
    public ResponseEntity<ResponsavelResponseDTO> createResponsavel(@RequestBody @Valid ResponsavelRequestDTO parentRequestDTO) {
        ResponsavelResponseDTO parent = responsavelMutationService.createResponsavel(parentRequestDTO);
        return ResponseEntity.ok(parent);
    }

    @GetMapping
    @Operation(operationId = "listarResponsaveis", description = "Retorna uma lista de responsáveis paginada")
    @ApiResponse(responseCode = "200", description = "Lista de responsáveis retornada com sucesso.")
    public ResponseEntity<PageDTO<ResponsavelResponseDTO>> getResponsaveis(
        @PageableDefault(sort = "name") @ParameterObject Pageable pageable,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) Boolean archived
    ) {
        return ResponseEntity.ok(responsavelQueryService.getResponsaveis(pageable, search, archived));
    }

    @GetMapping("/options")
    @Operation(operationId = "listarOpcoesResponsaveis", description = "Retorna uma lista de responsáveis para dropdown")
    @ApiResponse(responseCode = "200", description = "Lista de opções retornada com sucesso.")
    public ResponseEntity<List<ResponsaveisListDTO>> listResponsaveis() {
        return ResponseEntity.ok(responsavelQueryService.listResponsaveis());
    }

    @GetMapping("/{parentId}")
    @Operation(operationId = "buscarResponsavelPorId", description = "Retorna um responsável por ID")
    @ApiResponse(responseCode = "200", description = "Responsável retornado com sucesso")
    public ResponseEntity<ResponsavelResponseDTO> getResponsavelById(@PathVariable UUID parentId) {
        ResponsavelResponseDTO parent = responsavelQueryService.findResponsavelById(parentId);
        return ResponseEntity.ok(parent);
    }

    @PatchMapping("/{parentId}")
    @Operation(operationId = "atualizarResponsavel", description = "Atualiza um responsável por ID")
    @ApiResponse(responseCode = "200", description = "Responsável atualizado com sucesso")
    public ResponseEntity<ResponsavelResponseDTO> updateResponsavel(
        @PathVariable UUID parentId,
        @RequestBody @Valid ResponsavelRequestDTO parentRequestDTO
    ) {
        ResponsavelResponseDTO parent = responsavelMutationService.updateResponsavel(parentId, parentRequestDTO);

        return ResponseEntity.ok(parent);
    }

    @PatchMapping("/{parentId}/archive")
    @Operation(operationId = "arquivarResponsavel", description = "Arquiva um responsável por ID")
    @ApiResponse(responseCode = "204", description = "Responsável arquivado com sucesso")
    public ResponseEntity<Void> archiveResponsavel(@PathVariable UUID parentId) {
        responsavelMutationService.archiveResponsavel(parentId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{parentId}/unarchive")
    @Operation(operationId = "desarquivarResponsavel", description = "Desarquiva um responsável por ID")
    @ApiResponse(responseCode = "204", description = "Responsável desarquivado com sucesso")
    public ResponseEntity<Void> unarchiveResponsavel(@PathVariable UUID parentId) {
        responsavelMutationService.unarchiveResponsavel(parentId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{parentId}")
    @Operation(operationId = "deletarResponsavel", description = "Deleta um responsável por ID")
    @ApiResponse(responseCode = "204", description = "Responsável deletado com sucesso")
    public ResponseEntity<Void> deleteResponsavel(@PathVariable UUID parentId) {
        responsavelMutationService.deleteResponsavel(parentId);
        return ResponseEntity.noContent().build();
    }
}
