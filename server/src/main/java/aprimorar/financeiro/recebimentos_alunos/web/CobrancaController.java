package aprimorar.financeiro.recebimentos_alunos.web;

import aprimorar.common.openapi.BadRequestProblemResponse;
import aprimorar.common.openapi.CommonProblemResponses;
import aprimorar.common.openapi.NotFoundProblemResponse;
import aprimorar.financeiro.recebimentos_alunos.application.RecebimentoService;
import aprimorar.financeiro.recebimentos_alunos.web.dto.CobrancaFiltroRequest;
import aprimorar.financeiro.recebimentos_alunos.web.dto.CobrancaResponse;
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
@RequestMapping("/financeiro/cobrancas/particular")
@Tag(
    name = "Cobranças particulares",
    description = "APIs para consultar cobranças de atendimentos individuais"
)
@CommonProblemResponses
class CobrancaController {

    private final RecebimentoService recebimentoService;

    CobrancaController(RecebimentoService recebimentoService) {
        this.recebimentoService = recebimentoService;
    }

    @GetMapping
    @Operation(
        operationId = "buscarCobrancasParticulares",
        description = "Lista cobranças particulares com filtros e paginação."
    )
    @ApiResponse(responseCode = "200", description = "Cobranças encontradas.")
    @BadRequestProblemResponse
    public ResponseEntity<Page<CobrancaResponse>> buscarCobrancas(
        @ParameterObject @Valid CobrancaFiltroRequest filtro,
        @ParameterObject
        @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
        Pageable pageable
    ) {
        Page<CobrancaResponse> cobrancas = recebimentoService
            .getCobrancas(filtro, pageable);

        return ResponseEntity.ok(cobrancas);
    }

    @GetMapping("/{cobrancaId}")
    @Operation(
        operationId = "buscarCobrancaParticularPorId",
        description = "Busca uma cobrança particular por ID."
    )
    @ApiResponse(responseCode = "200", description = "Cobrança encontrada.")
    @NotFoundProblemResponse
    public ResponseEntity<CobrancaResponse> buscarPorId(
        @PathVariable Long cobrancaId
    ) {
        CobrancaResponse cobranca = recebimentoService
            .getCobrancaPorId(cobrancaId);

        return ResponseEntity.ok(cobranca);
    }
}
