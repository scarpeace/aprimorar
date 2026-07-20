package aprimorar.atendimentos.controller;

import aprimorar.atendimentos.dto.AtendimentoFiltroRequest;
import aprimorar.atendimentos.dto.AlunoRelatorioResponse;
import aprimorar.atendimentos.dto.AlunoResumoFinanceiroResponse;
import aprimorar.atendimentos.dto.CalendarioMensalAtendimentosResponse;
import aprimorar.atendimentos.dto.ColaboradorResumoFinanceiroResponse;
import aprimorar.atendimentos.dto.RelatorioAtendimentosResponse;
import aprimorar.atendimentos.service.AtendimentoQueryService;
import aprimorar.atendimentos.dto.AtendimentoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.UUID;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/atendimentos")
@Tag(name = "Atendimento")
public class AtendimentoQueryController {

    private final AtendimentoQueryService atendimentoQueryService;

    public AtendimentoQueryController(AtendimentoQueryService atendimentoQueryService) {
        this.atendimentoQueryService = atendimentoQueryService;
    }

    @GetMapping
    @Operation(operationId = "getAtendimentos",description = "Lista atendimentos com paginacao, ordenacao e filtros opcionais.")
    @ApiResponse(responseCode = "200", description = "Pagina de atendimentos retornada conforme os filtros informados.")
    public ResponseEntity<Page<AtendimentoResponse>> getAtendimentos(
        @ParameterObject Pageable pageable,
        @ParameterObject AtendimentoFiltroRequest filtro
    ) {
        return ResponseEntity.ok(atendimentoQueryService.getAtendimentos(pageable, filtro));
    }

    @GetMapping("/{id}")
    @Operation(operationId = "getAtendimentoById", description = "Consulta um atendimento especifico pelo ID.")
    @ApiResponse(responseCode = "200", description = "Atendimento encontrado e retornado.")
    public ResponseEntity<AtendimentoResponse> getAtendimentoById(@PathVariable Long id) {
        return ResponseEntity.ok(atendimentoQueryService.findAtendimentoById(id));
    }

    @GetMapping("/calendario")
    @Operation(operationId = "getCalendarioAtendimentos", description = "Retorna o conteúdo para montar o calendário de atendimentos.")
    @ApiResponse(responseCode = "200", description = "Calendário retornado com sucesso.")
    public ResponseEntity<CalendarioMensalAtendimentosResponse> getCalendarioAtendimentos(@RequestParam YearMonth anoMes) {
        return ResponseEntity.ok(atendimentoQueryService.getCalendarioAtendimentos(anoMes));
    }

    @GetMapping("/relatorio")
    @Operation(operationId = "getRelatorioAtendimentos", description = "Retorna o conteúdo para montar o relatório.")
    @ApiResponse(responseCode = "200", description = "Relatório retornado com sucesso.")
    public ResponseEntity<RelatorioAtendimentosResponse> getAtendimentosRelatorio(@RequestParam YearMonth anoMes) {
        return ResponseEntity.ok(atendimentoQueryService.getRelatorioAtendimentos(anoMes));
    }

    @GetMapping("/colaboradores/{colaboradorId}/resumo-financeiro")
    @Operation(operationId = "getResumoFinanceiroColaborador", description = "Retorna o resumo financeiro mensal de um colaborador.")
    @ApiResponse(responseCode = "200", description = "Resumo financeiro retornado com sucesso.")
    public ResponseEntity<ColaboradorResumoFinanceiroResponse> getResumoFinanceiroColaborador(
        @PathVariable java.util.UUID colaboradorId,
        @RequestParam YearMonth anoMes
    ) {
        return ResponseEntity.ok(atendimentoQueryService.getResumoFinanceiroColaborador(colaboradorId, anoMes));
    }

    @GetMapping("/alunos/{alunoId}/resumo-financeiro")
    @Operation(operationId = "getResumoFinanceiroAluno", description = "Retorna o resumo financeiro mensal de um aluno.")
    @ApiResponse(responseCode = "200", description = "Resumo financeiro retornado com sucesso.")
    public ResponseEntity<AlunoResumoFinanceiroResponse> getResumoFinanceiroAluno(
        @PathVariable java.util.UUID alunoId,
        @RequestParam YearMonth anoMes
    ) {
        return ResponseEntity.ok(atendimentoQueryService.getResumoFinanceiroAluno(alunoId, anoMes));
    }

    @GetMapping("/alunos/{alunoId}/relatorio")
    @Operation(operationId = "getRelatorioAluno", description = "Retorna o relatório de atendimentos de um aluno em um período.")
    @ApiResponse(responseCode = "200", description = "Relatório retornado com sucesso.")
    public ResponseEntity<AlunoRelatorioResponse> getRelatorioAluno(
        @PathVariable UUID alunoId,
        @RequestParam LocalDate dataInicio,
        @RequestParam LocalDate dataFim
    ) {
        return ResponseEntity.ok(atendimentoQueryService.getRelatorioAluno(alunoId, dataInicio, dataFim));
    }

}
