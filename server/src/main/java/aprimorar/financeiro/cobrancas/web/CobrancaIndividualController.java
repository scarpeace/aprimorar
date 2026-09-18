package aprimorar.financeiro.cobrancas.web;


import aprimorar.common.openapi.BadRequestProblemResponse;
import aprimorar.common.openapi.CommonProblemResponses;
import aprimorar.common.openapi.NotFoundProblemResponse;
import aprimorar.financeiro.cobrancas.service.CobrancaIndividualService;
import aprimorar.financeiro.cobrancas.web.dto.CancelarCobrancasIndividualRequest;
import aprimorar.financeiro.cobrancas.web.dto.CobrancaIndividualFiltroRequest;
import aprimorar.financeiro.cobrancas.web.dto.CobrancaIndividualResponse;
import aprimorar.financeiro.cobrancas.web.dto.CobrancaLoteDetalheResponse;
import aprimorar.financeiro.cobrancas.web.dto.CobrancaLoteResponse;
import aprimorar.financeiro.cobrancas.web.dto.RegistrarPagamentoIndividualRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/financeiro/cobrancas")
@Tag(
    name = "Cobranças individuais",
    description = "APIs para gestão de cobranças individuais"
)
@CommonProblemResponses
public class CobrancaIndividualController {

    private final CobrancaIndividualService cobrancaService;

    public CobrancaIndividualController(CobrancaIndividualService cobrancaService) {
        this.cobrancaService = cobrancaService;
    }

    @GetMapping
    @Operation(operationId = "buscarCobrancasIndividuais", description = "Lista cobranças individuais.")
    @ApiResponse(responseCode = "200", description = "Cobranças encontradas.")
    @BadRequestProblemResponse
    public ResponseEntity<Page<CobrancaIndividualResponse>> buscarCobrancas(
        @ParameterObject @Valid CobrancaIndividualFiltroRequest filtro,
        @ParameterObject @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(cobrancaService.buscarCobrancas(filtro, pageable));
    }

    @GetMapping("/lotes")
    @Operation(
        operationId = "buscarLotesDeCobranca",
        description = "Lista as cobranças de atendimentos individuais pagas pelo aluno agrupadas por lote."
    )
    @ApiResponse(responseCode = "200", description = "Lotes de cobrança encontrados.")
    @BadRequestProblemResponse
    public ResponseEntity<Page<CobrancaLoteResponse>> buscarLotes(
        @RequestParam UUID alunoId,
        @ParameterObject @PageableDefault(size = 10) Pageable pageable
    ) {
        return ResponseEntity.ok(cobrancaService.buscarLotes(alunoId, pageable));
    }

    @GetMapping("/lotes/{loteId}")
    @Operation(
        operationId = "buscarLoteDeCobrancaPorId",
        description = "Busca os detalhes de um lote de cobranças pelo identificador."
    )
    @ApiResponse(responseCode = "200", description = "Detalhes do lote de cobranças encontrados.")
    @NotFoundProblemResponse
    public ResponseEntity<CobrancaLoteDetalheResponse> buscarLotePorId(@PathVariable UUID loteId) {
        return ResponseEntity.ok(cobrancaService.buscarLotePorId(loteId));
    }

    @GetMapping("/{id}")
    @Operation(operationId = "buscarCobrancaIndividualPorId", description = "Busca uma cobrança individual por ID.")
    @ApiResponse(responseCode = "200", description = "Cobrança encontrada.")
    @NotFoundProblemResponse
    public ResponseEntity<CobrancaIndividualResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cobrancaService.buscarCobrancaPorId(id));
    }

    @PostMapping("/pagar")
    @Operation(operationId = "registrarPagamentoCobrancasIndividuais", description = "Registra o pagamento de cobranças individuais.")
    @ApiResponse(responseCode = "204", description = "Pagamento registrado.")
    @BadRequestProblemResponse
    @NotFoundProblemResponse
    public ResponseEntity<Void> registrarPagamento(@RequestBody @Valid RegistrarPagamentoIndividualRequest request) {
        cobrancaService.registrarPagamento(request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/cancelar")
    @Operation(operationId = "cancelarPagamentoCobrancasIndividuais", description = "Cancela pagamentos de cobranças individuais.")
    @ApiResponse(responseCode = "204", description = "Pagamentos cancelados.")
    @BadRequestProblemResponse
    @NotFoundProblemResponse
    public ResponseEntity<Void> cancelarPagamento(@RequestBody @Valid CancelarCobrancasIndividualRequest request) {
        cobrancaService.cancelarPagamento(request);
        return ResponseEntity.noContent().build();
    }
}
