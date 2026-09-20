package aprimorar.financeiro.financeiro_aluno.pagamentos.web;

import aprimorar.common.openapi.BadRequestProblemResponse;
import aprimorar.common.openapi.CommonProblemResponses;
import aprimorar.common.openapi.NotFoundProblemResponse;
import aprimorar.financeiro.financeiro_aluno.pagamentos.service.PagamentoAlunoService;
import aprimorar.financeiro.financeiro_aluno.pagamentos.web.dto.PagamentoAlunoDetalheResponse;
import aprimorar.financeiro.financeiro_aluno.pagamentos.web.dto.PagamentoAlunoResponse;
import aprimorar.financeiro.financeiro_aluno.pagamentos.web.dto.RegistrarPagamentoAlunoRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/financeiro/cobrancas/pagamentos")
@Tag(
    name = "Pagamentos de cobranças de alunos",
    description = "APIs para gestão de pagamentos de cobranças de alunos"
)
@CommonProblemResponses
public class PagamentoAlunoController {

    private final PagamentoAlunoService pagamentoService;

    public PagamentoAlunoController(PagamentoAlunoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PostMapping
    @Operation(operationId = "registrarPagamentoAluno", description = "Registra o pagamento de cobranças de aluno.")
    @ApiResponse(responseCode = "204", description = "Pagamento registrado.")
    @BadRequestProblemResponse
    @NotFoundProblemResponse
    public ResponseEntity<Void> registrar(@RequestBody @Valid RegistrarPagamentoAlunoRequest request) {
        pagamentoService.registrar(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(operationId = "buscarPagamentosAlunos", description = "Lista os pagamentos de um aluno.")
    @ApiResponse(responseCode = "200", description = "Pagamentos encontrados.")
    @BadRequestProblemResponse
    public ResponseEntity<Page<PagamentoAlunoResponse>> buscarPagamentos(
        @RequestParam UUID alunoId,
        @ParameterObject @PageableDefault(size = 10) Pageable pageable
    ) {
        return ResponseEntity.ok(pagamentoService.buscarPagamentos(alunoId, pageable));
    }

    @GetMapping("/{pagamentoId}")
    @Operation(operationId = "buscarPagamentoAlunoPorId", description = "Busca um pagamento de aluno pelo ID.")
    @ApiResponse(responseCode = "200", description = "Pagamento encontrado.")
    @BadRequestProblemResponse
    @NotFoundProblemResponse
    public ResponseEntity<PagamentoAlunoDetalheResponse> buscarPorId(
        @PathVariable UUID pagamentoId
    ) {
        return ResponseEntity.ok(pagamentoService.buscarPorId(pagamentoId));
    }

    @DeleteMapping("/{pagamentoId}")
    @Operation(operationId = "cancelarPagamentoAluno", description = "Cancela um pagamento de aluno.")
    @ApiResponse(responseCode = "204", description = "Pagamento cancelado.")
    @BadRequestProblemResponse
    @NotFoundProblemResponse
    public ResponseEntity<Void> cancelar(@PathVariable UUID pagamentoId) {
        pagamentoService.cancelar(pagamentoId);
        return ResponseEntity.noContent().build();
    }
}
