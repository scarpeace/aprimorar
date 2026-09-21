package aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.web;

import aprimorar.common.openapi.BadRequestProblemResponse;
import aprimorar.common.openapi.CommonProblemResponses;
import aprimorar.common.openapi.ConflictProblemResponse;
import aprimorar.common.openapi.NotFoundProblemResponse;
import aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.service.PagamentoParticularService;
import aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.web.dto.PagamentoParticularDetalheResponse;
import aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.web.dto.PagamentoParticularFiltroRequest;
import aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.web.dto.PagamentoParticularResponse;
import aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.web.dto.RegistrarPagamentoParticularRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.net.URI;
import java.util.UUID;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/financeiro/repasses/pagamentos")
@Tag(
    name = "Pagamentos particulares",
    description = "APIs para registrar, consultar e cancelar pagamentos de repasses particulares"
)
@CommonProblemResponses
@ConflictProblemResponse
public class PagamentoParticularController {

    private final PagamentoParticularService pagamentoService;

    public PagamentoParticularController(PagamentoParticularService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PostMapping
    @Operation(
        operationId = "registrarPagamentoParticular",
        description = "Registra um pagamento para um ou mais repasses do mesmo colaborador."
    )
    @ApiResponse(responseCode = "201", description = "Pagamento registrado.")
    @BadRequestProblemResponse
    @NotFoundProblemResponse
    public ResponseEntity<Void> registrarPagamento(
        @RequestBody @Valid RegistrarPagamentoParticularRequest request
    ) {
        UUID id = pagamentoService.registrarPagamento(
            request.repasseIds(),
            request.dataPagamento(),
            request.formaPagamento(),
            request.comprovanteUrl()
        );

        return ResponseEntity.created(
            URI.create("/financeiro/repasses/pagamentos/" + id)
        ).build();
    }

    @GetMapping
    @Operation(
        operationId = "buscarPagamentosParticulares",
        description = "Lista pagamentos de repasses particulares com filtros e paginação."
    )
    @ApiResponse(responseCode = "200", description = "Pagamentos encontrados.")
    @BadRequestProblemResponse
    public ResponseEntity<Page<PagamentoParticularResponse>> buscarPagamentos(
        @ParameterObject @Valid PagamentoParticularFiltroRequest filtro,
        @ParameterObject
        @PageableDefault(sort = "dataPagamento", direction = Sort.Direction.DESC)
        Pageable pageable
    ) {
        Page<PagamentoParticularResponse> pagamentos = pagamentoService
            .getPagamentos(filtro, pageable);

        return ResponseEntity.ok(pagamentos);
    }

    @GetMapping("/{pagamentoId}")
    @Operation(
        operationId = "buscarPagamentoParticularPorId",
        description = "Busca os detalhes de um pagamento de repasses particulares pelo ID."
    )
    @ApiResponse(responseCode = "200", description = "Pagamento encontrado.")
    @BadRequestProblemResponse
    @NotFoundProblemResponse
    public ResponseEntity<PagamentoParticularDetalheResponse> buscarPorId(
        @PathVariable UUID pagamentoId
    ) {
        PagamentoParticularDetalheResponse pagamento = pagamentoService
            .getPagamentoPorId(pagamentoId);

        return ResponseEntity.ok(pagamento);
    }

    @DeleteMapping("/{pagamentoId}")
    @Operation(
        operationId = "cancelarPagamentoParticular",
        description = "Cancela o pagamento inteiro e libera todos os repasses vinculados."
    )
    @ApiResponse(responseCode = "204", description = "Pagamento cancelado.")
    @BadRequestProblemResponse
    @NotFoundProblemResponse
    public ResponseEntity<Void> cancelarPagamento(@PathVariable UUID pagamentoId) {
        pagamentoService.cancelarPagamento(pagamentoId);
        return ResponseEntity.noContent().build();
    }
}
