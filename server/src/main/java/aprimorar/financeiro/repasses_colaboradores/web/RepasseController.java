package aprimorar.financeiro.repasses_colaboradores.web;

import aprimorar.common.openapi.BadRequestProblemResponse;
import aprimorar.common.openapi.CommonProblemResponses;
import aprimorar.common.openapi.NotFoundProblemResponse;
import aprimorar.financeiro.repasses_colaboradores.application.PagamentoService;
import aprimorar.financeiro.repasses_colaboradores.web.dto.RepasseFiltroRequest;
import aprimorar.financeiro.repasses_colaboradores.web.dto.RepasseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/financeiro/repasses")
@Tag(name = "Repasses",description = "APIs para consultar repasses de atendimentos individuais")
@CommonProblemResponses
class RepasseController {

    private final PagamentoService pagamentoService;

    RepasseController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @GetMapping
    @Operation(operationId = "buscarRepasses",description = "Lista repasses com filtros e paginação.")
    @ApiResponse(responseCode = "200", description = "Repasses encontrados.")
    @BadRequestProblemResponse
    public ResponseEntity<Page<RepasseResponse>> buscarRepasses(
        @ParameterObject @Valid RepasseFiltroRequest filtro,
        @ParameterObject @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable
    ) {
        Page<RepasseResponse> repasses = pagamentoService.getRepasses(filtro, pageable);
        return ResponseEntity.ok(repasses);
    }

    @GetMapping("/{repasseId}")
    @Operation(operationId = "buscarRepassePorId",description = "Busca um repasse por ID.")
    @ApiResponse(responseCode = "200", description = "Repasse encontrado.")
    @NotFoundProblemResponse
    public ResponseEntity<RepasseResponse> buscarPorId(
        @PathVariable Long repasseId
    ) {
        RepasseResponse repasse = pagamentoService.findRepasseById(repasseId);
        return ResponseEntity.ok(repasse);
    }
}
