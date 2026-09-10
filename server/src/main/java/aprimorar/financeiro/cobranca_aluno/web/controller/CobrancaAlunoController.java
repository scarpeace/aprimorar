package aprimorar.financeiro.cobranca_aluno.web.controller;

import aprimorar.financeiro.cobranca_aluno.service.CobrancaAlunoServiceImpl;
import aprimorar.financeiro.cobranca_aluno.web.dto.CancelarCobrancasAlunoRequest;
import aprimorar.financeiro.cobranca_aluno.web.dto.CobrancaAlunoResponse;
import aprimorar.financeiro.cobranca_aluno.web.dto.PagarCobrancasAlunoRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/cobrancas-alunos")
@Tag(name = "Cobrança de aluno")
public class CobrancaAlunoController {

    private final CobrancaAlunoServiceImpl cobrancaService;

    public CobrancaAlunoController(CobrancaAlunoServiceImpl cobrancaService) {
        this.cobrancaService = cobrancaService;
    }

    @PostMapping("/pagar")
    @Operation(operationId = "registrarPagamentoCobrancasAluno", description = "Marca uma ou mais cobranças pendentes como pagas")
    @ApiResponse(responseCode = "200", description = "Cobranças pagas com sucesso")
    public ResponseEntity<List<CobrancaAlunoResponse>> registrarPagamento(
        @RequestBody @Valid PagarCobrancasAlunoRequest request
    ) {
        return ResponseEntity.ok(cobrancaService.registrarPagamento(request));
    }

    @PatchMapping("/cancelar")
    @Operation(operationId = "cancelarPagamentoCobrancaAluno", description = "Cancela uma ou mais cobranças pagas")
    @ApiResponse(responseCode = "200", description = "Cobranças retornadas para pendente")
    public ResponseEntity<List<CobrancaAlunoResponse>> cancelarPagamento(
        @RequestBody @Valid CancelarCobrancasAlunoRequest request
    ) {
        return ResponseEntity.ok(cobrancaService.cancelarPagamento(request));
    }
}
